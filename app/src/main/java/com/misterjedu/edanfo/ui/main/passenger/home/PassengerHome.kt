package com.misterjedu.edanfo.ui.main.passenger.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.misterjedu.edanfo.R
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_passenger_home.*
import javax.inject.Inject

@AndroidEntryPoint
class PassengerHome : Fragment() {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var cancelTripButton: Button
    private lateinit var profileImage: CircleImageView
    private lateinit var profileName: TextView

    private lateinit var noOnGoingTrip: View
    private lateinit var oneOnGoingTrip: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_passenger_home, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        profileImage = fragment_passenger_home_profile_image
        profileName = fragment_passenger_home_passenger_profile_name_tv
        cancelTripButton = fragment_passenger_home_cance_trip_button

        noOnGoingTrip = fragment_passenger_home_no_on_going_trip_includes
        oneOnGoingTrip = fragment_passenger_home_active_trip_card_cl


        loadUserInformation()
    }


    private fun loadUserInformation() {
        val user: FirebaseUser? = firebaseAuth.currentUser
        if (user != null) {
            if (user.photoUrl.toString() != null) {
                Glide.with(this)
                    .load(user.photoUrl.toString())
                    .into(profileImage)

                profileName.text = user.displayName
            }
        }
    }
}