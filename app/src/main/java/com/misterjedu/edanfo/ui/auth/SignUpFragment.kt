package com.misterjedu.edanfo.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.misterjedu.edanfo.R
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : Fragment() {

    private lateinit var fragmentName: String
    private val args: SignUpFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get Argument Passed from Login fragment
        fragmentName = args.fragmentName

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Set Various text in the fragment based on the passed in Argument
        setVarTexts()

        // Load Header Image
        Glide.with(this)
            .load(R.drawable.danfo_curved_bg_2)
            .into(fragment_sign_up_header_img)

        // Continue Button
        fragment_sign_up_continue_btn.setOnClickListener {
            val action = SignUpFragmentDirections
                .actionSignUpFragmentToPhoneActivationFragment(fragmentName)
            findNavController().navigate(action)
        }

        // Back Arrow Pop back Stack
        fragment_sign_up_back_arrow_iv.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    // Change several texts and hide visibility if it's loaded as Forgot Password Framgment
    private fun setVarTexts() {
        if (fragmentName == "Change Password") {
            fragment_sign_up_hello_tv.text = "You have a Problem?"
            fragment_sign_up_welcome_tv.text = "Don't Worry!"
            fragment_sign_up_verify_code_tv.text = "No Problem? Sign in"
            fragment_sign_up_terms_of_service_tv.visibility = View.INVISIBLE
            fragment_sign_up_menu_verification_tv.text = "Forgot Password"
        }
    }
}
