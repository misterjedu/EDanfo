package com.misterjedu.edanfo.ui.main.driver.destination

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.data.firebasedata.Trip
import com.misterjedu.edanfo.utils.*
import com.misterjedu.edanfo.viewmodels.firebaseViewModels.DriverDestinationViewModel
import com.misterjedu.edanfo.viewmodels.firebaseViewModels.DriverTripViewModel
import kotlinx.android.synthetic.main.fragment_add_new_trip.*

class AddNewTrip : Fragment() {

    private lateinit var driverTripViewModel: DriverTripViewModel
    private lateinit var driverDestinationViewModel: DriverDestinationViewModel
    private lateinit var driverCurrentBustop: EditText
    private lateinit var driverDestination: EditText
    private lateinit var driverTripPrice: EditText
    private lateinit var addTripButton: Button

    private lateinit var destinationID: String

    private val args: AddNewTripArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Get the Destination Id
        destinationID = args.destinationId

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_trip, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        driverCurrentBustop = fragment_add_new_trip_current_bustop_et
        driverDestination = fragment_add_new_trip_final_bustop_et
        driverTripPrice = fragment_add_new_trip_price_et
        addTripButton = fragment_add_new_sub_trip_button


        driverTripViewModel = ViewModelProvider(this).get(DriverTripViewModel::class.java)
        driverDestinationViewModel =
            ViewModelProvider(this).get(DriverDestinationViewModel::class.java)

        validateFields()


        fragment_add_trip_back_arrow_iv.setOnClickListener {
            findNavController().popBackStack()
        }

        fragment_add_new_trip_from_history_tv.setOnClickListener {
            findNavController().navigate(R.id.action_addNewTripFragment_to_tripFromHistory)
        }

        //Observe what happens when a trip is added. When exception is null, trip successfuly added
        driverTripViewModel.tripResult.observe(requireActivity(), {
            if (it == null) {
                showSnackBar(addTripButton, "Successfully Added")
            } else {
                showSnackBar(addTripButton, it.message.toString())
                addTripButton.isEnabled = true
                findNavController().popBackStack()
            }
        })


        addTripButton.setOnClickListener {
            val trip = Trip(
                null,
                destinationID,
                driverCurrentBustop.text.toString(),
                driverDestination.text.toString(),
                true,
                false,
                driverTripPrice.text.toString().toInt(),
            )

            //Add A trip to Firebase
            driverTripViewModel.addDriverTrip(trip)
        }


        fetchDestinationAndTrips()
    }

    private fun fetchDestinationAndTrips() {
        driverTripViewModel.fetchDriverTrips()
        driverTripViewModel.getTripsRealTimeUpdate()
    }

    private fun validateFields() {
        //Validate Individual Fields
        fragment_add_new_trip_current_bustop_et.watchToValidate(EditField.NAME)
        fragment_add_new_trip_final_bustop_et.watchToValidate(EditField.NAME)
        fragment_add_new_trip_price_et.watchToValidate(EditField.AMOUNT)

        //Watch all Fields and Enable Button
        fragment_add_new_trip_current_bustop_et.addTextChangedListener(watcher)
        fragment_add_new_trip_final_bustop_et.addTextChangedListener(watcher)
        fragment_add_new_trip_price_et.addTextChangedListener(watcher)
    }


    private val watcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {

            fragment_add_new_sub_trip_button.isEnabled =
                !(!validateName(fragment_add_new_trip_current_bustop_et.text.toString()) or
                        !validateName(
                            fragment_add_new_trip_final_bustop_et.text.toString().trim()
                        ) or
                        !validatePrice(
                            fragment_add_new_trip_price_et.text.toString().trim()
                        ))

        }
    }
}
