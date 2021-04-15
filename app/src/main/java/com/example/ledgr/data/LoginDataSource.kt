package com.example.ledgr.data

import LedgrLogin
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.ledgr.data.model.LoggedInUser
import com.google.gson.JsonObject

import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String, activity: Context): Result<LoggedInUser> {


        try {

            val json = JsonObject()
            json.addProperty("email", username)
            json.addProperty("password", password)
            val ledgr = LedgrLogin(activity)
            var user = ledgr.authenticate().post(json)
            Log.i("acali", user.toString())
            user = user.getAsJsonObject("user")
            val loggedIn = LoggedInUser(user["id"].toString(), user["name"].toString(), apiKey = user["api_token"].toString())
            activity.openFileOutput("usr", Context.MODE_PRIVATE).use {
                it.write("$username:$password".toByteArray())
            }
            return Result.Success(loggedIn)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}