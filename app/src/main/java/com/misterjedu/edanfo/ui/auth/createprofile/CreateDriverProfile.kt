package com.misterjedu.edanfo.ui.auth.createprofile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.ui.main.driver.DriverActivity
import kotlinx.android.synthetic.main.fragment_create_driver_profile.*

class CreateDriverProfile : Fragment() {

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

        //Back Arrow
        fragment_create_driver_profile_back_arrow_iv.setOnClickListener {
            findNavController().popBackStack()
        }

        fragment_driver_profile_btn.setOnClickListener {
            // Login and Start Activity for Driver

            val intent = Intent(requireContext(), DriverActivity::class.java)

            startActivity(intent)

            //Finish Authentication Activity  here and user moves to a new Driver Activity
            requireActivity().finish()
        }
    }
}
