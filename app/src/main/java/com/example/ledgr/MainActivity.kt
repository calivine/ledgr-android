package com.example.ledgr


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.ledgr.ui.dashboard.DashboardFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var selectedFragment: Fragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val api = intent.getStringExtra("api")

        Log.d("acaliIntentapi", api.toString())

        val bundle = Bundle()
        bundle.putString("api", api)

        bottom_navigaiton.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.transactions__view -> {
                    selectedFragment = ViewTransactionsFragment::class.java.newInstance()


                }

                R.id.transaction__create -> {
                    selectedFragment = NewTransactionFragment::class.java.newInstance()

                }

                R.id.nav__home -> {
                    selectedFragment = DashboardFragment::class.java.newInstance()

                }

            }
            selectedFragment.arguments = bundle
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.layout_frame, selectedFragment).commit()

            true

        }
        selectedFragment = DashboardFragment::class.java.newInstance()
        selectedFragment.arguments = bundle
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.layout_frame, selectedFragment).commit()
    }

    override fun onPause() {
        super.onPause()
        Log.i("acali-MainActivity", "onPause was called")
    }

    override fun onStop() {
        super.onStop()
        Log.i("acali-MainActivity", "onStop was called")
    }

    override fun onResume() {
        super.onResume()

        Log.i("acali-MainActivity", "onResume was called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("acali-MainActivity", "onDestroy was called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("acali-MainActivity", "onRestart was called")
    }



}
