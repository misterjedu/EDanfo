package com.misterjedu.edanfo.ui.main.driver.destination

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.adapters.DestinationRecyclerAdapter
import com.misterjedu.edanfo.data.firebasedata.DriverDestination
import com.misterjedu.edanfo.data.firebasedata.Trip
import com.misterjedu.edanfo.utils.checkItem
import com.misterjedu.edanfo.utils.showSnackBar
import com.misterjedu.edanfo.viewmodels.firebaseViewModels.DriverDestinationViewModel
import com.misterjedu.edanfo.viewmodels.firebaseViewModels.DriverTripViewModel
import kotlinx.android.synthetic.main.activity_driver.*
import kotlinx.android.synthetic.main.fragment_destination_list.*


class DestinationList : Fragment(), DestinationRecyclerAdapter.OnDestinationClickListener {

    private lateinit var driverCurrDestination: TextView
    private lateinit var driverDestination: TextView

    private lateinit var driverTripViewModel: DriverTripViewModel

    private lateinit var driverDestinationViewModel: DriverDestinationViewModel

    private var activeDestination: DriverDestination? = null

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

        driverCurrDestination = fragment_destination_home_tv
        driverDestination = fragment_destination_destination_tv


        //Get live updates and get the added feed
        driverTripViewModel.addedTrip.observe(requireActivity(), {
            if (it != null) {
                adapter.addTripToList(it)
                adapter.notifyDataSetChanged()
            }
        })

        //Observer Destination View Model
//        driverDestinationViewModel.result.observe(requireActivity(), Observer {
//            if (it == null) {
//                showSnackBar(destinationButton, "Successfully Added")
//                dismiss()
//            } else {
//                showSnackBar(destinationButton, it.message.toString())
//                destinationProgressBar.hide(destinationButton)
//            }
//        })

        //Fetch Active Destination
        driverDestinationViewModel.activeDestination.observe(viewLifecycleOwner, {
            activeDestination = it
        })

        //Set trip lists to adapter
        driverTripViewModel.activeTrips.observe(requireActivity(), {
            if (it != null) {
                adapter.setTripList(it)
                adapter.notifyDataSetChanged()
            }
        })


        // DriverDestination Recycler Adapter
        adapter = DestinationRecyclerAdapter(this)
        destination_list_recyclerView.adapter = adapter
        destination_list_recyclerView.layoutManager = LinearLayoutManager(requireContext())

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

//        Navigate to Driver home on Back Press
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
    }

    override fun onItemClick(item: Trip, position: Int) {
        //Delete from Firebase
        driverTripViewModel.deleteTrip(item)


        //Delete from adapter List
        adapter.removeTripFromList(position)
        adapter.notifyItemRemoved(position)
        showSnackBar(
            destination_list_recyclerView,
            "$item.location to $item.destination removed"
        )
    }


}
