package com.misterjedu.edanfo.data.firebasedata

import java.sql.Timestamp


data class orders(
    var id: String? = null,
    var place: String? = null,
    var timestamp: Timestamp = Timestamp(System.currentTimeMillis()),
    var cancelled: Boolean = false
)