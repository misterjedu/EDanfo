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
import kotlinx.android.synthetic.main.fragment_phone_activation.*

class PhoneActivationFragment : Fragment() {

    private lateinit var fragmentName: String
    private val args: PhoneActivationFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get Argument Passed from SignUpFragment
        fragmentName = args.fragmentName

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
        fragment_phone_activation_send_otp_btn.setOnClickListener {
            if (fragmentName == "Sign Up") {
                findNavController().navigate(R.id.createProfileFragment)
            } else {
                findNavController().navigate(R.id.changePasswordFragment2)
            }
        }

        // Back Arrow
        fragment_phone_activation_back_arrow_iv.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
