package com.misterjedu.edanfo.utils

import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar


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

