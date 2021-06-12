package com.example.ledgr


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.ledgr.ui.account.AccountFragment
import com.example.ledgr.ui.dashboard.DashboardFragment
import com.example.ledgr.ui.newTransaction.NewTransactionFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var selectedFragment: Fragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val api = intent.getStringExtra("api")


        val bundle = Bundle().apply {
            this.putString("api", api)
        }



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
