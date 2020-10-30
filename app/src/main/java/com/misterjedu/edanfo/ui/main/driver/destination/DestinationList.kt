package com.misterjedu.edanfo.ui.main.driver.destination

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.adapters.DestinationRecyclerAdapter
import com.misterjedu.edanfo.data.firebasedata.DriverDestination
import com.misterjedu.edanfo.data.firebasedata.Trip
import com.misterjedu.edanfo.utils.*
import com.misterjedu.edanfo.viewmodels.firebaseViewModels.DriverDestinationViewModel
import com.misterjedu.edanfo.viewmodels.firebaseViewModels.DriverTripViewModel
import kotlinx.android.synthetic.main.activity_driver.*
import kotlinx.android.synthetic.main.fragment_destination_list.*


class DestinationList : Fragment(), DestinationRecyclerAdapter.OnDestinationClickListener {

    private lateinit var driverCurrDestination: TextView
    private lateinit var driverDestination: TextView
    private lateinit var driverTripViewModel: DriverTripViewModel
    private lateinit var cancelJourneyButton: Button
    private lateinit var driverDestinationViewModel: DriverDestinationViewModel
    private var activeDestination: DriverDestination? = null
    private var activeTripList = mutableListOf<Trip>()

    private lateinit var adapter: DestinationRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_destination_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Initialize Driver View model
        driverTripViewModel = ViewModelProvider(this).get(DriverTripViewModel::class.java)
        driverDestinationViewModel =
            ViewModelProvider(this).get(DriverDestinationViewModel::class.java)


        //View Model Listener Activators
        driverDestinationViewModel.fetchDriverDestinations()
        driverDestinationViewModel.getDestinationRealTimeUpdate()
        driverTripViewModel.fetchDriverTrips()
        driverTripViewModel.getTripsRealTimeUpdate()


        driverCurrDestination = fragment_destination_home_tv
        driverDestination = fragment_destination_destination_tv
        cancelJourneyButton = destination_list_cancel_btn


        //Get live updates and get the added feed
//        driverTripViewModel.addedTrip.observe(requireActivity(), {
//            if (it != null) {
//                adapter.addTripToList(it)
//                adapter.notifyDataSetChanged()
//            }
//        })


        //Fetch Active Destination
        driverDestinationViewModel.activeDestination.observe(viewLifecycleOwner, {
            if (it != null) {
                driverCurrDestination.text = it.currentBustop
                driverDestination.text = it.finalBustop
                fragment_destination_list_trip_status.text = "One Active Journey"
                destination_list_add_btn.show()
                destination_list_cancel_btn.show()
            }
            activeDestination = it
        })


        //Set trip lists to adapter
        driverTripViewModel.activeTrips.observe(requireActivity(), {
            // DriverDestination Recycler Adapter
            adapter = DestinationRecyclerAdapter(this)
            destination_list_recyclerView.adapter = adapter
            destination_list_recyclerView.layoutManager = LinearLayoutManager(requireContext())
            if (it != null) {
                activeTripList = it
                adapter.setTripList(it)
            }
        })


        //Observe Delete Journey/Destination
        driverDestinationViewModel.deleteDestinationResult.observe(viewLifecycleOwner, {
            if (it == null) {
                enableNoDestinationMode()

                showSnackBar(cancelJourneyButton, "Journey has been Cancelled")
            } else {
                showSnackBar(cancelJourneyButton, it.message!!)
            }
        })


        // Navigate to Add Trip Fragment
        destination_list_add_btn.setOnClickListener {
            val destinationId = activeDestination?.id

            val action = destinationId?.let { it1 ->
                DestinationListDirections.actionDestinationListToAddNewTripFragment(
                    it1
                )
            }
            if (action != null) {
                findNavController().navigate(action)
            }
        }


        //Cancel Journey (Destination)
        cancelJourneyButton.setOnClickListener {
            cancelAllTrips()
            driverDestinationViewModel.cancelDestination(activeDestination!!)
        }


        // Navigate to Driver home on Back Press
        requireActivity().onBackPressedDispatcher.addCallback {
            if (findNavController().currentDestination?.id == R.id.destinationList) {
                activity?.home_driver_bottom_navigation?.checkItem(R.id.bottom_driver_nav_home)

                val action = DestinationListDirections.actionDestinationListToDriverProfile6(
                    activeDestination?.id
                )
                findNavController().navigate(action)

            } else {
                findNavController().popBackStack()
            }
        }


        enableNoDestinationMode()
    }


    private fun cancelAllTrips() {
        val driverTrips = FirebaseDatabase.getInstance().getReference(DRIVER_TRIP)
        for (trip in activeTripList) {
            trip.isCancelled = true
            trip.isActive = false
            driverTrips.child(trip.id!!).setValue(trip)
        }

        adapter.setTripList(mutableListOf())
        adapter.notifyDataSetChanged()
    }


    private fun enableNoDestinationMode() {
        //Hide Views for null
        destination_list_add_btn.hide()
        destination_list_cancel_btn.hide()
        driverCurrDestination.text = "Not Available"
        driverDestination.text = "Not Avaiable"
        fragment_destination_list_trip_status.text = "No Active Journey"
    }


    override fun onItemClick(item: Trip, position: Int) {
        //Delete from Firebase
        driverTripViewModel.deleteTrip(item)
        //Delete from adapter List
        adapter.removeTripFromList(position)
        adapter.notifyItemRemoved(position)
//
    }
}
