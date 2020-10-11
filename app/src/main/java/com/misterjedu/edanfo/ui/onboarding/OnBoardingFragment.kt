package com.misterjedu.edanfo.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.adapters.ViewPagerAdapter
import com.misterjedu.edanfo.databinding.FragmentCreateProfileBinding
import com.misterjedu.edanfo.databinding.FragmentOnboardingBinding
import kotlinx.android.synthetic.main.fragment_onboarding.*

class OnBoardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Set View PagerAdapter
        //TODO Use Dependency Injection
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

        binding.fragmentOnboardingVp.adapter = adapter

        TabLayoutMediator(fragment_onboarding_tab_layout, fragment_onboarding_vp)
        { _, _ -> }.attach()

    }
}