package com.misterjedu.edanfo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CheckedTripViewModel : ViewModel() {

    private val _checkedTrips: MutableLiveData<Int> = MutableLiveData()
    private val checkedTrips: LiveData<Int>
        get() = _checkedTrips

    fun setCheckedTrips(num: Int) {
        _checkedTrips.value = num
    }
}
