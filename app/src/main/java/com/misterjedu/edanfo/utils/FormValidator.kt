package com.misterjedu.edanfo.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.google.android.material.textfield.TextInputLayout
import com.hbb20.CountryCodePicker
import com.misterjedu.edanfo.R
import java.util.regex.Matcher
import java.util.regex.Pattern


fun validateNumber(number: String): Boolean {
    val pattern: Pattern = Pattern.compile("(\\+?2340?)[789][01][0-9]{8}")
    val matcher: Matcher = pattern.matcher(number)
    val matchFound = matcher.matches()
    return !(number.isEmpty() || !matchFound)
}

fun validateOTP(string: String): Boolean {
    return string.isNotEmpty() && string.length == 6
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
    return name.trim().length > 1
}

fun validatePrice(amount: String): Boolean {
    return if (amount.isEmpty()) {
        false
    } else {
        amount.toInt() > 50
    }
}

fun validatePayment(amount: String): Boolean {
    return if (amount.isEmpty()) {
        false
    } else {
        amount.toInt() > 1000
    }
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
    inputLayer: TextInputLayout? = null,
    extraEditText: EditText? = null,
    countryPicker: CountryCodePicker? = null
) {
    addTextChangedListener(object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun beforeTextChanged(
            s: CharSequence, start: Int, count: Int,
            after: Int
        ) {
        }

        override fun afterTextChanged(s: Editable) {
            if (s.toString().trim().isEmpty()) {
                if (inputLayer != null) {
                    error = null
                    inputLayer.error = null
                    inputLayer.endIconDrawable = null
                }
            } else {
                when (editField) {
                    EditField.NAME -> if (validateName(s.toString())) {
                        setValidate()
                    } else {
                        setInvalidate("Too Short")
                    }
                    EditField.EMAIL -> if (validateEmail(s.toString())) {
                        setValidate()
                    } else {
                        setInvalidate("Invalid Email Address")
                    }
                    EditField.PHONE -> if (validateNumber(countryPicker?.textView_selectedCountry?.text.toString() + s.toString())) {
                        setValidate()
                    } else {
                        setInvalidate("Invalid Nigerian Phone Number")
                    }
                    EditField.PASSWORD -> if (validatePassword(s.toString())
                    ) {
                        setValidate()
                    } else {
                        setInvalidate("Password too Short")
                    }
                    EditField.REPEATPASSWORD -> if (extraEditText != null) {
                        if (validateRepeatPassword(extraEditText.text.toString(), s.toString())) {
                            setValidate()
                        } else {
                            setInvalidate("Password does not match")
                        }
                    }
                    EditField.AMOUNT -> if (validatePrice(s.toString())) {
                        setValidate()
                    } else {
                        setInvalidate("Minimum is 50 naira")
                    }
                    EditField.PAYMENT -> if (validatePrice(s.toString())) {
                        setValidate()
                    } else {
                        setInvalidate("Minimum is 1000 naira")
                    }

                }
            }


        }

        //When Field is validated
        private fun setValidate() {
            if (inputLayer != null) { //Works for Material Edit text with Input layer
                inputLayer.error = null
                inputLayer.setEndIconDrawable(R.drawable.ic_baseline_check_circle)
            } else { // Works for only Edit Text
                error = null
            }
        }

        //When Field is invalidated
        private fun setInvalidate(message: String) {
            if (inputLayer != null) { //Works for Material Edit text with Input layer
                inputLayer.error = message
            } else { // Works for only Edit Text
                error = message
            }
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


fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}