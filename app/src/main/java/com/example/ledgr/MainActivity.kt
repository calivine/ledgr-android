package com.example.ledgr


import android.content.Context
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var selectedFragment: Fragment
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setTheme(R.style.AppTheme_Blue)
        val sharedPref =
            getSharedPreferences(getString(R.string.api_token), Context.MODE_PRIVATE)
                ?: return
        val theme = sharedPref.getInt(getString(R.string.current_theme), R.style.AppTheme)
        setTheme(theme)
        setContentView(R.layout.activity_main)


        /**
        val api = intent.getStringExtra("api")


        val bundle = Bundle().apply {
            this.putString("api", api)
        }
        */

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.layout_frame) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = bottom_navigation
        bottomNavigationView.setupWithNavController(navController)



        /**
         * Decpreciated Navigation
         *
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.transactions__view -> {
                    selectedFragment = ViewTransactionsFragment::class.java.newInstance()
                    // val navItem = this.findNavController(item.itemId)



                }

                R.id.transaction_create -> {
                    selectedFragment = NewTransactionFragment::class.java.newInstance()

                }

                R.id.nav__home -> {
                    selectedFragment = DashboardFragment::class.java.newInstance()

                }

                R.id.account_settings -> {
                    selectedFragment = AccountFragment::class.java.newInstance()
                }

            }
            selectedFragment.arguments = bundle
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.layout_frame, selectedFragment).commit()

            true

        }

        bottom_navigation.setOnNavigationItemReselectedListener {

        }

        selectedFragment = DashboardFragment::class.java.newInstance()
        selectedFragment.arguments = bundle
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.layout_frame, selectedFragment).commit()

        */

    }




/**
    override fun onPause() {
        super.onPause()
        Log.d("acali-MainActivity", "onPause was called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("acali-MainActivity", "onStop was called")
    }

    override fun onResume() {
        super.onResume()

        Log.d("acali-MainActivity", "onResume was called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("acali-MainActivity", "onDestroy was called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("acali-MainActivity", "onRestart was called")
    }
*/


}
