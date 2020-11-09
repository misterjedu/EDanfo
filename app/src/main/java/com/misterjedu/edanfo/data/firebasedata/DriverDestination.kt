package com.misterjedu.edanfo.data.firebasedata

import com.google.firebase.database.Exclude

data class DriverDestination(
    @get:Exclude
    var id: String? = null,
    val driverId: String? = null,
    var currentBuStop: String? = null,
    var finalBuStop: String? = null,
    var seats: Int = 0,
    var isActive: Boolean = true,
    var isStarted: Boolean = false,
    var isCompleted: Boolean = false,
    var busUniqueId: String? = null,
)