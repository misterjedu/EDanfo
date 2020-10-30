package com.misterjedu.edanfo.viewmodels.firebaseViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.misterjedu.edanfo.data.firebasedata.DriverDestination
import com.misterjedu.edanfo.utils.DRIVER_DESTINATION

class DriverDestinationViewModel : ViewModel() {

    private val driverDestinationDatabase =
        FirebaseDatabase.getInstance().getReference(DRIVER_DESTINATION)

    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?>
        get() = _result

    // Exception when trip is deleted. If exception is added, then the adding was successful
    private val _deleteDestinationResult = MutableLiveData<Exception?>()
    val deleteDestinationResult: LiveData<Exception?>
        get() = _deleteDestinationResult


//    //Get active destination / full trip
//    private val _activeDestination = MutableLiveData<DriverDestination?>()
//    val activeDestination: LiveData<DriverDestination?>
//        get() = _activeDestination

    //New Destination
    private val _activeDestination = MutableLiveData<DriverDestination?>()
    val activeDestination: LiveData<DriverDestination?>
        get() = _activeDestination


    //  //Get active destination / full trip
    private val _completedDestination = MutableLiveData<MutableList<DriverDestination>?>()
    val completedDestination: LiveData<MutableList<DriverDestination>?>
        get() = _completedDestination


    fun addDestination(destination: DriverDestination) {
        /**
         * Get Instance of Firebase and reference to a node path.
         *Get the generated Id using the push().key
         * Save the new destination in the generated Id
         * If the Destination is safely added, the exception would be null.
         */
        destination.id = driverDestinationDatabase.push().key
        destination.id?.let { driverDestinationDatabase.child(it).setValue(destination) }
            ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    _result.value = null
                } else {
                    _result.value = it.exception
                }
            }
    }


    // Listen to events on Trips children
    private val childEventListener = object : ChildEventListener {
        override fun onCancelled(error: DatabaseError) {}
        override fun onChildMoved(snapshot: DataSnapshot, p1: String?) {}
        override fun onChildChanged(snapshot: DataSnapshot, p1: String?) {}
        override fun onChildRemoved(snapshot: DataSnapshot) {}

        override fun onChildAdded(snapshot: DataSnapshot, p1: String?) {
            val newDestination = snapshot.getValue(DriverDestination::class.java)
            if (newDestination != null) {
                newDestination.id = snapshot.key
                _activeDestination.value = newDestination
            } else {
                _activeDestination.value = null
            }

        }
    }

    //Get real time updates on trips added
    fun getDestinationRealTimeUpdate() {
        driverDestinationDatabase.addChildEventListener(childEventListener)
    }


    //Delete Cancel Destination Trip
    fun cancelDestination(destination: DriverDestination) {
        driverDestinationDatabase.child(destination.id!!).setValue(null).addOnCompleteListener {
            if (it.isSuccessful) {
                _deleteDestinationResult.value = null
            } else {
                _deleteDestinationResult.value = it.exception
            }
        }
    }


    //Get the Current/active destination trip
    fun fetchDriverDestinations() {
        driverDestinationDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (destinationSnapShot in snapshot.children) {
                        val singleDestination =
                            destinationSnapShot.getValue(DriverDestination::class.java)
                        singleDestination?.id = destinationSnapShot.key

                        if (singleDestination != null && singleDestination.isActive) {
                            _activeDestination.value = singleDestination

                        } else {
                            _activeDestination.value = null
                        }

                        if (singleDestination != null && singleDestination.isCompleted) {
                            _completedDestination.value?.add(singleDestination)
                        }

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _activeDestination.value = null
            }
        })


    }

    override fun onCleared() {
        super.onCleared()
        driverDestinationDatabase.removeEventListener(childEventListener)
    }
}