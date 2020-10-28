package com.misterjedu.edanfo.ui.main.driver.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.misterjedu.edanfo.R
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_driver_profile.*
import javax.inject.Inject

@AndroidEntryPoint
class DriverProfile : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private lateinit var newPostDialog: CreateDestinationsPopUpFragment
    private lateinit var profileImage: CircleImageView
    private lateinit var profileName: TextView

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

        profileImage = fragment_driver_profile_image
        profileName = fragment_driver_proflie_name

        //Load User Information
        loadUserInformation()


        // Launch Create Destination Popup fragment
        fragment_drvier_profile_launch_trip_btn.setOnClickListener {
            newPostDialog = CreateDestinationsPopUpFragment()
            newPostDialog.show(activity?.supportFragmentManager!!, "Add Dialog")
        }

        fragment_driver_profile_check_orders_btn.setOnClickListener {
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


    private fun loadUserInformation() {
        val user: FirebaseUser? = firebaseAuth.currentUser


        if (user != null) {
            Log.i("Driver name", "Hello")
            Log.i("Driver Photo URI", user.photoUrl.toString())
        }
//        if (user != null) {
//            if (user.displayName != null) {
//                Glide.with(this)
//                    .load(user.photoUrl.toString())
//                    .into(profileImage)
//
//                profileName.text = user.displayName
//
//            }
//        }
    }
}
