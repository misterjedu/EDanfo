package com.misterjedu.edanfo.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.data.PhoneToOtp
import kotlinx.android.synthetic.main.fragment_phone_activation.*

class PhoneActivationFragment : Fragment() {

    private lateinit var dataFromSignUp: PhoneToOtp
    private val args: PhoneActivationFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get Argument Passed from SignUpFragment
        dataFromSignUp = args.phoneToOtp

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phone_activation, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Load Header Background Image
        Glide.with(this)
            .load(R.drawable.danfo_curved_bg_2)
            .into(fragment_phone_activation_header_img)

        // Navigate to Create Profile or Change Password based on args.
        fragment_phone_activation_continue_btn.setOnClickListener {
            if (dataFromSignUp.fragmentName == "Sign Up") {
                findNavController().navigate(R.id.action_phoneActivationFragment_to_createProfileLanding)
            } else {
                findNavController().navigate(R.id.action_phoneActivationFragment_to_changePasswordFragment2)
            }
        }


        fragment_phone_activation_resend_code_tv.setOnClickListener {
            Toast.makeText(requireContext(), dataFromSignUp.phoneNumber, Toast.LENGTH_SHORT).show()
        }


        // Back Arrow
        fragment_phone_activation_back_arrow_iv.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
