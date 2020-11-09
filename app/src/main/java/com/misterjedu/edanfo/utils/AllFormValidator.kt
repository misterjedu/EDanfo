package com.misterjedu.edanfo.utils

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText


object AllFormValidator {

    private lateinit var myFieldsToWatch: MutableMap<EditText, EditField>
    private var myButtonToEnable: Button? = null
    private var myRepeatPassword: EditText? = null
    private var myViewToHide: View? = null

    fun setEditFieldsToWatch(
        fieldsToWatch: MutableMap<EditText, EditField>,
        buttonToEnable: Button? = null,
        optionalRepeatPassword: EditText? = null,
        optionalViewToHide: View? = null
    ) {
        Log.i("Validator Called", " Validation Started ")
        myFieldsToWatch = fieldsToWatch
        myButtonToEnable = buttonToEnable
        myRepeatPassword = optionalRepeatPassword
        myViewToHide = optionalViewToHide

        for (i in fieldsToWatch.keys.toList()) {
            Log.i("Validator edits passed", i.text.toString())
        }

        for (i in myFieldsToWatch.keys.toList()) {
            Log.i("Validator edits passed", i.text.toString())
        }

        watchAllMyFields()

    }

    private fun watchAllMyFields() {
        for (field in myFieldsToWatch.keys) {
            field.addTextChangedListener(globalWatcher)
        }
        println(myFieldsToWatch.keys.toList())
    }

    private val globalWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {

            val editTextArray = myFieldsToWatch.keys.toMutableList()
            val editFieldArray = myFieldsToWatch.values.toMutableList()

            val validationArray: MutableList<Boolean> = mutableListOf()

            for (i in 0 until myFieldsToWatch.entries.size) {
                when (editFieldArray[i]) {
                    EditField.AMOUNT -> {
                        if (validatePrice(editTextArray[i].text.toString())) {
                            validationArray.add(true)
                        } else {
                            validationArray.add(false)
                        }
                    }
                    EditField.NAME -> {
                        if (validateName(editTextArray[i].text.toString())) {
                            validationArray.add(true)
                        } else {
                            validationArray.add(false)
                        }
                    }
                    EditField.PHONE -> {
                        if (validateNumber(editTextArray[i].text.toString())) {
                            validationArray.add(true)
                        } else {
                            validationArray.add(false)
                        }
                    }
                    EditField.EMAIL -> {
                        if (validateEmail(editTextArray[i].text.toString())) {
                            validationArray.add(true)
                        } else {
                            validationArray.add(false)
                        }

                    }
                    EditField.PASSWORD -> {
                        if (validatePassword(editTextArray[i].text.toString())) {
                            validationArray.add(true)
                        } else {
                            validationArray.add(false)
                        }
                    }
                    EditField.PAYMENT -> {
                        if (validatePayment(editTextArray[i].text.toString())) {
                            validationArray.add(true)
                        } else {
                            validationArray.add(false)
                        }
                    }
                    EditField.REPEATPASSWORD -> {
                        if (validateRepeatPassword(
                                editTextArray[i].text.toString(),
                                myRepeatPassword?.text.toString()
                            )
                        ) {
                            validationArray.add(true)
                        } else {
                            validationArray.add(false)
                        }
                    }
                }
            }

            //Validate Button if used.
            if (myButtonToEnable != null && validationArray.contains(false)) {
                myButtonToEnable!!.isEnabled = false
            } else if (myButtonToEnable != null && !validationArray.contains(false)) {
                myButtonToEnable!!.isEnabled = true
                println("1")
            }

            println(validationArray)

            //Validate View, if used.
            if (myViewToHide != null && validationArray.contains(false)) {
                myViewToHide!!.visibility = View.GONE
            } else if (myViewToHide != null && !validationArray.contains(false)) {
                myViewToHide!!.visibility = View.VISIBLE
            }
        }


        fun clearFieldsArray() {
            myFieldsToWatch = mutableMapOf()
            myButtonToEnable = null
            myRepeatPassword = null
            myViewToHide = null
        }

    }

}