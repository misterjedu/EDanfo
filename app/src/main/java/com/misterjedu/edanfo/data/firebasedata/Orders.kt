package com.misterjedu.edanfo.data.firebasedata

import com.google.firebase.database.Exclude

data class Orders(
    @get: Exclude
    var id: String? = null,
    var destinationId: String? = null,
    var passengerName: String? = null,
    var trip: String? = null,
    var timestamp: String? = null,
    var isCancelled: Boolean? = false,
    var isCompleted: Boolean? = false,
    var isPaid: Boolean? = false,
)