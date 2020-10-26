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
import com.misterjedu.edanfo.utils.EditField
import com.misterjedu.edanfo.utils.validatePassword
import com.misterjedu.edanfo.utils.validatePayment
import com.misterjedu.edanfo.utils.watchToValidate
import kotlinx.android.synthetic.main.fragment_driver_account_change_password.*
import kotlinx.android.synthetic.main.fragment_driver_account_withdrawal.*


class DriverAccountChangePassword : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_account_change_password, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        validateFields()


        fragment_driver_account_change_password_back_arrow_iv.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    fun validateFields() {
        //Individual Field Validation
        fragment_driver_account_change_password_new_password_et.watchToValidate(EditField.PASSWORD)
        fragment_driver_account_change_password_retype_password_et.watchToValidate(EditField.PASSWORD)

        //Entire Form Validation
        fragment_driver_account_change_password_new_password_et.addTextChangedListener(watcher)
        fragment_driver_account_change_password_retype_password_et.addTextChangedListener(watcher)

    }

    private val watcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {

            fragment_driver_account_change_password_update_button.isEnabled =
                !(!validatePassword(fragment_driver_account_change_password_new_password_et.text.toString()) or
                        !validatePassword(
                            fragment_driver_account_change_password_retype_password_et.toString()
                                .trim()
                        )
                        )
        }
    }

}