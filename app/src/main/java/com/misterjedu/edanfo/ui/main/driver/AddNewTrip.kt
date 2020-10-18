package com.misterjedu.edanfo.ui.main.driver

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.misterjedu.edanfo.R
import kotlinx.android.synthetic.main.fragment_add_new_trip.*

class AddNewTrip : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_trip, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fragment_add_trip_back_arrow_iv.setOnClickListener {
            findNavController().popBackStack()
        }

        fragment_add_new_trip_from_history_tv.setOnClickListener {
            findNavController().navigate(R.id.tripFromHistory)
        }


    }


}