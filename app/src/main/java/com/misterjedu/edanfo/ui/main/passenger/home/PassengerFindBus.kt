package com.misterjedu.edanfo.ui.main.passenger.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.adapters.BusTripRecyclerAdapter
import com.misterjedu.edanfo.data.BusTrip
import com.misterjedu.edanfo.data.firebasedata.Trip
import com.misterjedu.edanfo.utils.*
import com.misterjedu.edanfo.viewmodels.firebaseViewModels.passenger.PassengerTripViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_passenger_find_bus.*
import java.util.*

class PassengerFindBus : Fragment(), BusTripRecyclerAdapter.OnBusTripClickListener {

    private lateinit var adapter: BusTripRecyclerAdapter
    private lateinit var findBusRecyclerView: RecyclerView
    private lateinit var driverTripViewModel: PassengerTripViewModel
    private var activeTripList = mutableListOf<Trip>()
    private lateinit var searchTripButton: Button
    private lateinit var currLocationEditText: EditText
    private lateinit var currLocationTil: TextInputLayout
    private lateinit var destinationTil: TextInputLayout
    private lateinit var destinationEditText: EditText
    private lateinit var noTripFoundCard: View
    private lateinit var searchProgressBar: ProgressBar

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
        searchTripButton = fragment_passenger_find_bus_search_button
        currLocationEditText = fragment_passenger_find_bus_current_location_et
        currLocationTil = fragment_passenger_find_bus_current_location_til
        destinationEditText = fragment_passenger_find_bus_destination_et
        destinationTil = fragment_passenger_find_bus_destination_til
        noTripFoundCard = fragment_passenger_find_bus_no_bus_available_includes
        searchProgressBar = fragment_passenger_find_bus_progress_bar

        validateFields()

        //Feed Recycler Adapter with Data
        adapter = BusTripRecyclerAdapter(this)
        findBusRecyclerView.adapter = adapter
        findBusRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        //Initialize Driver View model
        driverTripViewModel = ViewModelProvider(this).get(PassengerTripViewModel::class.java)


        //Make Database Query for trips
        searchTripButton.setOnClickListener {
            driverTripViewModel.searchDriverTrips(
                currLocationEditText.text.toString(),
                destinationEditText.text.toString()
            )

            destinationEditText.clearFocus()
            currLocationEditText.clearFocus()
            //Disable button and show progress bar
            searchProgressBar.show(searchTripButton)
        }

        driverTripViewModel.tripError.observe(requireActivity(), {
            if (it != null) {
                showSnackBar(searchTripButton, it)
                searchProgressBar.hide(searchTripButton)
            }
        })


        //Observe active trips from database
        driverTripViewModel.activeTrips.observe(requireActivity(), {

            //Disable button and show progress bar
            searchProgressBar.hide(searchTripButton)


            if (it != null) {
                activeTripList = it
                val busTrips = mutableListOf<BusTrip>()

                for (trip in activeTripList) {

                    val currentTIme = getFormattedDate(getCurrentTimeStamp())
                    val timeStarted = getFormattedDate(trip.time!!)
                    val time = getTimeDifference(timeStarted, currentTIme)

                    busTrips.add(
                        BusTrip(
                            trip.busUniqueNumber!!,
                            time,
                            "${trip.location!!.capitalize(Locale.ROOT)} to ${
                                trip.destination!!.capitalize(Locale.ROOT)
                            }",
                            trip.price!!
                        )
                    )
                }

                //Feed Recycler Adapter with Data
                adapter = BusTripRecyclerAdapter(this)
                findBusRecyclerView.adapter = adapter
                findBusRecyclerView.layoutManager = LinearLayoutManager(requireContext())

                adapter.addBustTripList(busTrips)
                adapter.notifyDataSetChanged()

                if (it.size == 0) {
                    noTripFoundCard.show()
                } else {
                    noTripFoundCard.hide()
                }
            } else {
                noTripFoundCard.show()
                showSnackBar(searchTripButton, "Sorry, no Bus found")
            }
        })
    }




    private fun validateFields() {

        currLocationEditText.watchToValidate(EditField.NAME, currLocationTil)
        destinationEditText.watchToValidate(EditField.NAME, destinationTil)
        currLocationEditText.addTextChangedListener(watcher)
        destinationEditText.addTextChangedListener(watcher)
    }


    private val watcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            searchTripButton.isEnabled =
                !(!validateName(currLocationEditText.text.toString()) or
                        !validateName(destinationEditText.text.toString().trim()))
        }
    }

    override fun onButtonClick(item: BusTrip, position: Int) {
        showSnackBar(findBusRecyclerView, "${item.journey} selected")
    }
}