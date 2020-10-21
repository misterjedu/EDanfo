package com.misterjedu.edanfo.ui.main.driver

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.misterjedu.edanfo.R
import kotlinx.android.synthetic.main.activity_driver.*

class DriverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver)

        //Bottom Navigation
        val bottomNavigator: BottomNavigationView = home_driver_bottom_navigation
        bottomNavigator.setOnNavigationItemSelectedListener(navListener)
    }

    //Switch Fragment with Bottom Navigation
    private var navListener: BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            val fragment: Int = when (item.itemId) {
                R.id.bottom_nav_home -> R.id.driverProfile
                R.id.bottom_nav_destination -> R.id.destinationList
                else -> R.id.driverProfile
            }
            Navigation.findNavController(this, R.id.activity_driver_fcv).navigate(fragment)
            true
        }
}