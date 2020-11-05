package com.misterjedu.edanfo.viewmodels.firebaseViewModels.driver

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.misterjedu.edanfo.data.firebasedata.Trip
import com.misterjedu.edanfo.utils.DRIVER_TRIP


class DriverTripViewModel : ViewModel() {

    private val driverTrips = FirebaseDatabase.getInstance().getReference(DRIVER_TRIP)


    //Just added trip object
    private val _addedTrip = MutableLiveData<Trip?>()
    val addedTrip: LiveData<Trip?>
        get() = _addedTrip

    // Exception when trip is added. If exception is added, then the adding was successful
    private val _tripResult = MutableLiveData<Exception?>()
    val tripResult: LiveData<Exception?>
        get() = _tripResult

    // Exception when trip is deleted. If exception is added, then the adding was successful
    private val _deleteTripResult = MutableLiveData<Exception?>()
    val deleteResult: LiveData<Exception?>
        get() = _deleteTripResult


    private val _activeTrips = MutableLiveData<MutableList<Trip>>()
    val activeTrips: LiveData<MutableList<Trip>>
        get() = _activeTrips

    private val _completedTrips = MutableLiveData<MutableList<Trip>?>()
    val completedTrips: LiveData<MutableList<Trip>?>
        get() = _completedTrips


    fun addDriverTrip(trip: Trip) {
        trip.id = driverTrips.push().key
        trip.id?.let { driverTrips.child(it).setValue(trip) }
            ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    _tripResult.value = null
                } else {
                    _tripResult.value = it.exception
                }
            }
    }


    //Fetch all Driver Trips
    fun fetchDriverTrips() {
        driverTrips.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    val onGoingTripList = mutableListOf<Trip>()
                    val completedTripList = mutableListOf<Trip>()

                    for (tripSnapShot in snapshot.children) {
                        val trip = tripSnapShot.getValue(Trip::class.java)
                        trip?.id = tripSnapShot.key

                        if (trip != null && trip.isActive && !trip.isCompleted && !trip.isCancelled!!) {
                            onGoingTripList.add(trip)
                        }

                        if (trip != null && trip.isCompleted && trip.isCancelled!!) {
                            completedTripList.add(trip)
                        }
                    }

                    _activeTrips.value = onGoingTripList
                    _completedTrips.value = completedTripList

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    // Listen to events on Trips children
    private val childEventListener = object : ChildEventListener {
        override fun onCancelled(error: DatabaseError) {}
        override fun onChildMoved(snapshot: DataSnapshot, p1: String?) {}
        override fun onChildChanged(snapshot: DataSnapshot, p1: String?) {}
        override fun onChildRemoved(snapshot: DataSnapshot) {}

        override fun onChildAdded(snapshot: DataSnapshot, p1: String?) {
            val trip = snapshot.getValue(Trip::class.java)
            trip?.id = snapshot.key
            _addedTrip.value = trip
        }
    }

    //Get real time updates on trips added
    fun getTripsRealTimeUpdate() {
        driverTrips.addChildEventListener(childEventListener)
    }


    //Delete single trip from Trips
    fun deleteTrip(trip: Trip) {
        driverTrips.child(trip.id!!).setValue(null).addOnCompleteListener {
            if (it.isSuccessful) {
                _deleteTripResult.value = null
            } else {
                _deleteTripResult.value = it.exception
            }
        }
    }


    //This only Updates the trip.iscompleted to true
    fun completeTrip(trip: Trip) {
        trip.id?.let { driverTrips.child(it).setValue(trip) }
            ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    _tripResult.value = null
                } else {
                    _tripResult.value = it.exception
                }
            }
    }


    override fun onCleared() {
        super.onCleared()
        driverTrips.removeEventListener(childEventListener)
    }
}