package com.misterjedu.edanfo.ui.main.driver.destination

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.adapters.DestinationRecyclerAdapter
import com.misterjedu.edanfo.data.DestinationData
import kotlinx.android.synthetic.main.fragment_destination_list.*

class DestinationList : Fragment(), DestinationRecyclerAdapter.OnDestinationClickListener {

    private lateinit var adapter: DestinationRecyclerAdapter
    private var destinationList = arrayListOf(
        DestinationData("Sango", "Sagamu", 500),
        DestinationData("Egba", "Idi Iroko", 200),
        DestinationData("Ajah", "Lekki", 100),
        DestinationData("Mary Land", "Yaba", 150),
        DestinationData("Ikeja", "Isale Afa", 700)
    )

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
            findNavController().navigate(R.id.addNewTripFragment)
        }

        // override Back Pressed
//        requireActivity().onBackPressedDispatcher.addCallback {
//            findNavController().navigate(R.id.landingFragment)
//        }
    }

    override fun onItemClick(item: DestinationData, position: Int) {
        Toast.makeText(requireContext(), item.destination, Toast.LENGTH_SHORT).show()
    }
}
