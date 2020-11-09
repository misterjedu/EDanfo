package com.misterjedu.edanfo.viewmodels.firebaseViewModels.passenger

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.misterjedu.edanfo.data.firebasedata.Trip
import com.misterjedu.edanfo.utils.DRIVER_TRIP

class PassengerTripViewModel : ViewModel() {
    private val driverTrips = FirebaseDatabase.getInstance().getReference(DRIVER_TRIP)

    private val _activeTrips = MutableLiveData<MutableList<Trip>>()
    val activeTrips: LiveData<MutableList<Trip>>
        get() = _activeTrips

    private val _tripError = MutableLiveData<String>()
    val tripError: LiveData<String>
        get() = _tripError


    //Fetch all Driver Trips
    fun searchDriverTrips(location: String, destination: String) {
        driverTrips.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    val onGoingTripList = mutableListOf<Trip>()
                    for (tripSnapShot in snapshot.children) {
                        val trip = tripSnapShot.getValue(Trip::class.java)
                        trip?.id = tripSnapShot.key

                        if ((trip != null && trip.isActive &&
                                    !trip.isCompleted &&
                                    !trip.isCancelled!!)
                            &&
                            (trip.destination == destination ||
                                    trip.location == location)
                        ) {
                            onGoingTripList.add(trip)
                        }
                    }

                    _activeTrips.value = onGoingTripList

                } else {
                    _activeTrips.value = mutableListOf()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _tripError.value = error.message
            }

        })
    }
}