package com.misterjedu.edanfo.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.utils.*
import kotlinx.android.synthetic.main.fragment_change_password.*
import kotlinx.android.synthetic.main.fragment_login.*

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


        validateFields()
    }

    private fun validateFields() {

        val fields: MutableList<JeduFormData> = mutableListOf(
            JeduFormData(
                editText = fragment_change_password_new_password_et,
                editTextInputLayout = fragment_login_email_text_layout_tl,
                errorMessage = JeduErrorMessageConstants.INVALID_EMAIL_ERROR,
                validator = { it.validateEmail(it.text.toString()) }

            ),
            JeduFormData(
                editText = fragment_change_password_confirm_password_et,
                editTextInputLayout = fragment_login_password_til,
                errorMessage = JeduErrorMessageConstants.INVALID_PASSWORD_ERROR,
                validator = { it.validatePassword(it.text.toString()) }
            )
        )


        JeduFormValidator.Builder()
            .addFieldsToValidate(fields)
            .watchWhileTyping(true)
            .setErrorIcon(R.drawable.ic_baseline_check_circle)
            .shouldShowErrorIcon(true)
            .build()

    }
}
