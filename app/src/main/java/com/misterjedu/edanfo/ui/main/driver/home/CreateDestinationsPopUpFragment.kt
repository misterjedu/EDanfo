package com.misterjedu.edanfo.ui.main.driver.home

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.data.firebasedata.DriverDestination
import com.misterjedu.edanfo.utils.*
import com.misterjedu.edanfo.viewmodels.firebaseViewModels.driver.DriverDestinationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_create_destinations.view.*
import javax.inject.Inject

@AndroidEntryPoint
class CreateDestinationsPopUpFragment : AppCompatDialogFragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var driverDestinationViewModel: DriverDestinationViewModel
    private lateinit var destinationButton: Button
    private lateinit var destinationProgressBar: ProgressBar
    private lateinit var driverLocation: EditText
    private lateinit var driverDestination: EditText
    private lateinit var tripCost: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        driverDestinationViewModel.result.observe(requireActivity(), Observer {
            if (it == null) {
                showSnackBar(destinationButton, "Successfully Added")
                dismiss()
            } else {
                showSnackBar(destinationButton, it.message.toString())
                destinationProgressBar.hide(destinationButton)
            }
        })

        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        // Create an Alert Dialog
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater

        // Inflate the view
        val view: View = inflater.inflate(R.layout.fragment_create_destinations, null)

        //Initialize ViewModel
        driverDestinationViewModel =
            ViewModelProvider(this).get(DriverDestinationViewModel::class.java)


        destinationButton = view.create_destination_set_destinaton_button
        destinationProgressBar = view.create_destination_progress_bar
        driverDestination = view.create_destination_final_bustop_et
        driverLocation = view.create_destination_current_bustop_et
        tripCost = view.create_destination_seats_available_et


        validateFields()


        // Close the Dialog when cancel button clicked
        view.create_destination_close_dialog_btn.setOnClickListener {
            dialog?.dismiss()
        }


        // return dialog view
        builder.setView(view)
        return builder.create()
    }

    private fun validateFields() {
        driverLocation.watchToValidate(EditField.NAME)
        driverDestination.watchToValidate(EditField.NAME)
        tripCost.watchToValidate(EditField.NAME)

        driverLocation.addTextChangedListener(watcher)
        driverDestination.addTextChangedListener(watcher)
        tripCost.addTextChangedListener(watcher)

    }


    private val watcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            destinationButton.isEnabled =
                !(!validateName(driverLocation.text.toString()) or
                        !validateName(driverDestination.toString().trim()) or
                        !validateName(tripCost.text.toString().trim()))
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val user: FirebaseUser? = firebaseAuth.currentUser
        destinationButton.setOnClickListener {
            destinationProgressBar.show(destinationButton)
            val destination = user?.uid?.let { it1 ->
                DriverDestination(
                    null,
                    it1,
                    driverLocation.text.toString(),
                    driverDestination.text.toString(),
                    tripCost.text.toString().toInt()
                )
            }

            if (destination != null) {
                driverDestinationViewModel.addDestination(destination)
            } else {
                showSnackBar(destinationButton, "Something went Wrong")
            }

        }

    }
}
