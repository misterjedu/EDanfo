package com.misterjedu.edanfo.ui.auth.createprofile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.ui.main.DriverActivity
import com.misterjedu.edanfo.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_create_driver_profile.*

class CreateDriverProfile : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_driver_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fragment_driver_profile_btn.setOnClickListener {
            val intent = Intent(requireContext(), DriverActivity::class.java)
            startActivity(intent)
        }


    }

}