
import android.content.Context
import android.util.Log
import com.example.ledgr.data.LedgrDataSource

class LedgrLogin(user: String="", activity: Context) : LedgrDataSource(user, activity) {
    fun authenticate() : LedgrLogin {
        this.uri = "${this.root}authenticate"
        Log.d("acaliAuth", this.uri.toString())
        return this
    }

}