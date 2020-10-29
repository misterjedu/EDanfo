package com.misterjedu.edanfo.data.firebasedata

import com.google.firebase.database.Exclude

data class DriverDestination(
    @get:Exclude
    var id: String? = null,
    val driverId: String? = null,
    var currentBustop: String? = null,
    var finalBustop: String? = null,
    var seats: Int = 0,
    var isActive: Boolean = true,
    var isCompleted: Boolean = false
)