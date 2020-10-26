package com.misterjedu.edanfo.ui.main.driver.account

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.utils.*
import kotlinx.android.synthetic.main.fragment_driver_account_profile.*
import kotlinx.android.synthetic.main.fragment_login.*

class DriverAccountProfile : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_account_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        validateFields()

        fragment_driver_account_profile_update_button.setOnClickListener {
            //TODO
        }

        fragment_driver_account_profile_back_arrow_iv.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun validateFields() {
        //Individual Validation
        fragment_driver_account_profile_first_name_et.watchToValidate(EditField.NAME)
        fragment_driver_account_profile_last_name_et.watchToValidate(EditField.NAME)
        fragment_driver_account_profile_email_et.watchToValidate(EditField.EMAIL)
        fragment_driver_account_profile_phone_number_et.watchToValidate(EditField.PHONE)


        //Entire Fields Validation
        fragment_driver_account_profile_first_name_et.addTextChangedListener(watcher)
        fragment_driver_account_profile_last_name_et.addTextChangedListener(watcher)
        fragment_driver_account_profile_email_et.addTextChangedListener(watcher)
        fragment_driver_account_profile_phone_number_et.addTextChangedListener(watcher)

    }


    private val watcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {

            fragment_driver_account_profile_update_button.isEnabled =
                !(!validateName(fragment_driver_account_profile_first_name_et.text.toString()) or
                        !validateName(
                            fragment_driver_account_profile_last_name_et.text.toString().trim()
                        ) or
                        !validateEmail(
                            fragment_driver_account_profile_email_et.toString().trim()
                        ) or

                        !validateNumber(
                            fragment_driver_account_profile_phone_number_et.text.toString().trim()
                        )
                        )
        }
    }
}