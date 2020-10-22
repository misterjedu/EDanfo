package com.misterjedu.edanfo.ui.main.driver.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.misterjedu.edanfo.R
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
            findNavController().navigate(R.id.driverStatistics)
        }
    }
}