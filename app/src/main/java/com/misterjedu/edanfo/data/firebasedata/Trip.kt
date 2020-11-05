package com.misterjedu.edanfo.data.firebasedata

import com.google.firebase.database.Exclude

data class Trip(
    @get:Exclude
    var id: String? = null,
    var destinationId: String? = null,
    var location: String? = null,
    var destination: String? = null,
    var isActive: Boolean = true,
    var isCompleted: Boolean = false,
    var isCancelled: Boolean? = false,
    var price: Int? = null,
)