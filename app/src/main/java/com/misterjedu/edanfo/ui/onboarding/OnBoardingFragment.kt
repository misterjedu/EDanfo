package com.misterjedu.edanfo.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.adapters.ViewPagerAdapter
import com.misterjedu.edanfo.utils.ONBOARD
import com.misterjedu.edanfo.utils.loadFromSharedPreference
import com.misterjedu.edanfo.utils.saveToSharedPreference
import kotlinx.android.synthetic.main.fragment_onboarding.*

class OnBoardingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboarding, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //If user has already gone through onBoarding once, don't show again.
        // Set View PagerAdapter
        val fragmentList: ArrayList<Fragment> = arrayListOf(
            OnBoardingScreenOne(),
            OnBoardingScreenTwo(),
            OnBoardingScreenThree()
        )

        // Connect the fragment list to the view pager
        val adapter = activity?.supportFragmentManager?.let {
            ViewPagerAdapter(
                fragmentList,
                it,
                lifecycle
            )
        }

        fragment_onboarding_vp.adapter = adapter

        TabLayoutMediator(
            fragment_onboarding_tab_layout,
            fragment_onboarding_vp
        ) { _, _ ->
        }.attach()

        fragment_onboarding_getStarted_btn.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }

        saveToSharedPreference(requireActivity(), ONBOARD, "true")
    }


    //If User has already gone through OnBoarding once, Move straight to Login Fragment
    override fun onStart() {
        super.onStart()
        if (loadFromSharedPreference(requireActivity(), ONBOARD) == "true") {
            findNavController().navigate(R.id.loginFragment)
        }
    }
}
