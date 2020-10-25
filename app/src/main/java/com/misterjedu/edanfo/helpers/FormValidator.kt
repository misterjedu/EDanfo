package com.misterjedu.edanfo.helpers

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.google.android.material.textfield.TextInputLayout
import com.misterjedu.edanfo.R
import java.util.regex.Matcher
import java.util.regex.Pattern


fun validateNumber(number: String): Boolean {
    val pattern: Pattern = Pattern.compile("(\\+?234|0)[789][01][0-9]{8}")
    val matcher: Matcher = pattern.matcher(number)
    val matchFound = matcher.matches()
    return !(number.isEmpty() || !matchFound)
}

fun validateGender(genderSelected: String): Boolean {
    return genderSelected == "Male" || genderSelected == "Female" || genderSelected == "Other"
}

fun validatePassword(password: String): Boolean {
    return password.trim().length > 6
}

fun validateRepeatPassword(password1: String, password2: String): Boolean {
    return password1.trim().isNotEmpty() && password1 == password2
}

fun validateName(name: String): Boolean {
    return name.trim().isNotEmpty()
}


fun validateEmail(email: String): Boolean {
    val pattern: Pattern =
        Pattern.compile("^([a-zA-Z0-9_\\-.]+)@([a-zA-Z0-9_\\-.]+)\\.([a-zA-Z]{2,5})\$");
    val matcher: Matcher = pattern.matcher(email);
    val matchFound = matcher.matches()

    return email.trim().isNotEmpty() && matchFound
}


/**
 * Extension Function to Watch and Validate Individual Input Fields
 */
@JvmOverloads
fun EditText.watchToValidate(
    editField: EditField,
    inputLayer: TextInputLayout,
    extraParam: EditText? = null
) {
    addTextChangedListener(object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun beforeTextChanged(
            s: CharSequence, start: Int, count: Int,
            after: Int
        ) {
        }

        override fun afterTextChanged(s: Editable) {
            when (editField) {
                EditField.NAME -> if (validateName(s.toString())) {
                    setValidate()
                } else {
                    inputLayer.error = "Name can not be empty"
                }
                EditField.EMAIL -> if (validateEmail(s.toString())) {
                    setValidate()
                } else {
                    inputLayer.error = "Invalid Email Address"
                }
                EditField.PASSWORD -> if (validatePassword(s.toString())) {
                    setValidate()
                } else {
                    inputLayer.error = "Password too Short"
                }
                EditField.REPEATPASSWORD -> if (extraParam != null) {
                    if (validateRepeatPassword(extraParam.text.toString(), s.toString())) {
                        setValidate()
                    } else {
                        inputLayer.error = "Password does not match"
                    }
                }
            }
        }

        private fun setValidate() {
            inputLayer.error = null
            inputLayer.setEndIconDrawable(R.drawable.ic_baseline_check_circle)
        }
    })

}


//Hide and Show Progress Bars
fun ProgressBar.hide(button: Button? = null) {
    visibility = View.GONE
    if (button != null) {
        button.isEnabled = true
    }
}

fun ProgressBar.show(button: Button? = null) {
    visibility = View.VISIBLE
    if (button != null) {
        button.isEnabled = false
    }
}