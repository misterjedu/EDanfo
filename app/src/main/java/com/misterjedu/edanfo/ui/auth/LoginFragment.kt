package com.misterjedu.edanfo.ui.auth

import android.content.Intent
import android.os.Bundle
import android.service.autofill.UserData
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.data.firebasedata.User
import com.misterjedu.edanfo.ui.main.driver.DriverActivity
import com.misterjedu.edanfo.ui.main.passenger.PassengerActivity
import com.misterjedu.edanfo.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.fragment_sign_up_header_img
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var driverInputEmail: EditText
    private lateinit var logInButton: Button
    private lateinit var driverInputPassword: EditText


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        validateField()

        driverInputEmail = fragment_login_email_et
        driverInputPassword = fragment_login_password_et
        logInButton = fragment_login_login_btn


        // Load Header Image with Glide
        Glide.with(this)
            .load(R.drawable.danfo_curved_bg_2)
            .into(fragment_sign_up_header_img)

        // Navigate to PhoneActivationFragment to SignUpFragment
        fragment_login_create_my_account_tv.setOnClickListener {
            //TODO(Action used if using OTP)
//            val action = LoginFragmentDirections
//                .actionLoginFragmentToSignUpFragment("Sign Up")
            findNavController().navigate(R.id.createProfileLanding)
        }


        // Login and Start Activity for Driver
        fragment_login_login_btn.setOnClickListener {
            userLogin()
        }

        // Navigate to PhoneActivationFragment to change Password
        fragment_login_forgot_password_tv.setOnClickListener {
            //TODO(Action used if using OTP)
//            val action = LoginFragmentDirections
//                .actionLoginFragmentToSignUpFragment("Change Password")
            findNavController().navigate(R.id.changePasswordFragment2)
        }


        //Close App if User exists from Login Fragment
        requireActivity().onBackPressedDispatcher.addCallback {
            if (findNavController().currentDestination?.id == R.id.loginFragment
            ) {
                activity?.finish()
            } else {
                findNavController().popBackStack()
            }
        }
    }

    private fun userLogin() {
        //Disable button and show Progress Bar
        fragment_login_progress_bar.show(fragment_login_login_btn)
        val userPassWord = driverInputPassword.text.toString()
        val userEmail = driverInputEmail.text.toString()


        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassWord)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (firebaseAuth.currentUser != null) {
                        //Fetch User type from RealTime Firebase
                        FirebaseDatabase.getInstance().getReference(USER_DETAILS)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        for (childSnapshot in snapshot.children) {
                                            val userData = childSnapshot.getValue(User::class.java)

                                            //Start Corresponding Activity based on User Type and finish Auth activity
                                            if (userData != null && userData.userType == DRIVER && firebaseAuth.currentUser!!.uid == userData.userId) {
                                                saveToSharedPreference(
                                                    requireActivity(),
                                                    USERTYPE,
                                                    DRIVER
                                                )

                                                val intent =
                                                    Intent(
                                                        requireContext(),
                                                        DriverActivity::class.java
                                                    )
                                                startActivity(intent)
                                                requireActivity().finish()
                                            } else if (userData != null && userData.userType == PASSENGER && firebaseAuth.currentUser!!.uid == userData.userId) {

                                                saveToSharedPreference(
                                                    requireActivity(),
                                                    USERTYPE,
                                                    PASSENGER
                                                )

                                                val intent =
                                                    Intent(
                                                        requireContext(),
                                                        PassengerActivity::class.java
                                                    )
                                                startActivity(intent)
                                                requireActivity().finish()
                                            } else {
                                                showSnackBar(
                                                    fragment_login_login_btn,
                                                    "An error occurred"
                                                )
                                            }
                                        }

                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    fragment_login_progress_bar.hide(fragment_login_login_btn)
                                    showSnackBar(fragment_login_login_btn, error.message)
                                }
                            })
                    }
                } else {
                    //Disable Button and show Progress bar
                    it.exception?.message?.let { it1 ->
                        showSnackBar(
                            logInButton,
                            it1
                        )
                    }
                }
            }
    }


    private fun validateField() {
        fragment_login_email_et.watchToValidate(
            EditField.EMAIL,
            fragment_login_email_text_layout_tl,
        )

        fragment_login_password_et.watchToValidate(
            EditField.PASSWORD, fragment_login_password_til
        )

        fragment_login_email_et.addTextChangedListener(watcher)
        fragment_login_password_et.addTextChangedListener(watcher)
    }


    private val watcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            fragment_login_login_btn.isEnabled =
                !(!validateEmail(fragment_login_email_et.text.toString()) or
                        !validatePassword(fragment_login_password_et.text.toString().trim()))
        }
    }

    //If User is already Logged in, Go straight to the Dashboard
    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null && loadFromSharedPreference(
                requireActivity(),
                USERTYPE
            ) == DRIVER
        ) {
            val intent =
                Intent(requireContext(), DriverActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        } else if (firebaseAuth.currentUser != null && loadFromSharedPreference(
                requireActivity(),
                USERTYPE
            ) == PASSENGER
        ) {
            val intent =
                Intent(requireContext(), PassengerActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}
