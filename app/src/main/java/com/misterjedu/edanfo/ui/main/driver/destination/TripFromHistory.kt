package com.misterjedu.edanfo.ui.main.driver.destination

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.util.keyIterator
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.adapters.TripHistoryRecyclerAdapter
import com.misterjedu.edanfo.data.HistoryData
import com.misterjedu.edanfo.helpers.DummyData
import kotlinx.android.synthetic.main.fragment_trip_from_history.*

class TripFromHistory : Fragment(), TripHistoryRecyclerAdapter.OnTripClickListener {

    private lateinit var itemSelectedText: TextView
    private lateinit var addItemButton: Button
    private lateinit var adapter: TripHistoryRecyclerAdapter
    private var tripList = DummyData.tripData()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip_from_history, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Get Views to use
        itemSelectedText = fragment_trip_item_selected_tv
        addItemButton = fragment_trip_history_add_button

        // Get all Checked Items
        fragment_trip_history_add_button.setOnClickListener {
            var trips = ""
            adapter.checkBoxStateArray.keyIterator().forEach {
                if (adapter.checkBoxStateArray.get(it)) {
                    trips += "${tripList[it].trip} \n"
                }
            }
        }

        // Setting Adapter
        adapter = TripHistoryRecyclerAdapter(tripList, this)
        trip_from_history_recycler_view.adapter = adapter
        trip_from_history_recycler_view.layoutManager = LinearLayoutManager(requireContext())

        // Back Button
        fragment_trip_from_history_back_arrow_iv.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onItemClick(item: HistoryData, position: Int) {
        // DO SOMETHING LATER
    }

    // Update UI text with number of Items Clicked
    override fun onCheckboxClick(position: Int) {
        val checkedListArr = mutableListOf<Int>()
        adapter.checkBoxStateArray.keyIterator().forEach {
            if (adapter.checkBoxStateArray.get(it)) {
                checkedListArr.add(it)
            }
        }
        itemSelectedText.text = "${checkedListArr.size} items Selected"
        addItemButton.isEnabled = checkedListArr.size > 0
    }
}
