package com.misterjedu.edanfo.utils

import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

data class JeduFormData(
    val editText: EditText,
    val editTextInputLayout: TextInputLayout?,
    val validator: (EditText) -> Boolean,
    val errorMessage: String,
)