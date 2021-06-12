package com.example.ledgr.data

import LedgrLogin
import android.content.Context
import android.util.Log
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
            json.addProperty("username", username)
            json.addProperty("password", password)

            val ledgr = LedgrLogin(activity, "")
            var user = ledgr.login(json)
            Log.i("acali", user.toString())
            user = user.getAsJsonObject("data")

            val loggedIn = LoggedInUser(user["name"].toString(), apiKey = user["api_token"].toString().removeSurrounding("\""))

            activity.openFileOutput("usr", Context.MODE_PRIVATE).use {
                it.write("$username:$password:${loggedIn.apiKey.removeSurrounding("\"")}".toByteArray())
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