package com.misterjedu.edanfo.ui.main.driver.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.helpers.checkItem
import kotlinx.android.synthetic.main.activity_driver.*
import kotlinx.android.synthetic.main.fragment_driver_profile.*

class DriverProfile : Fragment() {

    private lateinit var newPostDialog: CreateDestinationsPopUpFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Launch Create Destination Popup fragment
        fragment_drvier_profile_launch_trip_btn.setOnClickListener {
            newPostDialog = CreateDestinationsPopUpFragment()
            newPostDialog.show(activity?.supportFragmentManager!!, "Add Dialog")
        }

        fragment_driver_profile_trip_destination_tv.setOnClickListener {
            findNavController().navigate(R.id.action_driverProfile_to_currentPassengers)
        }

        //        Navigate to Driver home on Back Press
        requireActivity().onBackPressedDispatcher.addCallback {
            if (findNavController().currentDestination?.id == R.id.driverProfile
            ) {
                activity?.finish()
            } else {
                findNavController().popBackStack()
            }
        }

    }
}
