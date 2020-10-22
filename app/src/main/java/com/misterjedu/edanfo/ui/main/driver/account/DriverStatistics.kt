package com.misterjedu.edanfo.ui.main.driver.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.misterjedu.edanfo.R
import kotlinx.android.synthetic.main.fragment_driver_statistics.*

class DriverStatistics : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_statistics, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.stat_spinner_array,
            android.R.layout.simple_spinner_item
        ).also { statAdapter ->
            // Specify the layout to use when the list of choices appears
            statAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            driver_statistics_rating_spinner.adapter = statAdapter
            driver_statistics_earning_spinner.adapter = statAdapter
            driver_statistics_booked_spinner.adapter = statAdapter
            driver_statistics_seats_cancelled_spinner.adapter = statAdapter
            driver_statistics_trips_completed_spinner.adapter = statAdapter
            driver_statistics_trips_cancelled_spinner.adapter = statAdapter
        }


        fragment_driver_statistics_back_arrow_iv.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}