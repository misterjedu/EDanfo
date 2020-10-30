package com.misterjedu.edanfo.ui.main.driver.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.data.firebasedata.DriverDestination
import com.misterjedu.edanfo.data.firebasedata.Trip
import com.misterjedu.edanfo.utils.hide
import com.misterjedu.edanfo.utils.show
import com.misterjedu.edanfo.viewmodels.firebaseViewModels.DriverDestinationViewModel
import com.misterjedu.edanfo.viewmodels.firebaseViewModels.DriverTripViewModel
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
    private var destinationId: String? = null
    private lateinit var destinationViewModel: DriverDestinationViewModel
    private lateinit var driverTripViewModel: DriverTripViewModel
    private var currentActiveDestination: DriverDestination? = null
    private lateinit var driverDetailBox: ConstraintLayout
    private lateinit var launchButton: Button
    private lateinit var tripStatus: TextView
    private var totalAmountEarned: Int = 0
    private var totalCompletedTrips: List<Trip>? = null
    private var totalCompletedDestinations: List<DriverDestination>? = null
    private var totalTrips: Int = 0


    private val args: DriverProfileArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Set Destination Id from args
        destinationId = args.destinationId
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        driverDetailBox = fragment_driver_profile_trip_detail_box_cl
        launchButton = fragment_drvier_profile_launch_trip_btn
        tripStatus = fragment_driver_profile_trip_status_tv

        //Instantiate DestinationViewModel
        destinationViewModel = ViewModelProvider(this)
            .get(DriverDestinationViewModel::class.java)


        driverTripViewModel = ViewModelProvider(this)
            .get(DriverTripViewModel::class.java)


        //Start Getting Newly Destination Real time Updates and fetch all destination details
        destinationViewModel.fetchDriverDestinations()
        destinationViewModel.getDestinationRealTimeUpdate()
        driverTripViewModel.fetchDriverTrips()
        driverTripViewModel.getTripsRealTimeUpdate()


//        Log.i("Destination", destinationId!!)
        //Get Current Active Destination, which is usually newly added destination
        destinationViewModel.activeDestination.observe(viewLifecycleOwner, {
            //TODO( SHOW LOADER HERE UNTIL FIREBASE SENDS RESULT )
            if (it == null) {
                Log.i("Destination", "Destination is null")
//                //If no active trip, show launch button, and show No active until a trip is active
//                driverDetailBox.hide()
//                launchButton.show()
//                tripStatus.setTextColor(resources.getColor(R.color.colorCancelButton))
//                tripStatus.text = "No Active Trip"
            } else {
                Log.i("Destination", "Destination is not null")
                //Set view based on if there's a destination or not
                driverDetailBox.show()
                launchButton.hide()
                tripStatus.setTextColor(resources.getColor(R.color.colorPrimary))
                tripStatus.text = "Active"
            }

            //Update Current Destination and Destination ID
            if (it != null) {
                destinationId = it.id
                currentActiveDestination = it
            }

        })

        //Observe Total Completed Destinations
        destinationViewModel.completedDestination.observe(viewLifecycleOwner, {
            totalCompletedDestinations = it
            //Get Total Amount Earned
            if (totalCompletedTrips != null) {
                for (trip in totalCompletedTrips!!) {
                    totalAmountEarned += trip.price!!
                }
            }
            fragment_driver_profile_total_trips_tv.text = totalAmountEarned.toString()
        })

        //Observe Driver trips
        driverTripViewModel.completedTrips.observe(viewLifecycleOwner, {
            if (it != null && totalCompletedDestinations != null) {
                totalCompletedTrips = it
                //Set Total Trip
                totalTrips = totalCompletedDestinations?.size!!
                fragment_driver_profile_earnings_tv.text = totalTrips.toString()
            }
        })


        profileImage = fragment_driver_profile_image
        profileName = fragment_driver_proflie_name

        //Load User Information
        loadUserInformation()

        // Launch Create DriverDestination Popup fragment
        launchButton.setOnClickListener {
            newPostDialog = CreateDestinationsPopUpFragment()
            newPostDialog.show(activity?.supportFragmentManager!!, "Add Dialog")
        }

        //Navigate to Current Passengers Fragment
        fragment_driver_profile_check_orders_btn.setOnClickListener {
            val action =
                DriverProfileDirections.actionDriverProfileToCurrentPassengers(destinationId!!)
            findNavController().navigate(action)
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


        enableNoDestinationMode()
    }

    private fun enableNoDestinationMode() {
        //If no active trip, show launch button, and show No active until a trip is active
        driverDetailBox.hide()
        launchButton.show()
        tripStatus.setTextColor(resources.getColor(R.color.colorCancelButton))
        tripStatus.text = "No Active Trip"
    }

    private fun getHomePageDetails() {
//        TODO("Get Seats booked, available, and other details")

        //Next payout is 26th of current month. Get from root.
        //Seats Booked is from the passenger side.
        //Seats available is CurrentDestination.seats - seatsBooked
    }


    private fun loadUserInformation() {
        val user: FirebaseUser? = firebaseAuth.currentUser
        if (user != null) {
            if (user.photoUrl.toString() != null) {
                Glide.with(this)
                    .load(user.photoUrl.toString())
                    .into(profileImage)

                profileName.text = user.displayName
            }
        }
    }
}
