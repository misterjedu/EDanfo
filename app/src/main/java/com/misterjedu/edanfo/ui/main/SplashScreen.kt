package com.misterjedu.edanfo.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.misterjedu.edanfo.R

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
