
import android.content.Context
import android.util.Log
import com.google.gson.JsonObject
import com.koushikdutta.ion.Ion

class LedgrLogin(activity: Context, user: String="") : Ledgr(activity, user) {
    fun authenticate() : LedgrLogin {
        this.uri = "${this.root}authenticate"
        Log.d("acaliAuth", this.uri.toString())
        return this
    }

}