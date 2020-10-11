package com.misterjedu.edanfo.ui.auth.createprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.misterjedu.edanfo.adapters.ViewPagerAdapter
import com.misterjedu.edanfo.databinding.FragmentCreateProfileBinding

class CreateProfileFragment : Fragment() {

    private var _binding: FragmentCreateProfileBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateProfileBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Set View PagerAdapter
        //TODO Use Dependency Injection
        val fragmentList: ArrayList<Fragment> = arrayListOf(
            DriverProfile(),
            PassengerProfile()
        )


        //Connect the fragment list to the view pager
        val adapter = activity?.supportFragmentManager?.let {
            ViewPagerAdapter(
                fragmentList,
                it,
                lifecycle
            )
        }

        binding.fragmentCreateProfileViewPager.adapter = adapter

        TabLayoutMediator(
            binding.fragmentCreateProfileTabLayout,
            binding.fragmentCreateProfileViewPager
        )
        { tab, position ->
            tab.text = when (position) {
                0 -> "DRIVER"
                else -> "RIDER"
            }
        }.attach()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}