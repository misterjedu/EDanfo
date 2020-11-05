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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.data.firebasedata.DriverDestination
import com.misterjedu.edanfo.data.firebasedata.DriverDetail
import com.misterjedu.edanfo.data.firebasedata.Trip
import com.misterjedu.edanfo.data.firebasedata.User
import com.misterjedu.edanfo.utils.*
import com.misterjedu.edanfo.viewmodels.firebaseViewModels.driver.DriverDestinationViewModel
import com.misterjedu.edanfo.viewmodels.firebaseViewModels.driver.DriverTripViewModel
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
    private lateinit var noOnGoingActivity: View
    private lateinit var onGoingActivity: View
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

        //Fetch and save driver unique Id to share Preference
        fetchDriverUniqueID()


        driverDetailBox = fragment_driver_profile_trip_detail_box_cl
        launchButton = fragment_drvier_profile_launch_trip_btn
        tripStatus = fragment_driver_profile_trip_status_tv
        noOnGoingActivity = fragment_driver_profile_no_activity_going_on_includes
        onGoingActivity = fragment_driver_profile_activity_card_cl

        //Instantiate DestinationViewModel
        destinationViewModel = ViewModelProvider(this)
            .get(DriverDestinationViewModel::class.java)


        driverTripViewModel = ViewModelProvider(this)
            .get(DriverTripViewModel::class.java)


        //Start Getting Newly Destination Real time Updates and fetch all destination details
        destinationViewModel.fetchDriverDestinations(firebaseAuth.currentUser!!.uid)
        destinationViewModel.getDestinationRealTimeUpdate()
        driverTripViewModel.fetchDriverTrips()
        driverTripViewModel.getTripsRealTimeUpdate()

        swapVisibility(onGoingActivity, noOnGoingActivity)
        launchButton.show()


        //Get Current Active Destination, which is usually newly added destination
        destinationViewModel.activeDestination.observe(viewLifecycleOwner, {
            //TODO( SHOW LOADER HERE UNTIL FIREBASE SENDS RESULT )
            //Set view based on if there's a destination or not
            if (it == null) {
                swapVisibility(onGoingActivity, noOnGoingActivity)
                launchButton.show()
            } else {
                swapVisibility(noOnGoingActivity, onGoingActivity)
                launchButton.hide()
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

    }

    //Fetch and save driver bus unique id to share preference
    private fun fetchDriverUniqueID() {
        if (loadFromSharedPreference(requireActivity(), DRIVERUNIQUEID).isEmpty()) {
            FirebaseDatabase.getInstance().getReference(DRIVER)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (driverSnapShot in snapshot.children) {
                                val driver = driverSnapShot.getValue(DriverDetail::class.java)
                                driver?.userId = driverSnapShot.key
                                //Save driver unique Id to shared preference
                                if (driver != null && driver.userId == firebaseAuth.currentUser!!.uid) {
                                    saveToSharedPreference(
                                        requireActivity(), DRIVERUNIQUEID,
                                        driver.busUniqueNumber!!
                                    )
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }
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
