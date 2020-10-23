package com.misterjedu.edanfo.ui.main.driver.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.misterjedu.edanfo.R
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

        fragment_driver_account_withdrawal_account_change_account_details_button.setOnClickListener {
            findNavController().navigate(R.id.action_driverAccountWithdrawal_to_fragmentDriverWithdrawalSettings)
        }


        fragment_driver_account_withdrawal_back_arrow_iv.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}