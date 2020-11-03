package com.misterjedu.edanfo.ui.main.passenger.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.misterjedu.edanfo.R
import kotlinx.android.synthetic.main.fragment_passenger_home.*

class PassengerHome : Fragment() {

    private lateinit var findBusButton: Button
    private lateinit var cancelTripButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_passenger_home, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        findBusButton = fragment_passenger_home_find_a_bus_button
        cancelTripButton = fragment_passenger_home_cance_trip_button

        findBusButton.setOnClickListener {
            findNavController().navigate(R.id.action_passengerHome_to_passengerFindBus)
        }


    }
}