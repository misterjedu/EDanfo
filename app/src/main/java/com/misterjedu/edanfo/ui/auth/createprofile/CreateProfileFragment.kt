package com.misterjedu.edanfo.ui.auth.createprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.adapters.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_create_profile.*

class CreateProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Glide.with(this)
            .load(R.drawable.danfo_curved_bg_2)
            .into(fragment_create_profile_header_img)

//        Glide.with(this)
//            .load(R.drawable.logo_filled_black_background)
//            .into(fragment_create_profile_picture_iv);
//
//        Glide.with(this)
//            .load(R.drawable.ic_outline_photo_camera)
//            .into(fragment_create_profile_photo_icon_iv);

        // Set View PagerAdapter
        // TODO Use Dependency Injection
        val fragmentList: ArrayList<Fragment> = arrayListOf(
            CreateDriverProfile(),
            CreatePassengerProfile()
        )

        // Connect the fragment list to the view pager
        val adapter = activity?.supportFragmentManager?.let {
            ViewPagerAdapter(
                fragmentList,
                it,
                lifecycle
            )
        }

        fragment_create_profile_view_pager.adapter = adapter

        TabLayoutMediator(
            fragment_create_profile_tab_layout,
            fragment_create_profile_view_pager
        ) { tab, position ->
            tab.text = when (position) {
                0 -> "DRIVER"
                else -> "RIDER"
            }
        }.attach()

        // Back Arrow
        fragment_create_profile_back_arrow_iv.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
