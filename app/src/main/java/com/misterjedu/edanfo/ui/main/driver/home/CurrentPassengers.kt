package com.misterjedu.edanfo.ui.main.driver.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.adapters.PassengerRecyclerAdapter
import com.misterjedu.edanfo.data.PassengerData
import kotlinx.android.synthetic.main.fragment_current_passengers.*

class CurrentPassengers :
    Fragment(),
    PassengerRecyclerAdapter.OnPassengerClickListener {

    private lateinit var adapter: PassengerRecyclerAdapter
    private var passengerList = arrayListOf(
        PassengerData("Badmus Shobowale", "Mushin to Yaba", "2 mins ago"),
        PassengerData("Omo Ologo", "Shagamu to Boluwatife", "5 mins ago"),
        PassengerData("Adewale Shwab", "Lekki to Sangotedo", "5 mins ago"),
        PassengerData("Funke Duduke", "Mushin to Yaba", "10 mins ago"),
        PassengerData("Hushpuppy MoronmuboMoronmubo", "Lekki to Ikorodu", "11 mins ago"),
        PassengerData("Badmus Shobowale", "Mushin to Yaba", "13 mins ago"),
        PassengerData("Funke Duduke", "Mushin to Yaba", "15 mins ago"),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_passengers, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Passenger Recycler Adapter
        adapter = PassengerRecyclerAdapter(this, passengerList)
        current_passenger_recyler_adapter.adapter = adapter
        current_passenger_recyler_adapter.layoutManager = LinearLayoutManager(requireContext())

        fragment_current_passengers_back_arrow_iv.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onItemClick(item: PassengerData, position: Int) {
        Toast.makeText(requireContext(), item.name, Toast.LENGTH_SHORT).show()
    }
}
