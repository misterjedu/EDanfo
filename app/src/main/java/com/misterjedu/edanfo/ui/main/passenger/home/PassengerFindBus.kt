package com.misterjedu.edanfo.ui.main.passenger.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.adapters.BusTripRecyclerAdapter
import com.misterjedu.edanfo.data.BusTrip
import com.misterjedu.edanfo.utils.DummyData
import com.misterjedu.edanfo.utils.showSnackBar
import kotlinx.android.synthetic.main.fragment_passenger_find_bus.*

class PassengerFindBus : Fragment(), BusTripRecyclerAdapter.OnBusTripClickListener {


    private lateinit var adapter: BusTripRecyclerAdapter
    private var tripList = DummyData.busTripList()
    private lateinit var findBusRecyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_passenger_find_bus, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        findBusRecyclerView = fragment_passenger_find_bus_recycler_view


        //Feed Recycler Adapter with Data
        adapter = BusTripRecyclerAdapter(this, tripList)
        findBusRecyclerView.adapter = adapter
        findBusRecyclerView.layoutManager = LinearLayoutManager(requireContext())


    }


    override fun onButtonClick(item: BusTrip, position: Int) {
        showSnackBar(findBusRecyclerView, "${item.journey} selected")
    }
}