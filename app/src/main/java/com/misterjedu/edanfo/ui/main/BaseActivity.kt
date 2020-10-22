package com.misterjedu.edanfo.ui.main

import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.misterjedu.edanfo.R

open class BaseActivity : AppCompatActivity() {

    private lateinit var baseProgressBar: ProgressBar

    /**
     * Set frameLayout as container for every activity that extends this base activity
     */
    override fun setContentView(layoutResID: Int) {
        val constraintLayout: ConstraintLayout =
            layoutInflater.inflate(R.layout.activiy_base, null) as ConstraintLayout
        val frameLayout: FrameLayout = constraintLayout.findViewById(R.id.activity_base_frame_fl)

        baseProgressBar = constraintLayout.findViewById(R.id.activity_base_progress_bar)

        layoutInflater.inflate(layoutResID, frameLayout, true)
        super.setContentView(layoutResID)
    }

    fun showProgressBar(visibility: Boolean) {
        if (visibility) {
            baseProgressBar.visibility = View.INVISIBLE
        } else {
            baseProgressBar.visibility = View.VISIBLE
        }
    }
}
