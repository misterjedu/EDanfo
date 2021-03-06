package com.misterjedu.edanfo.data.firebasedata

import com.google.firebase.database.Exclude

data class DriverDetail(
    @get:Exclude
    val id: String? = null,
    var userId: String? = null,
    var busPlateNumber: String? = null,
    var driverRating: Float? = null,
    var bankName: String? = null,
    var bankAccountNumber: Int? = null,
    var accountBalance: Int? = null,
    var amountWithdrawn: Int? = null,
    var busUniqueNumber: String? = null
)