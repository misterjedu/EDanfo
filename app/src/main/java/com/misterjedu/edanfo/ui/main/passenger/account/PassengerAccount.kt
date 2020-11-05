package com.misterjedu.edanfo.ui.main.passenger.account

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.misterjedu.edanfo.R
import kotlinx.android.synthetic.main.fragment_passenger_account.*

class PassengerAccount : Fragment() {

    private lateinit var myStatCard: LinearLayout
    private lateinit var profileSettingsCard: LinearLayout
    private lateinit var topUpWalletCard: LinearLayout
    private lateinit var changePasswordCard: LinearLayout
    private lateinit var signOutCard: LinearLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_passenger_account, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        myStatCard = passenger_account_profile_stat_card_ll
        profileSettingsCard = passenger_account_account_settings_card_ll
        topUpWalletCard = passenger_account_top_up_wallet_ll
        changePasswordCard = passenger_account_change_password_card_ll
        signOutCard = passenger_account_logout_card_ll


        myStatCard.setOnClickListener {
            findNavController().navigate(R.id.action_passengerAccount_to_passengerStatistics)
        }

        profileSettingsCard.setOnClickListener {
            findNavController().navigate(R.id.action_passengerAccount_to_passengerAccountProfile)
        }

        topUpWalletCard.setOnClickListener {
            findNavController().navigate(R.id.action_passengerAccount_to_passengerTopUpWallet)
        }

        changePasswordCard.setOnClickListener {
            findNavController().navigate(R.id.action_passengerAccount_to_changePasswordFragment)
        }

        passenger_account_logout_card_ll.setOnClickListener {
            AlertDialog.Builder(requireContext()).apply {
                setTitle("Are you sure")
                setPositiveButton("Yes") { _, _ ->
                    FirebaseAuth.getInstance().signOut()
                    requireActivity().finish()
                }
                setNegativeButton("No") { _, _ ->
                }.create().show()


            }
        }

    }
}