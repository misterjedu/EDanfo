package com.misterjedu.edanfo.data.firebasedata

import com.google.firebase.database.Exclude


data class User(
    @get:Exclude
    val id: String? = null,
    val userId: String? = null,
    var phoneNumber: String? = null,
    var email: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var userType: String? = null
)