package com.misterjedu.edanfo.utils

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.test.core.app.ApplicationProvider.getApplicationContext

class JeduFormValidator(builder: Builder) {

    private var fields: MutableList<JeduFormData>
    private var viewsToBeVisible: MutableList<View>
    private var viewsToEnable: MutableList<View>
    private var shouldWatch: Boolean = false
    private var errorIcon: Int? = null
    private var shouldShowErrorIcon: Boolean = false
    var areAllFieldsValidated: Boolean = false

    init {
        this.fields = builder.fields
        this.viewsToBeVisible = builder.fieldsToBeVisible
        this.viewsToEnable = builder.viewsToEnable
        this.shouldWatch = builder.shouldWatch
        this.errorIcon = builder.errorIcon
        this.shouldShowErrorIcon = builder.showErrorIcon
        startValidation()
    }


    private fun startValidation() {
        val globalFieldWatcher: TextWatcher = object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Log.i("Field Watch", "On Tex Changed")
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                Log.i("Field Watch", "Before Text Change")
            }

            override fun afterTextChanged(s: Editable) {


                //Validate all fields and return an array of the validation status of each field
                val editTextValidationArray = fieldsValidationLogic()


                //If there are views to be enabled or made visible, this is where to do that.
                if (shouldWatch &&
                    (viewsToBeVisible.isNotEmpty() || viewsToEnable.isNotEmpty()) &&
                    editTextValidationArray.contains(false)
                ) {

                    if (viewsToEnable.isNotEmpty()) {
                        for (view in viewsToEnable) {
                            view.isEnabled = false
                        }
                    }

                    if (viewsToBeVisible.isNotEmpty()) {
                        for (view in viewsToBeVisible) {
                            view.visibility = View.GONE
                        }
                    }


                } else if ((viewsToBeVisible.isNotEmpty() || viewsToEnable.isNotEmpty()) &&
                    editTextValidationArray.contains(true)
                ) {

                    if (viewsToEnable.isNotEmpty()) {
                        for (view in viewsToEnable) {
                            view.isEnabled = true
                        }
                    }

                    if (viewsToBeVisible.isEmpty()) {
                        for (view in viewsToBeVisible) {
                            view.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        //If EditTexts fields should be watched, add a watcher to all passed Edit Fields
        if (shouldWatch) {
            for (field in fields) {
                field.editText.addTextChangedListener(globalFieldWatcher)
            }
        } else {

            validateWithoutWatching()

        }

    }


    // Validation without text watcher.
    //All fields are validated when user clicks the button and sets the areAllFieldsValidated Boolean
    private fun validateWithoutWatching() {
        //Validate all fields and return an array of the validation status of each field
        val editTextValidationArray = fieldsValidationLogic()
        areAllFieldsValidated = !editTextValidationArray.contains(false)
    }


    private fun fieldsValidationLogic(): MutableList<Boolean> {

        val editTextValidationArray: MutableList<Boolean> = mutableListOf()

        /*If there are no fields to hide/show/enable/disable, the fields are watched
        * individually.
         */
        for (field in fields) {

            //If fields are validated and if there's a TIL or not, show error message
            if (field.validator.invoke(field.editText)) {

                //Remove error icons if field is validated
                if (field.editTextInputLayout != null) { //If there's a TextInputLayout

                    field.editTextInputLayout.error = null

                    if (errorIcon != null) { //Remove Error Icon if available
                        field.editTextInputLayout.endIconDrawable = null
                    }

                } else { //If there's no TIL, use the editText
                    field.editText.error = null
                }

                //Add true to the validation array
                editTextValidationArray.add(true)

            } else if (!field.validator.invoke(field.editText)) { //If validation fails, set Icon and Error Messages

                // Add Error Icons and Text if Edit Text fields are not empty
                if (field.editTextInputLayout != null &&
                    field.editText.text.toString().trim().isNotEmpty()
                ) {
                    field.editTextInputLayout.error = field.errorMessage

                    if (errorIcon != null) { //Set Icon Drawable if it's available
                        field.editTextInputLayout.setEndIconDrawable(errorIcon!!)
                    }

                } else if (field.editTextInputLayout == null &&
                    field.editText.text.toString().trim().isNotEmpty()
                ) {
                    //Set Error on EditText if TextInputLayer is not available
                    field.editText.error = field.errorMessage
                }

                //Add false to the validation array
                editTextValidationArray.add(false)
            }
        }

        return editTextValidationArray
    }

    class Builder {

        internal var fields: MutableList<JeduFormData> = mutableListOf()
            private set
        internal var fieldsToBeVisible: MutableList<View> = mutableListOf()
            private set
        internal var viewsToEnable: MutableList<View> = mutableListOf()
            private set
        internal var shouldWatch: Boolean = false
            private set
        internal var errorIcon: Int? = null

        internal var showErrorIcon: Boolean = true
            private set

        internal var setButton: Button? = null
            private set

        fun addFieldsToValidate(fields: MutableList<JeduFormData>): Builder {
            this.fields = fields
            return this
        }

        fun fieldsToShow(fieldsToShow: MutableList<View>): Builder {
            this.fieldsToBeVisible = fieldsToShow
            return this
        }

        fun viewsToEnable(viewsToEnable: MutableList<View>): Builder {
            this.viewsToEnable = viewsToEnable
            return this
        }

        fun watchWhileTyping(shouldWatch: Boolean): Builder {
            this.shouldWatch = shouldWatch
            return this
        }

        fun shouldShowErrorIcon(shouldShowErrorIcon: Boolean): Builder {
            this.showErrorIcon = shouldShowErrorIcon
            return this
        }

        fun setErrorIcon(iconResource: Int): Builder {
            this.errorIcon = iconResource
            return this
        }


        fun build(): JeduFormValidator {
            return JeduFormValidator(this)
        }
    }
}
