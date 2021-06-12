package com.example.ledgr.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.example.ledgr.LaunchActivity
import com.example.ledgr.MainActivity

import com.example.ledgr.R
import com.example.ledgr.data.LoginDataSource
import com.example.ledgr.data.model.LoggedInUser
import java.io.FileInputStream
import java.util.*

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var starterIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        starterIntent = intent;
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)
        val loading = findViewById<ProgressBar>(R.id.loading)
        val togglePassword = findViewById<ImageButton>(R.id.toggle_show_password)

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            /**
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
            **/
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }

            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })



        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString(),
                            applicationContext
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString(), applicationContext)
            }
        }

        togglePassword.setOnClickListener {
            // If input type is password, change to password visible
            if (password.inputType == 129) {
                password.inputType = 145
                togglePassword.setImageResource(R.drawable.baseline_visibility_off_24)
            }
            // If input type is password visible, change to password
            else if (password.inputType == 145) {
                password.inputType = 129
                togglePassword.setImageResource(R.drawable.baseline_visibility_24)
            }
        }
    }

    /**
     * Start main activity with logged in user.
     */
    private fun updateUiWithUser(model: LoggedInUserView) {
        val displayName = model.displayName

        Log.d("acaliupdateUIwUser", "${model.displayName} ${model.apiKey}")

        val myIntent = Intent(this, MainActivity::class.java).apply {
            this.putExtra("username", displayName)
            this.putExtra("api", model.apiKey.removeSurrounding("\""))
        }
        // Save User in sharedPreferences
        val sharedPref = getSharedPreferences(
            getString(R.string.api_token), Context.MODE_PRIVATE)

        with (sharedPref.edit()) {
            putString(getString(R.string.api_token), model.apiKey)
            apply()
        }

        startActivity(myIntent)
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
        //val myIntent = Intent(this, LaunchActivity::class.java)
        finish()
        startActivity(starterIntent)
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}