package com.misterjedu.edanfo.data.firebasedata

import com.google.firebase.database.Exclude

data class DriverDetail(
    @get:Exclude
    val id: String? = null,
    var phoneNumber: String? = null,
    var email: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
)