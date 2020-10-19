package com.misterjedu.edanfo.ui.main.driver

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.util.keyIterator
import androidx.core.util.size
import androidx.core.util.valueIterator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.adapters.TripHistoryRecyclerAdapter
import com.misterjedu.edanfo.data.HistoryData
import kotlinx.android.synthetic.main.fragment_trip_from_history.*

class TripFromHistory : Fragment(), TripHistoryRecyclerAdapter.OnTripClickListener {

    private lateinit var adapter: TripHistoryRecyclerAdapter

    private var tripList = arrayListOf(
        HistoryData(1, "Mushin to Yaba", 100),
        HistoryData(2, "Shagamu to Boluwatife", 100),
        HistoryData(3, "Lekki to Sangotedo", 100),
        HistoryData(4, "Lekki to Sangotedo", 100),
        HistoryData(5, "Lekki to Ikorodu", 100),
        HistoryData(6, "Mushin to Yaba", 100),
        HistoryData(7, "Shagamu to Boluwatife", 100),
        HistoryData(8, "Lekki to Ikorodu", 100),
        HistoryData(9, "Shagamu to Boluwatife", 100),
        HistoryData(10, "Lekki to Sangotedo", 100),
        HistoryData(11, "Lekki to Ikorodu", 100),
        HistoryData(12, "Mushin to Yaba", 100),
        HistoryData(13, "Lekki to Sangotedo", 100),
        HistoryData(14, "Lekki to Sangotedo", 100),
        HistoryData(15, "Lekki to Ikorodu", 100),
        HistoryData(16, "Mushin to Yaba", 100),
        HistoryData(17, "Shagamu to Boluwatife", 100),
        HistoryData(18, "Lekki to Ikorodu", 100),
        HistoryData(19, "Shagamu to Boluwatife", 100),
        HistoryData(20, "Lekki to Sangotedo", 100),
        HistoryData(21, "Lekki to Ikorodu", 100),
        HistoryData(21, "Mushin to Yaba", 100),
        HistoryData(22, "Shagamu to Boluwatife", 100),
        HistoryData(23, "Lekki to Sangotedo", 100),
        HistoryData(24, "Lekki to Sangotedo", 100),
        HistoryData(25, "Lekki to Ikorodu", 100),
        HistoryData(26, "Mushin to Yaba", 100),
        HistoryData(27, "Shagamu to Boluwatife", 100),
        HistoryData(28, "Lekki to Ikorodu", 100),
        HistoryData(29, "Shagamu to Boluwatife", 100),
        HistoryData(30, "Lekki to Sangotedo", 100),

        )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip_from_history, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        fragment_trip_history_add_button.setOnClickListener {
            var trips = ""
            adapter.checkBoxStateArray.keyIterator().forEach {
                if (adapter.checkBoxStateArray.get(it)) {
                    trips += "${tripList[it].trip} \n"
                }

            }

            Toast.makeText(requireContext(), trips, Toast.LENGTH_SHORT).show()
        }


        adapter = TripHistoryRecyclerAdapter(tripList, this)

        trip_from_history_recycler_view.adapter = adapter
        trip_from_history_recycler_view.layoutManager = LinearLayoutManager(requireContext())

        fragment_trip_from_history_back_arrow_iv.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onItemClick(item: HistoryData, position: Int) {
        Toast.makeText(requireContext(), item.trip, Toast.LENGTH_SHORT).show()
    }


}