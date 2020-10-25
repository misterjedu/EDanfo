package com.misterjedu.edanfo.ui.main.driver.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.utils.checkItem
import kotlinx.android.synthetic.main.activity_driver.*
import kotlinx.android.synthetic.main.fragment_driver_account.*

class DriverAccount : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_account, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        driver_account_profile_stat_card_ll.setOnClickListener {
            findNavController().navigate(R.id.action_driverAccount_to_driverStatistics)
        }

        driver_account_account_settings_card_ll.setOnClickListener {
            findNavController().navigate(R.id.action_driverAccount_to_driverAccountProfile)
        }

        driver_account_change_password_card_ll.setOnClickListener {
            findNavController().navigate(R.id.action_driverAccount_to_driverAccountChangePassword)
        }

        driver_account_withdrawal_tv.setOnClickListener {
            findNavController().navigate(R.id.action_driverAccount_to_driverAccountWithdrawal)
        }

        driver_account_withdrawal_settings_card_ll.setOnClickListener {
            findNavController().navigate(R.id.action_driverAccount_to_fragmentDriverWithdrawalSettings)
        }

//        Navigate to Driver home on Back Press
        requireActivity().onBackPressedDispatcher.addCallback {
            if (findNavController().currentDestination?.id == R.id.driverAccount
            ) {
                activity?.home_driver_bottom_navigation?.checkItem(R.id.bottom_driver_nav_home)
                findNavController().navigate(R.id.action_driverAccount_to_driverProfile4)
            } else {
                findNavController().popBackStack()
            }
        }

    }
}