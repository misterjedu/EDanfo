package com.misterjedu.edanfo.ui.main.driver

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.misterjedu.edanfo.R
import kotlinx.android.synthetic.main.fragment_driver_proflle.*

class DriverProfile : Fragment() {

    private lateinit var newPostDialog: CreateDestinationsPopUpFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_proflle, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Launch Create Destination Popup fragment
        fragment_drvier_profile_launch_trip_btn.setOnClickListener {
            newPostDialog = CreateDestinationsPopUpFragment()
            newPostDialog.show(activity?.supportFragmentManager!!, "Add Dialog")
        }


        fragment_driver_profile_trip_destination_tv.setOnClickListener {
            findNavController().navigate(R.id.currentPassengers)
        }
    }

}