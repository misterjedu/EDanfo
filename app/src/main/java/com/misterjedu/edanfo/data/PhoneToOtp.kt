package com.misterjedu.edanfo.data

import android.os.Parcel
import android.os.Parcelable

data class PhoneToOtp(
    val fragmentName: String,
    val phoneNumber: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeString(fragmentName)
        p0?.writeString(phoneNumber)
    }

    companion object CREATOR : Parcelable.Creator<PhoneToOtp> {
        override fun createFromParcel(parcel: Parcel): PhoneToOtp {
            return PhoneToOtp(parcel)
        }

        override fun newArray(size: Int): Array<PhoneToOtp?> {
            return arrayOfNulls(size)
        }
    }

}