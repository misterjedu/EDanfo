package com.misterjedu.edanfo.ui.auth.createprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.misterjedu.edanfo.R
import kotlinx.android.synthetic.main.fragment_create_profile_landing.*

class CreateProfileLanding : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_profile_landing, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fragment_create_profile_landing_register_as_driver_btn.setOnClickListener {
            findNavController().navigate(R.id.action_createProfileLanding_to_createDriverProfile)
        }

        fragment_create_profile_landing__register_as_passenger_btn.setOnClickListener {
            findNavController().navigate(R.id.action_createProfileLanding_to_createPassengerProfile)
        }


    }


}
