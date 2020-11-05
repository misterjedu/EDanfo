package com.misterjedu.edanfo.utils

import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import java.util.*


fun BottomNavigationView.checkItem(actionId: Int) {
    for (i in 0 until menu.size()) {
        val item: MenuItem = menu.getItem(i)
        item.isChecked = false
    }

    menu.findItem(actionId)?.isChecked = true
}


fun showSnackBar(view: View, message: String) {
    Snackbar.make(
        view,
        message,
        Snackbar.LENGTH_LONG
    ).setAction("Ok") {}.show()
}

fun swapVisibility(viewToHide: View, viewToShow: View) {
    viewToHide.visibility = View.GONE
    viewToShow.visibility = View.VISIBLE
}


fun getFormattedDate(timeStamp: String): CustomTImeFormat {

    val timeStampLength = timeStamp.length
    val second: String = timeStamp.substring(timeStampLength - 2)
    val minute: String = timeStamp.substring(timeStampLength - 5, timeStampLength - 3)
    val hour: String = timeStamp.substring(timeStampLength - 8, timeStampLength - 6)
    return CustomTImeFormat(hour.toInt(), minute.toInt(), second.toInt())

}


fun getTimeDifference(timeStarted: CustomTImeFormat, currentTIme: CustomTImeFormat): String {

    return if (currentTIme.hour > timeStarted.hour) {
        "${currentTIme.hour - timeStarted.hour} seconds ago"
    } else if (currentTIme.minute > timeStarted.minute
        && timeStarted.hour == currentTIme.hour
    ) {
        "${currentTIme.minute - timeStarted.minute} minutes ago"
    } else if (currentTIme.second > timeStarted.second
        && currentTIme.minute == timeStarted.minute
        && currentTIme.hour == timeStarted.hour
    ) {
        "${currentTIme.second - timeStarted.second} hours ago"
    } else {
        "More than a day ago"
    }
}


fun getCurrentTimeStamp(): String {
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    val hour = c.get(Calendar.HOUR_OF_DAY)
    val minute = c.get(Calendar.MINUTE)
    val second = c.get(Calendar.SECOND)

    return "$year:${"%02d".format(month)}:${"%02d".format(day)}:${"%02d".format(hour)}:${
        "%02d".format(minute)
    }:${"%02d".format(second)}"
}

data class CustomTImeFormat(
    var hour: Int,
    var minute: Int,
    var second: Int
)


//var currentTIme = getFormattedDate(getCurrentTimeStamp())
//var timeStarted = getFormattedDate("2020:10:05:19:13:20")
//
//
//println(getTimeDifference(timeStarted, currentTIme))




