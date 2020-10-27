package com.misterjedu.edanfo.ui.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.hbb20.CountryCodePicker
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.ui.main.driver.DriverActivity
import com.misterjedu.edanfo.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.fragment_sign_up_header_img
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var driverEmail: String


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

        driverEmail = loadFromSharedPreference(requireActivity(), DRIVEREMAILADDRESS)

        // Load Header Image with Glide
        Glide.with(this)
            .load(R.drawable.danfo_curved_bg_2)
            .into(fragment_sign_up_header_img)

        // Navigate to PhoneActivationFragment to SignUpFragment
        fragment_login_create_my_account_tv.setOnClickListener {
            val action = LoginFragmentDirections
                .actionLoginFragmentToSignUpFragment("Sign Up")
            findNavController().navigate(action)
        }


        // Login and Start Activity for Driver
        fragment_login_login_btn.setOnClickListener {
            userLogin()
        }

        // Navigate to PhoneActivationFragment to change Password
        fragment_login_forgot_password_tv.setOnClickListener {
            val action = LoginFragmentDirections
                .actionLoginFragmentToSignUpFragment("Change Password")
            findNavController().navigate(action)
        }
    }

    private fun userLogin() {

        //Disable button and show Progress Bar
        fragment_login_progress_bar.show(fragment_login_login_btn)
        val userPassWord = fragment_login_password_et.text.toString()

        firebaseAuth.signInWithEmailAndPassword(driverEmail, userPassWord).addOnCompleteListener {
            fragment_login_progress_bar.hide(fragment_login_login_btn)
            if (it.isSuccessful) {



                //Start Driver Activity
                val intent = Intent(requireContext(), DriverActivity::class.java)
                startActivity(intent)

                //Finish Authentication Activity  here and user moves to a new Driver Activity
                requireActivity().finish()
            } else {
                //Disable Button and show Progress bar

                it.exception?.message?.let { it1 -> showSnackBar(fragment_login_login_btn, it1) }
            }
        }
    }


    private fun validateField() {
        fragment_login_phone_number_et.watchToValidate(
            EditField.PHONE,
            fragment_login_phone_number_text_layout_tl,
            null,
            fragment_login_country_picker
        )

        fragment_login_password_et.watchToValidate(
            EditField.PASSWORD, fragment_login_password_til
        )

        fragment_login_phone_number_et.addTextChangedListener(watcher)
        fragment_login_password_et.addTextChangedListener(watcher)
    }


    private val watcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            val phoneNumber =
                fragment_login_country_picker.textView_selectedCountry?.text.toString() +
                        fragment_login_phone_number_et.text.toString().trim()
            fragment_login_login_btn.isEnabled =
                !(!validateNumber(phoneNumber) or
                        !validatePassword(fragment_login_password_et.text.toString().trim()))
        }
    }



}
