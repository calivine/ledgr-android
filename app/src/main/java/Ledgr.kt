import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.example.ledgr.NewTransactionFragment
import com.google.gson.JsonObject
import com.koushikdutta.ion.Ion
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime


open class Ledgr(private val activity: Context, user: String) {
    private val auth: String = "?api_token=${user}"
    private val routes = mapOf(
        "transactions" to "activity/transactions",
        "create" to "activity/transactions",
        "budget" to "budget/categories"
    )
    val root: String = "https://ledgr.site/api/"
    var method: String? = null
    var uri: String? = null

    init {
        Ion.getDefault(activity).conscryptMiddleware.enable(false)
    }

    fun transactions(param: String? = null): Ledgr {
        val dateString = createDateString()
        val to = "${dateString.substringBeforeLast("-")}-31"
        val from = "${dateString.substringBeforeLast("-")}-01"
        this.uri =
            if (param != null) "${this.root}${this.routes["transactions"]}/${from}/${to}/${param}${this.auth}" else "${this.root}${this.routes["transactions"]}/${from}/${to}${this.auth}"
        // this.uri = "${this.root}${this.routes["transactions"]}/${from}/${to}${this.auth}"
        return this
    }

    fun budget(param: String? = null): Ledgr {
        this.uri =
            if (param != null) "${this.root}${this.routes["budget"]}/${param}${this.auth}" else "${this.root}${this.routes["budget"]}${this.auth}"
        return this
    }

    fun newTransaction(): Ledgr {
        this.uri = "${this.root}${this.routes["create"]}${this.auth}"
        return this
    }


    private fun createDateString(): String {
        val zdt: ZonedDateTime =
            ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("America/Montreal"))
        return zdt.toString().substringBefore("T")
    }


    open fun post(json: JsonObject, callback:Unit?=null, results: MutableLiveData<Any>?=null) : JsonObject {
        this.method = "POST"
        val jsonn = Ion.with(activity)
            .load(this.method, this.uri.toString())
            .setLogging("acali-API", Log.INFO)
            .setJsonObjectBody(json)
            .asJsonObject()
            .get()
                /*
            .setCallback { ex, result ->
                if (ex != null) {
                    Log.i("acali", ex.message)
                }
                Log.i("acali", result.toString())
                callback.run { this }
                results?.postValue(result.get("user"))
            }*/

        return jsonn
    }

    open fun get(results: MutableLiveData<Any>) {
        this.method = "GET"
        Ion.with(activity)
            .load(this.method, this.uri.toString())
            .setLogging("acali-API", Log.INFO)
            .asJsonObject()
            .setCallback { ex, result ->
                if (ex != null) {
                    Log.i("acali", ex.message)
                }
                Log.i("acali", result.toString())
                results.postValue(result.get("data"))
            }


    }


}