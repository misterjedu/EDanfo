package com.misterjedu.edanfo.utils

import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.misterjedu.edanfo.R

class JeduFormValidator(builder: Builder) {

    private var fields: MutableMap<String, Boolean>
    private var fieldsToShow: MutableList<View>
    private var viewsToEnable: MutableList<View>
    private var shouldWatch: Boolean = false
    private var errorIcon: Int? = null


    init {
        this.fields = builder.fields
        this.fieldsToShow = builder.fieldsToShow
        this.viewsToEnable = builder.viewsToEnable
        this.shouldWatch = builder.shouldWatch
        this.errorIcon = builder.errorIcon
    }


    fun startValidation(){
        for (field in fields) {

        }
    }



    private val globalWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {}
    }








    class Builder {

        internal var fields: MutableMap<String, Boolean> = hashMapOf()
            private set
        internal var fieldsToShow: MutableList<View> = mutableListOf()
            private set
        internal var viewsToEnable: MutableList<View> = mutableListOf()
            private set
        internal var shouldWatch: Boolean = false
            private set
        internal var errorIcon: Int? = null


        fun addValidator(fields: HashMap<String, Boolean>): Builder {
            this.fields = fields
            return this
        }


        fun fieldsToShow(fieldsToShow: MutableList<View>): Builder {
            this.fieldsToShow = fieldsToShow
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

        fun iconError(iconResource: Int): Builder {
            this.errorIcon = iconResource
            return this
        }

        fun build(): JeduFormValidator {
            return JeduFormValidator(this)
        }
    }
}