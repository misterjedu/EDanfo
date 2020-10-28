package com.misterjedu.edanfo.ui.main.driver

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_driver.*
import javax.inject.Inject

@AndroidEntryPoint
class DriverActivity : AppCompatActivity() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver)

        // Bottom Navigation
        val bottomNavigator: BottomNavigationView = home_driver_bottom_navigation
        bottomNavigator.setOnNavigationItemSelectedListener(navListener)
    }


    //     Switch Fragment with Bottom Navigation
    private var navListener: BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            val fragment: Int = when (item.itemId) {
                R.id.bottom_driver_nav_home -> R.id.driverProfile
                R.id.bottom_driver_nav_destination -> R.id.destinationList
                R.id.bottom_driver_nav_my_account -> R.id.driverAccount
                else -> R.id.driverProfile
            }
            Navigation.findNavController(this, R.id.activity_driver_fcv).navigate(fragment)
            true
        }


    //If user is not Logged in, Go back to the Main Activity to Login
    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser == null) {
            //Start Driver Activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            //Finish Authentication Activity  here and user moves to a new Driver Activity
            finish()
        }
    }
}
