package com.misterjedu.edanfo.ui.auth.createprofile

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.data.DriverDetail
import com.misterjedu.edanfo.ui.main.driver.DriverActivity
import com.misterjedu.edanfo.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_create_driver_profile.*
import java.io.InputStream
import javax.inject.Inject


@AndroidEntryPoint
class CreateDriverProfile : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var createDriverAccountButton: Button
    private val CHOOSE_IMAGE = 101
    private lateinit var profileImageDownloadUrl: String
    private lateinit var driverProfileImg: Uri
    private lateinit var driverFirstName: String
    private lateinit var driverLastName: String
    private lateinit var driverEmail: String
    private lateinit var driverNumber: String


    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_driver_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        firstNameEditText = fragment_driver_first_name_et
        lastNameEditText = fragment_driver_last_name_et
        emailEditText = fragment_driver_email_et
        passwordEditText = fragment_driver_password_et

        loadDriverPhoneNumber()


        createDriverAccountButton = fragment_driver_profile_btn

        //Form Field Validation
        validateFields()

        //Select Picture Gallery
        driver_account_profile_image.setOnClickListener {
            showImageChooser()
        }

        //Firebase Sign in Authentication on button clicked
        createDriverAccountButton.setOnClickListener {
            authenticateDriver()
        }

        //Back Arrow
        fragment_create_driver_profile_back_arrow_iv.setOnClickListener {
            findNavController().popBackStack()
        }

        //Start Uploading Image to Firebase
        fragment_create_driver_profile_upload_image_btn.setOnClickListener {
            uploadImageToFireBase()
        }


    }


    /* ------------------End of OnActivityCreated---------------------------*/


    private fun validateFields() {
        //Validate individual input textFields
        firstNameEditText.watchToValidate(
            EditField.NAME, fragment_driver_first_name_til
        )
        lastNameEditText.watchToValidate(
            EditField.NAME, fragment_driver_last_name_til
        )
        fragment_driver_email_et.watchToValidate(
            EditField.EMAIL, fragment_driver_email_til
        )
        fragment_driver_plate_number_et.watchToValidate(
            EditField.NAME, fragment_driver_plate_number_til
        )
        fragment_driver_password_et.watchToValidate(
            EditField.PASSWORD, fragment_driver_password_til
        )
        fragment_driver_repeat_password_et.watchToValidate(
            EditField.REPEATPASSWORD,
            fragment_driver_repeat_password_til,
            fragment_driver_password_et
        )


        //Watch every text field and enable button when all fields are validated.
        firstNameEditText.addTextChangedListener(watcher);
        lastNameEditText.addTextChangedListener(watcher);
        fragment_driver_email_et.addTextChangedListener(watcher);
        fragment_driver_plate_number_et.addTextChangedListener(watcher);
        fragment_driver_password_et.addTextChangedListener(watcher);
        fragment_driver_repeat_password_et.addTextChangedListener(watcher);
    }


    //Enable button once all fields are validated
    private val watcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            //Enable Button when all fields are validated
            createDriverAccountButton.isEnabled =
                !(!validateName(firstNameEditText.text.toString()) or
                        !validateName(lastNameEditText.text.toString()) or
                        !validateEmail(fragment_driver_email_et.text.toString()) or
                        !validateName(fragment_driver_plate_number_et.text.toString()) or
                        !validatePassword(fragment_driver_password_et.text.toString()) or
                        !validateRepeatPassword(
                            fragment_driver_password_et.text.toString(),
                            fragment_driver_repeat_password_et.text.toString()
                        ))
        }
    }


    /**
     * Enable User to choose Image from Gallery
     */
    private fun showImageChooser() {
        //Disable Button when User is about choosing another Image
        fragment_create_driver_profile_upload_image_btn.isEnabled = false

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "select profile Image"), CHOOSE_IMAGE)
    }


    /**
     * Load Image on Image View and save the URI
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data?.data != null) {
                    driverProfileImg = data.data!!
                }

                var inputStream: InputStream? = null
                try {
                    inputStream =
                        driverProfileImg.let { requireActivity().contentResolver.openInputStream(it) }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                BitmapFactory.decodeStream(inputStream)
                driver_account_profile_image.setImageURI(driverProfileImg)

                //Enable Upload Button when Image Loads Correctly
                fragment_create_driver_profile_upload_image_btn.isEnabled = true
            }
        }
    }


    /**
     * Send Image to Firebase
     */
    private fun uploadImageToFireBase() {
        //Hide Image Progress Bar
        fragment_create_driver_profile_image_progress_bar.show()

        val profileImageStorageReference: StorageReference? =
            driverProfileImg.lastPathSegment?.let {
                FirebaseStorage.getInstance().reference.child(
                    it
                )
            }

        profileImageStorageReference?.putFile(driverProfileImg)?.addOnSuccessListener {
            profileImageStorageReference.downloadUrl.addOnSuccessListener {
                //Hide Image Progress Bar
                fragment_create_driver_profile_image_progress_bar.hide()

                profileImageDownloadUrl = it.toString()
                showSnackBar(createDriverAccountButton, "Image Successfully uploaded")
                Log.i("Image Store", profileImageDownloadUrl)
            }
        }?.addOnFailureListener {
            showSnackBar(createDriverAccountButton, it.message.toString())
        }
    }


    //Authenticate Driver in Firebase
    private fun authenticateDriver() {

        //Show Progress Bar when Button's clicked and disable Button
        fragment_driver_profile_progress_bar.show(createDriverAccountButton)

        driverEmail = emailEditText.text.toString()
        driverFirstName = firstNameEditText.text.toString()
        driverLastName = lastNameEditText.text.toString()
        val password = passwordEditText.text.toString()

        //First, sign up with Email and Password
        firebaseAuth.createUserWithEmailAndPassword(driverEmail, password)
            .addOnCompleteListener { task ->
                //Hide Progress Bar
                fragment_driver_profile_progress_bar.hide(createDriverAccountButton)

                if (task.isSuccessful) {

                    //Create Driver Detail Object with all Parameters
                    val driverDetail =
                        DriverDetail(driverNumber, driverEmail, driverFirstName, driverLastName)

                    //Second, if sign_up is successful, upload user detail to real time database
                    FirebaseAuth.getInstance().currentUser?.uid?.let { it1 ->
                        FirebaseDatabase.getInstance().getReference("Drivers")
                            .child(it1)
                            .setValue(driverDetail).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    showSnackBar(
                                        createDriverAccountButton,
                                        "Registration Successful"
                                    )
                                    // Login and Start Activity for Driver
                                    val intent =
                                        Intent(requireContext(), DriverActivity::class.java)
                                    startActivity(intent)
                                    //Finish Authentication Activity  here and user moves to a new Driver Activity
                                    requireActivity().finish()

                                } else {
                                    showSnackBar(
                                        createDriverAccountButton,
                                        "Registeration Failed"
                                    )
                                }
                            }
                    }


                } else {
                    // If sign up fails, display a message to the user.
                    when (task.exception) {
                        is FirebaseAuthUserCollisionException -> {
                            showSnackBar(
                                createDriverAccountButton, "You are already Registered"
                            )
                        }
                        is FirebaseNetworkException -> {
                            showSnackBar(
                                createDriverAccountButton,
                                "Network Error! Check your Network and try Again",
                            )
                        }
                        else -> {
                            showSnackBar(
                                createDriverAccountButton,
                                task.exception?.message.toString()
                            )
                        }
                    }

                }
            }
    }

    //Load Phone Number From Shared Preferences
    private fun loadDriverPhoneNumber() {
        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        driverNumber = sharedPreferences.getString(PHONENUMBER, "").toString()
    }
}

