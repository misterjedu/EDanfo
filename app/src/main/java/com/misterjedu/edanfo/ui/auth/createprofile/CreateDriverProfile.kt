package com.misterjedu.edanfo.ui.auth.createprofile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.data.firebasedata.DriverDetail
import com.misterjedu.edanfo.data.firebasedata.User
import com.misterjedu.edanfo.ui.main.driver.DriverActivity
import com.misterjedu.edanfo.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_create_driver_profile.*
import java.io.InputStream
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class CreateDriverProfile : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var createDriverAccountButton: Button
    private val CHOOSE_IMAGE = 101
    private var profileImageDownloadUrl: String? = null
    private lateinit var driverProfileImg: Uri
    private lateinit var driverFirstName: String
    private lateinit var driverLastName: String
    private lateinit var driverEmail: String
    private lateinit var userId: String

    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var tapToUploadTextView: TextView
    private lateinit var uploadImageButton: Button
    private lateinit var phoneNumberEditText: EditText
    private lateinit var vehiclePlateNumber: EditText


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
        createDriverAccountButton = fragment_driver_profile_btn
        tapToUploadTextView = fragment_create_driver_tap_to_upload_tv
        uploadImageButton = fragment_create_driver_profile_upload_image_btn
        phoneNumberEditText = fragment_phone_number_et
        vehiclePlateNumber = fragment_driver_plate_number_et


        //Form Field Validation
        validateFields()

        //Select Picture Gallery
        driver_account_profile_image.setOnClickListener {
            showImageChooser()
        }

        //Firebase Sign in Authentication on button clicked
        createDriverAccountButton.setOnClickListener {
            //First Upload Profile to firebase
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
        phoneNumberEditText.watchToValidate(
            EditField.PHONE,
            fragment_phone_number_til
        )
        fragment_driver_repeat_password_et.watchToValidate(
            EditField.REPEATPASSWORD,
            fragment_driver_repeat_password_til,
            fragment_driver_password_et
        )


        //Watch every text field and enable button when all fields are validated.
        firstNameEditText.addTextChangedListener(watcher);
        lastNameEditText.addTextChangedListener(watcher);
        phoneNumberEditText.addTextChangedListener(watcher)
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
                        ) or !validateNumber(phoneNumberEditText.text.toString())
                        )
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
                uploadImageButton.isEnabled = true
            }
        }
    }


    /**
     * Send Image to Firebase
     */
    private fun uploadImageToFireBase() {
        //Hide Image Progress Bar
        fragment_create_driver_profile_image_progress_bar.show(uploadImageButton)
        tapToUploadTextView.text = "Uploading to server..."


        val profileImageStorageReference: StorageReference? =
            driverProfileImg.lastPathSegment?.let {
                FirebaseStorage.getInstance().reference.child(
                    it
                )
            }

        profileImageStorageReference?.putFile(driverProfileImg)?.addOnSuccessListener {
            profileImageStorageReference.downloadUrl.addOnSuccessListener {
                //Hide Image Progress Bar
                fragment_create_driver_profile_image_progress_bar.hide(uploadImageButton)
                val tapAgain = "Tap to Upload another Image"
                tapToUploadTextView.text = tapAgain

                profileImageDownloadUrl = it.toString()
                showSnackBar(createDriverAccountButton, "Image Successfully uploaded")

            }
        }?.addOnFailureListener {
            showSnackBar(createDriverAccountButton, it.message.toString())
        }
    }


    //Generate a unique number the bus. E.g Bus 001
    private fun generateUniqueBusName() {
        FirebaseDatabase.getInstance().getReference(USER_DETAILS)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var driverCount = 1
                        for (userSnapShot in snapshot.children) {
                            val driver = userSnapShot.getValue(User::class.java)
                            driver?.id = userSnapShot.key
                            if (driver != null && driver.userType == DRIVER) {
                                driverCount += driverCount
                            }
                        }
                        val busNumFormatted = "%04d".format(driverCount)
                        updateAllToFireBase("Bus $busNumFormatted")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }


    //Create a new user object with details about user(driver) and upload to database
    fun updateAllToFireBase(busNumber: String) {

        val phoneNum = phoneNumberEditText.text.toString()
        val email = emailEditText.text.toString()
        val firstName = firstNameEditText.text.toString()
        val lastName = lastNameEditText.text.toString()

        //Create New User Object
        val userDetail = User(
            null, userId, phoneNum, email, firstName, lastName, DRIVER
        )

        val driverDetail = DriverDetail(
            null,
            userId,
            vehiclePlateNumber.text.toString(),
            0F,
            null,
            null,
            0,
            0,
            busNumber
        )

        FirebaseAuth.getInstance().currentUser?.uid?.let { userUniqueId ->
            FirebaseDatabase.getInstance().getReference(USER_DETAILS)
                .child(userUniqueId)
                .setValue(userDetail).addOnCompleteListener {
                    if (it.isSuccessful) {
                        FirebaseDatabase.getInstance().getReference(DRIVER)
                            .child(userUniqueId).setValue(driverDetail)
                            .addOnCompleteListener { driverUpload ->
                                if (driverUpload.isSuccessful) {
                                    showSnackBar(
                                        createDriverAccountButton,
                                        "Registration Successful"
                                    )

                                    //Save User type to shared preference to be used to
                                    // authenticate when re-opening the app
                                    saveToSharedPreference(requireActivity(), USERTYPE, DRIVER)

                                    // Login and Start Activity for Driver
                                    val intent =
                                        Intent(requireContext(), DriverActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    fragment_driver_profile_progress_bar.hide(
                                        createDriverAccountButton
                                    )
                                    showSnackBar(
                                        createDriverAccountButton,
                                        "Your details were not uploaded"
                                    )
                                }
                            }
                    } else {
                        fragment_driver_profile_progress_bar.hide(createDriverAccountButton)
                        showSnackBar(
                            createDriverAccountButton,
                            "Something went wrong"
                        )
                    }
                }
        }
    }

    /**
     * Upload Profile Picture and name to firebase
     */
    private fun updateProfileToFirebaseAuth() {
        driverFirstName = firstNameEditText.text.toString()
        driverLastName = lastNameEditText.text.toString()

        val user: FirebaseUser? = firebaseAuth.currentUser

        //Show Progress Bar when Button's clicked and disable Button
        fragment_driver_profile_progress_bar.show(createDriverAccountButton)

        //User Profile object builder containing Profile picture and Display Name
        val profile: UserProfileChangeRequest = UserProfileChangeRequest.Builder()
            .setDisplayName(
                "${driverLastName.capitalize(Locale.ROOT)} ${
                    driverFirstName.capitalize(
                        Locale.ROOT
                    )
                }"
            )
            .setPhotoUri(Uri.parse(profileImageDownloadUrl))
            .build()

        user?.updateProfile(profile)?.addOnCompleteListener {

            if (it.isSuccessful) {
                userId = user.uid
                generateUniqueBusName()

            } else {
                fragment_driver_profile_progress_bar.hide(createDriverAccountButton)
            }
        }?.addOnFailureListener {
            fragment_driver_profile_progress_bar.hide(createDriverAccountButton)
            showSnackBar(createDriverAccountButton, it.message.toString())
        }
    }


    //Authenticate Driver in Firebase
    private fun authenticateDriver() {

        fragment_driver_profile_progress_bar.show(createDriverAccountButton)

        if (profileImageDownloadUrl != null) {
            driverEmail = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            //First, sign up with Email and Password
            firebaseAuth.createUserWithEmailAndPassword(driverEmail, password)
                .addOnCompleteListener { task ->
                    //Hide Progress Bar
                    fragment_driver_profile_progress_bar.hide(createDriverAccountButton)

                    if (task.isSuccessful) {

                        updateProfileToFirebaseAuth()

                    } else {
                        fragment_driver_profile_progress_bar.hide(createDriverAccountButton)
                        // If sign up fails, display a message to the user.
                        when (task.exception) {
                            is FirebaseAuthUserCollisionException -> {
                                showSnackBar(
                                    createDriverAccountButton,
                                    "You are already Registered. Please, Login "
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

        } else {
            fragment_driver_profile_progress_bar.hide(createDriverAccountButton)
            showSnackBar(createDriverAccountButton, "Please, Upload a profile Image")
        }
    }
}
