package com.misterjedu.edanfo.ui.auth.createprofile

import android.app.Activity
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
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.data.firebasedata.PassengerDetail
import com.misterjedu.edanfo.data.firebasedata.User
import com.misterjedu.edanfo.ui.main.driver.DriverActivity
import com.misterjedu.edanfo.ui.main.passenger.PassengerActivity
import com.misterjedu.edanfo.utils.*
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_create_driver_profile.*
import kotlinx.android.synthetic.main.fragment_create_passenger_profile.*
import java.io.InputStream
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CreatePassengerProfile : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    private val CHOOSE_IMAGE = 101

    private var profileImageDownloadUrl: String? = null
    private lateinit var passengerProfileImgUri: Uri
    private lateinit var passengerFirstName: String
    private lateinit var passengerLastName: String
    private lateinit var passengerEmail: String
    private lateinit var passengerPassword: String
    private lateinit var passengerPhoneNumber: String

    private lateinit var userId: String

    private lateinit var profileImage: CircleImageView
    private lateinit var uploadImageText: TextView
    private lateinit var uploadImageButton: Button
    private lateinit var uploadImageButtonProgressBar: ProgressBar
    private lateinit var firstNameEditText: EditText
    private lateinit var firstNameTil: TextInputLayout
    private lateinit var lastNameEditText: EditText
    private lateinit var lastNameTil: TextInputLayout
    private lateinit var emailAddressEditText: EditText
    private lateinit var emailAddressTil: TextInputLayout
    private lateinit var passWordEditText: EditText
    private lateinit var passWordTil: TextInputLayout
    private lateinit var repeatPasswordEditText: EditText
    private lateinit var repeatPasswordTil: TextInputLayout
    private lateinit var phoneNumberEditText: EditText
    private lateinit var phoneNumberEditTil: TextInputLayout

    private lateinit var createPassengerAccountButton: Button
    private lateinit var createPassengerProgressBar: ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_passenger_profile, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Initialize all Views to be used
        createPassengerAccountButton = fragment_passenger_account_btn
        profileImage = passenger_account_profile_image
        uploadImageButton = fragment_create_passenger_profile_upload_image_button
        uploadImageText = fragment_create_passenger_image_upload_status_tv
        uploadImageButtonProgressBar = fragment_create_passenger_profile_image_progress_bar
        firstNameEditText = fragment_passenger_first_name_et
        firstNameTil = fragment_passenger_first_name_til
        lastNameEditText = fragment_passenger_last_name_et
        lastNameTil = fragment_passenger_last_name_til
        phoneNumberEditText = fragment_passenger_phone_number_et
        phoneNumberEditTil = fragment_passenger_phone_number_til
        emailAddressEditText = fragment_passenger_email_et
        emailAddressTil = fragment_passenger_email_til
        passWordEditText = fragment_passenger_password_et
        passWordTil = fragment_passenger_password_til
        repeatPasswordEditText = fragment_passenger_repeat_password_et
        repeatPasswordTil = fragment_passenger_repeat_password_til
        createPassengerProgressBar = fragment_create_passenger_profile_button_progress_bar


        //Form Field Validation
        validateFields()

        profileImage.setOnClickListener {
            showImageChooser()
        }


        //Upload Image to Firebase
        uploadImageButton.setOnClickListener {
            uploadImageToFireBase()
        }


        //Firebase Sign in Authentication starts on button clicked
        createPassengerAccountButton.setOnClickListener {
            authenticatePassenger()
        }


        fragment_create_passenger_profile_back_arrow_iv.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun validateFields() {
        firstNameEditText.watchToValidate(
            EditField.NAME, firstNameTil
        )
        lastNameEditText.watchToValidate(
            EditField.NAME, lastNameTil
        )
        emailAddressEditText.watchToValidate(
            EditField.EMAIL, emailAddressTil
        )
        passWordEditText.watchToValidate(
            EditField.PASSWORD, passWordTil
        )
        phoneNumberEditText.watchToValidate(
            EditField.PHONE, phoneNumberEditTil
        )
        repeatPasswordEditText.watchToValidate(
            EditField.REPEATPASSWORD,
            repeatPasswordTil,
            passWordEditText
        )


        //Watch every text field and enable button when all fields are validated.
        firstNameEditText.addTextChangedListener(watcher)
        lastNameEditText.addTextChangedListener(watcher)
        emailAddressEditText.addTextChangedListener(watcher)
        passWordEditText.addTextChangedListener(watcher)
        phoneNumberEditText.addTextChangedListener(watcher)
        repeatPasswordEditText.addTextChangedListener(watcher)
    }


    //Enable button once all fields are validated
    private val watcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {

            //Enable Button when all fields are validated
            createPassengerAccountButton.isEnabled =
                !(!validateName(firstNameEditText.text.toString()) or
                        !validateName(lastNameEditText.text.toString()) or
                        !validateEmail(emailAddressEditText.text.toString()) or
                        !validatePassword(passWordEditText.text.toString()) or
                        !validateNumber(phoneNumberEditText.text.toString()) or
                        !validateRepeatPassword(
                            passWordEditText.text.toString(),
                            repeatPasswordEditText.text.toString()
                        )
                        )
        }
    }

    /**
     * Enable User to choose Image from Gallery
     */
    private fun showImageChooser() {
        //Disable Button when User is about choosing another Image
        uploadImageButton.isEnabled = false

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "select profile Image"),
            CHOOSE_IMAGE
        )
    }


    /**
     * Load Image on Image View and save the URI
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data?.data != null) {
                    passengerProfileImgUri = data.data!!
                }

                var inputStream: InputStream? = null
                try {
                    inputStream =
                        passengerProfileImgUri.let {
                            requireActivity().contentResolver.openInputStream(
                                it
                            )
                        }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                BitmapFactory.decodeStream(inputStream)
                profileImage.setImageURI(passengerProfileImgUri)

                //Enable Upload Button when Image Loads Correctly
                uploadImageButton.isEnabled = true
            }
        }
    }


    private fun uploadImageToFireBase() {
        //Show Progress Bar while Uploading
        uploadImageButtonProgressBar.show(uploadImageButton)
        uploadImageText.text = "Uploading to server..."


        val profileImageStorageReference: StorageReference? =
            passengerProfileImgUri.lastPathSegment?.let {
                FirebaseStorage.getInstance().reference.child(
                    it
                )
            }


        //Upload Image to Firebase and returns the URL Reference which is saved globally as "ProfileImageDownloadUrl"
        profileImageStorageReference?.putFile(passengerProfileImgUri)?.addOnSuccessListener {
            profileImageStorageReference.downloadUrl.addOnSuccessListener {
                //Hide Image Progress Bar
                uploadImageButtonProgressBar.hide(uploadImageButton)
                val tapAgain = "Tap to Upload another Image"
                uploadImageText.text = tapAgain

                profileImageDownloadUrl = it.toString()
                showSnackBar(createPassengerAccountButton, "Image Successfully uploaded")

            }
        }?.addOnFailureListener {
            showSnackBar(createPassengerAccountButton, it.message.toString())
        }
    }


    private fun authenticatePassenger() {
        //Show Progress bar and disable create passenger button
        createPassengerProgressBar.show(createPassengerAccountButton)

        if (profileImageDownloadUrl != null) {
            passengerFirstName = firstNameEditText.text.toString()
            passengerLastName = lastNameEditText.text.toString()
            passengerEmail = emailAddressEditText.text.toString()
            passengerPassword = passWordEditText.text.toString()
            passengerPhoneNumber = phoneNumberEditText.text.toString()


            //First, sign up with Email and Password
            firebaseAuth.createUserWithEmailAndPassword(passengerEmail, passengerPassword)
                .addOnCompleteListener { task ->

                    createPassengerProgressBar.hide(createPassengerAccountButton)

                    if (task.isSuccessful) {

                        //Once authentication is done, update the display name and image URL for the Auth
                        updateProfileToFirebaseAuth()

                    } else {
                        // If sign up fails, display a message to the user.
                        when (task.exception) {
                            is FirebaseAuthUserCollisionException -> {
                                showSnackBar(
                                    createPassengerAccountButton,
                                    "You are already Registered. Please, Login "
                                )
                            }
                            is FirebaseNetworkException -> {
                                showSnackBar(
                                    createPassengerAccountButton,
                                    "Network Error! Check your Network and try Again",
                                )
                            }
                            else -> {
                                showSnackBar(
                                    createPassengerAccountButton,
                                    task.exception?.message.toString()
                                )
                            }
                        }
                    }
                }
        } else {
            createPassengerProgressBar.hide(createPassengerAccountButton)
            showSnackBar(createPassengerAccountButton, "Please, Upload a profile Image")
        }

    }


    private fun updateProfileToFirebaseAuth() {
        //Show Progress bar and disable create passenger button
        createPassengerProgressBar.show(createPassengerAccountButton)

        //Get Current already created while authenticating passenger
        val user: FirebaseUser? = firebaseAuth.currentUser

        //User Profile object builder containing Profile picture and Display Name
        val profile: UserProfileChangeRequest = UserProfileChangeRequest.Builder()
            .setDisplayName(
                "${passengerLastName.capitalize(Locale.ROOT)} ${
                    passengerFirstName.capitalize(
                        Locale.ROOT
                    )
                }"
            )
            .setPhotoUri(Uri.parse(profileImageDownloadUrl))
            .build()

        user?.updateProfile(profile)?.addOnCompleteListener {

            if (it.isSuccessful) {
                userId = user.uid

                //Upload a new User and Passenger to Real Time Firebase
                uploadDetailToRealTimeFirebase()

            } else {
                createPassengerProgressBar.hide(createPassengerAccountButton)
            }
        }?.addOnFailureListener {

            createPassengerProgressBar.hide(createPassengerAccountButton)

            showSnackBar(createPassengerAccountButton, it.message.toString())
        }
    }


    private fun uploadDetailToRealTimeFirebase() {
        //Create New User Object
        val userDetail = User(
            null,
            userId,
            passengerPhoneNumber,
            passengerEmail,
            passengerFirstName,
            passengerLastName,
            PASSENGER
        )

        val passengerDetail = PassengerDetail(
            null,
            userId,
            0
        )

        FirebaseAuth.getInstance().currentUser?.uid?.let { userUniqueId ->
            FirebaseDatabase.getInstance().getReference(USER_DETAILS)
                .child(userUniqueId)
                .setValue(userDetail).addOnCompleteListener {
                    if (it.isSuccessful) {
                        FirebaseDatabase.getInstance().getReference(DRIVER)
                            .child(userUniqueId).setValue(passengerDetail)
                            .addOnCompleteListener { passengerUpload ->

                                if (passengerUpload.isSuccessful) {
                                    showSnackBar(
                                        createPassengerAccountButton,
                                        "Registration Successful"
                                    )

                                    //Save User type to shared preference to be used to
                                    // authenticate when re-opening the app
                                    saveToSharedPreference(requireActivity(), USERTYPE, PASSENGER)

                                    // Login and Start Activity for Driver
                                    val intent =
                                        Intent(requireContext(), PassengerActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    createPassengerProgressBar.hide(createPassengerAccountButton)
                                    showSnackBar(
                                        createPassengerAccountButton,
                                        "Your details were not uploaded"
                                    )
                                }
                            }
                    } else {
                        createPassengerProgressBar.hide(createPassengerAccountButton)
                        showSnackBar(
                            createPassengerAccountButton,
                            "Something went wrong"
                        )
                    }
                }
        }
    }
}


