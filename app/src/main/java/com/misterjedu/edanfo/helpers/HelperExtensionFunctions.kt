package com.misterjedu.edanfo.helpers

import android.app.Activity
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.misterjedu.edanfo.R
import kotlinx.android.synthetic.main.activity_driver.*


fun BottomNavigationView.checkItem(actionId: Int) {
    for (i in 0 until menu.size()) {
        val item: MenuItem = menu.getItem(i)
        item.isChecked = false
    }

    menu.findItem(actionId)?.isChecked = true
}



//fun navigateBackPress(activity: Activity, fragment: Fragment, destination : Int?){
//    if (findNavController(activity, destination!!).currentDestination?.id == destination) {
//        activity.home_driver_bottom_navigation?.checkItem(R.id.bottom_driver_nav_home)
//        findNavController(activity, destination).navigate(R.id.action_destinationList_to_driverProfile6)
//    } else {
//        findNavController(activity, destination).popBackStack()
//    }
//
//}
