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
import kotlinx.android.synthetic.main.fragment_driver_account_withdrawal.*

class DriverAccountWithdrawal : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_account_withdrawal, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        validateFields()

        fragment_driver_account_withdrawal_account_change_account_details_button.setOnClickListener {
            findNavController().navigate(R.id.action_driverAccountWithdrawal_to_fragmentDriverWithdrawalSettings)
        }

        fragment_driver_account_withdrawal_back_arrow_iv.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun validateFields() {

        //Individual Validation
        fragment_driver_account_withdrawal_amount_et.watchToValidate(EditField.PAYMENT)
        fragment_driver_account_withdrawal_password_et.watchToValidate(EditField.PASSWORD)

        //Form Fields Validation
        fragment_driver_account_withdrawal_amount_et.addTextChangedListener(watcher)
        fragment_driver_account_withdrawal_password_et.addTextChangedListener(watcher)


    }

    private val watcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {

            fragment_driver_account_withdrawal_button.isEnabled =
                !(!validatePayment(fragment_driver_account_withdrawal_amount_et.text.toString()) or
                        !validatePassword(
                            fragment_driver_account_withdrawal_password_et.toString().trim()
                        )
                        )
        }
    }

}