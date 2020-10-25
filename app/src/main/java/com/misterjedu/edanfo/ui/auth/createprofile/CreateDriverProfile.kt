package com.misterjedu.edanfo.ui.auth.createprofile

import android.R.attr.password
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.helpers.*
import com.misterjedu.edanfo.ui.main.driver.DriverActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_create_driver_profile.*
import javax.inject.Inject


@AndroidEntryPoint
class CreateDriverProfile : Fragment() {


    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private lateinit var createDriverAccountButton: Button

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

        createDriverAccountButton = fragment_driver_profile_btn

        //Form Field Validation
        validateFields()

        //Firebase Sign in Authentication on button clicked
        fragment_driver_profile_btn.setOnClickListener {
            //Show Progress Bar when Button's clicked and disable Button
            fragment_driver_profile_progress_bar.show(fragment_driver_profile_btn)

            val email = fragment_driver_email_et.text.toString()
            val password = fragment_driver_password_et.text.toString()

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        //Hide Progress bar  and enable button when result comes back
                        fragment_driver_profile_progress_bar.hide(fragment_driver_profile_btn)

                        // Login and Start Activity for Driver
                        val intent = Intent(requireContext(), DriverActivity::class.java)
                        startActivity(intent)
                        //Finish Authentication Activity  here and user moves to a new Driver Activity
                        requireActivity().finish()
                    } else {
                        fragment_driver_profile_progress_bar.hide(fragment_driver_profile_btn)
                        // If sign in fails, display a message to the user.

                        when (task.exception) {
                            is FirebaseAuthUserCollisionException -> {
                                showSnackBar(
                                    fragment_driver_profile_btn, "You are already Registered"
                                )
                            }
                            is FirebaseNetworkException -> {
                                showSnackBar(
                                    fragment_driver_profile_btn,
                                    "Network Error! Check your Network and try Again",
                                )
                            }
                            else -> {
                                showSnackBar(
                                    fragment_driver_profile_btn,
                                    task.exception?.message.toString()
                                )
                            }
                        }

                    }
                }
        }


        //Back Arrow
        fragment_create_driver_profile_back_arrow_iv.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun validateFields() {

        //Validate individual input textFields
        fragment_driver_first_name_et.watchToValidate(
            EditField.NAME, fragment_driver_first_name_til
        )
        fragment_driver_last_name_et.watchToValidate(
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
        fragment_driver_first_name_et.addTextChangedListener(watcher);
        fragment_driver_last_name_et.addTextChangedListener(watcher);
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
                !(!validateName(fragment_driver_first_name_et.text.toString()) or
                        !validateName(fragment_driver_last_name_et.text.toString()) or
                        !validateEmail(fragment_driver_email_et.text.toString()) or
                        !validateName(fragment_driver_plate_number_et.text.toString()) or
                        !validatePassword(fragment_driver_password_et.text.toString()) or
                        !validateRepeatPassword(
                            fragment_driver_password_et.text.toString(),
                            fragment_driver_repeat_password_et.text.toString()
                        ))
        }
    }
}
