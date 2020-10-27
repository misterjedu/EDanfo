package com.misterjedu.edanfo.ui.main.driver.destination

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.adapters.DestinationRecyclerAdapter
import com.misterjedu.edanfo.data.DestinationData
import com.misterjedu.edanfo.utils.DummyData
import com.misterjedu.edanfo.utils.checkItem
import kotlinx.android.synthetic.main.activity_driver.*
import kotlinx.android.synthetic.main.fragment_destination_list.*

class DestinationList : Fragment(), DestinationRecyclerAdapter.OnDestinationClickListener {

    private lateinit var adapter: DestinationRecyclerAdapter
    private var destinationList = DummyData.passengerDetails()

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

        // Destination Recycler Adapter
        adapter = DestinationRecyclerAdapter(this, destinationList)
        destination_list_recyclerView.adapter = adapter
        destination_list_recyclerView.layoutManager = LinearLayoutManager(requireContext())

//        Navigate to Add Trip Fragment
        destination_list_add_btn.setOnClickListener {
            findNavController().navigate(R.id.action_destinationList_to_addNewTripFragment)
        }

//        Navigate to Driver home on Back Press
        requireActivity().onBackPressedDispatcher.addCallback {
            if (findNavController().currentDestination?.id == R.id.destinationList) {
                activity?.home_driver_bottom_navigation?.checkItem(R.id.bottom_driver_nav_home)
                findNavController().navigate(R.id.action_destinationList_to_driverProfile6)
            } else {
                findNavController().popBackStack()
            }
        }
    }

    override fun onItemClick(item: DestinationData, position: Int) {
        Toast.makeText(requireContext(), item.destination, Toast.LENGTH_SHORT).show()
    }
}
