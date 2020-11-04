package com.misterjedu.edanfo.data.firebasedata

import com.google.firebase.database.Exclude

data class PassengerDetail(
    @get:Exclude
    val id: String? = null,
    val userId: String? = null,
    val walletBalance: Int? = null
)