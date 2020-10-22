package com.misterjedu.edanfo.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.misterjedu.edanfo.R
import kotlinx.android.synthetic.main.fragment_on_boarding_screen_three.*

class OnBoardingScreenThree : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_boarding_screen_three, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Glide.with(this)
            .load(R.drawable.taxi_image)
            .into(fragment_onboarding2_background_iv)
    }
}
