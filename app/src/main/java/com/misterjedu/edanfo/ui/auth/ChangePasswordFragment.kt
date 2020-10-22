package com.misterjedu.edanfo.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.misterjedu.edanfo.R
import kotlinx.android.synthetic.main.fragment_change_password.*

class ChangePasswordFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Load Header Background Image
        Glide.with(this)
            .load(R.drawable.danfo_curved_bg_2)
            .into(fragment_change_password_header_img)
    }
}
