package com.misterjedu.edanfo.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.misterjedu.edanfo.R
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.fragment_sign_up_header_img


class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Load Header Image with Glide
        Glide.with(this)
            .load(R.drawable.danfo_curved_bg_2)
            .into(fragment_sign_up_header_img);

        //Navigate to PhoneActivationFragment to SignUpFragment
        fragment_login_create_my_account_tv.setOnClickListener {
            val action =  LoginFragmentDirections
                .actionLoginFragmentToSignUpFragment("Sign Up")
            findNavController().navigate(action)
        }

        //Navigate to PhoneActivationFragment to change Password
        fragment_login_forgot_password_tv.setOnClickListener {
            val action =  LoginFragmentDirections
                .actionLoginFragmentToSignUpFragment("Change Password")
            findNavController().navigate(action)
        }


    }

}