package com.misterjedu.edanfo.ui.auth.createprofile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.ui.main.driver.DriverActivity
import com.misterjedu.edanfo.ui.main.passenger.PassengerActivity
import kotlinx.android.synthetic.main.fragment_create_passenger_profile.*

class CreatePassengerProfile : Fragment() {

    private lateinit var createPassengerProfileButton: Button

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
        createPassengerProfileButton = fragment_passenger_profile_btn


        createPassengerProfileButton.setOnClickListener {
            val intent =
                Intent(requireContext(), PassengerActivity::class.java)
            startActivity(intent)
        }









        fragment_create_passenger_profile_back_arrow_iv.setOnClickListener {
            findNavController().popBackStack()
        }


    }
}
