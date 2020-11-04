package com.misterjedu.edanfo.ui.main.passenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.ui.main.driver.DriverActivity
import com.misterjedu.edanfo.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_passenger.*

@AndroidEntryPoint
class PassengerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passenger)

        saveToSharedPreference(this, USERTYPE, PASSENGER)

        // Bottom Navigation
        val bottomNavigator: BottomNavigationView = passenger_bottom_navigation
        bottomNavigator.setOnNavigationItemSelectedListener(navListener)
    }


    //     Switch Fragment with Bottom Navigation
    private var navListener: BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            val fragment: Int = when (item.itemId) {
                //TODO(Change the Fragments to the Passenger Fragments)
                R.id.passenger_nav_home -> R.id.passengerHome
                R.id.passenger_nav_my_account -> R.id.passengerAccount
                R.id.passenger_nav_search -> R.id.passengerFindBus
                else -> R.id.passengerHome
            }
            Navigation.findNavController(this, R.id.activity_passenger_fcv).navigate(fragment)
            true
        }

}

