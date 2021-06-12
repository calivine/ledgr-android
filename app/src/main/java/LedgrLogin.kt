
import android.content.Context
import android.util.Log
import com.example.ledgr.data.LedgrDataSource
import com.google.gson.JsonObject
import com.koushikdutta.ion.Ion

class LedgrLogin(activity: Context, token: String) : LedgrDataSource(activity, token) {
    private val _activity = activity
    fun authenticate(user: String) : LedgrLogin {
        this.uri = "${this.root}authenticate"
        Log.d("acaliAuth", this.uri.toString())
        return this
    }

    fun login(requestBody: JsonObject): JsonObject {
        return Ion.with(_activity)
            .load("POST", "https://api.ledgr.site/api/login")
            .setLogging("acali-API", Log.INFO)
            .setJsonObjectBody(requestBody)
            .asJsonObject()
            .get()
    }




}