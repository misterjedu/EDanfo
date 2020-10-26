package com.misterjedu.edanfo.ui.main.driver.destination

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.utils.*
import kotlinx.android.synthetic.main.fragment_add_new_trip.*
import kotlinx.android.synthetic.main.fragment_login.*

class AddNewTrip : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_trip, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        validateFields()

        fragment_add_trip_back_arrow_iv.setOnClickListener {
            findNavController().popBackStack()
        }

        fragment_add_new_trip_from_history_tv.setOnClickListener {
            findNavController().navigate(R.id.action_addNewTripFragment_to_tripFromHistory)
        }
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
