package com.wahidabd.dicodingstories.view.components

import android.content.Context
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doAfterTextChanged
import com.wahidabd.dicodingstories.R

class MyEmailEdittext : AppCompatEditText {

    constructor(context: Context) : super(context) {init()}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){init()}
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr){init()}

    private fun init(){
        background = null
        hint = resources.getString(R.string.email)

        doAfterTextChanged {
            if (!Patterns.EMAIL_ADDRESS.matcher(it.toString()).matches()){
                error = resources.getString(R.string.invalid_email_address)
            }
        }

    }
}