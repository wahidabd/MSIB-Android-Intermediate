package com.wahidabd.dicodingstories.utils

import android.content.Context
import com.wahidabd.dicodingstories.data.model.UserModel

class MySharedPreference(context: Context) {

    companion object{
        private const val PREFS_NAME = "prefs_name"
        private const val ID = "id"
        private const val NAME = "name"
        private const val EMAIL = "emial"
        private const val TOKEN = "token"
        private const val LOGIN = "login"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(data: UserModel){
        val editor = prefs.edit()
        editor.putString(ID, data.userId)
        editor.putString(NAME, data.name)
        editor.putString(EMAIL, data.email)
        editor.putString(TOKEN, data.token)
        editor.apply()
    }

    fun getUser(): UserModel =
        UserModel(
            userId = prefs.getString(ID, "").toString(),
            name = prefs.getString(NAME, "").toString(),
            email = prefs.getString(EMAIL, "").toString(),
            token = prefs.getString(TOKEN, "").toString()
        )


    fun setLogin(value: Boolean){
        val editor = prefs.edit()
        editor.putBoolean(LOGIN, value)
        editor.apply()
    }

    fun getLogin(): Boolean =
        prefs.getBoolean(LOGIN, false)

    fun logout(){
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

}