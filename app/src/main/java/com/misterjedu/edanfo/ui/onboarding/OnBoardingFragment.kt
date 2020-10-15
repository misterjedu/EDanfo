package com.misterjedu.edanfo.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.adapters.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_onboarding.*

class OnBoardingFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboarding, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Set View PagerAdapter
        val fragmentList: ArrayList<Fragment> = arrayListOf(
            OnBoardingScreenOne(),
            OnBoardingScreenTwo(),
            OnBoardingScreenThree()
        )

        //Connect the fragment list to the view pager
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
        )
        { _, _ ->
        }.attach()



        fragment_onboarding_getStarted_btn.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }


}