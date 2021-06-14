package com.example.ledgr

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.ledgr.ui.login.LoginActivity
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Paths
import java.util.*

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class LaunchActivity : AppCompatActivity() {
    private lateinit var fullscreenContent: TextView
    // private lateinit var fullscreenContentControls: LinearLayout
    private val hideHandler = Handler()
    private val launchHandler = Handler()



    @SuppressLint("InlinedApi")
    private val hidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        fullscreenContent.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
    private val showPart2Runnable = Runnable {
        // Delayed display of UI elements
        supportActionBar?.show()
        // fullscreenContentControls.visibility = View.VISIBLE
    }
    private var isFullscreen: Boolean = false

    private val hideRunnable = Runnable { hide() }

    private val launchRunnable = Runnable { launch() }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private val delayHideTouchListener = View.OnTouchListener { view, motionEvent ->
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS)
            }
            MotionEvent.ACTION_UP -> view.performClick()
            else -> {
            }
        }
        false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_launch)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Log.d("acali-LaunchActivity", "onCreate was called")

        isFullscreen = true

        // Set up the user interaction to manually show or hide the system UI.
        fullscreenContent = findViewById(R.id.fullscreen_content)
        fullscreenContent.setOnClickListener { toggle() }

        //fullscreenContentControls = findViewById(R.id.fullscreen_content_controls)

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //findViewById<Button>(R.id.dummy_button).setOnTouchListener(delayHideTouchListener)





    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.

    }

    private fun launch()
    {

        if (checkForFile("usr")) {

            val user = Scanner(openFileInput("usr"))

            val userCreds = user.nextLine().toString().split(':')
            val username = userCreds[0]
            val apiKey = userCreds[2].removeSurrounding('"'.toString())

            /**
            val sharedPref = getSharedPreferences(
                getString(R.string.api_token), Context.MODE_PRIVATE)

            with (sharedPref.edit()) {
                putString(getString(R.string.api_token), apiKey)
                apply()
            }
            */

            val myIntent = Intent(this, MainActivity::class.java).apply {
                this.putExtra("username", username)
                this.putExtra("api", apiKey)
            }



            fullscreenContent
            ObjectAnimator.ofFloat(fullscreenContent, "textSize", 16F, 0F).apply {
                duration = 1000
                interpolator = AnticipateInterpolator()
                start()
            }
            /**
             * Depreciated
            myIntent.putExtra("username", username)
            myIntent.putExtra("api", apiKey)
            */

            // startActivity(myIntent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            startActivity(myIntent)
        }
        else {
            Log.d("acaliLAUNCH", "no file")

            val myIntent = Intent(this, LoginActivity::class.java)
            startActivity(myIntent)
        }


        /*
        try {
            Log.d("acaliLAUNCH", "file")
            val user = Scanner(openFileInput("usrrr"))
        }
        catch(e: FileNotFoundException) {
            Log.d("acaliLAUNCH", "no file")
            val myIntent = Intent(this, LoginActivity::class.java)
            startActivity(myIntent)
        }

        val myIntent = Intent(this, MainActivity::class.java)
        myIntent.putExtra("username", "acali")
        myIntent.putExtra("api", "LHWmiGNoaVbrgYTv7qETIVpoNkJ8H9IB1Y3Ze72voXY5Oei8Pyl7gp2Apfpw")

        startActivity(myIntent)

         */
    }


    private fun toggle() {
        if (isFullscreen) {
            //hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        supportActionBar?.hide()
        // fullscreenContentControls.visibility = View.GONE
        isFullscreen = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        hideHandler.removeCallbacks(showPart2Runnable)
        hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        // Show the system bar
        fullscreenContent.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        isFullscreen = true

        // Schedule a runnable to display UI elements after a delay
        hideHandler.removeCallbacks(hidePart2Runnable)
        hideHandler.postDelayed(showPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(hideRunnable, delayMillis.toLong())
    }

    /**
     * Returns true if file exists
     */
    private fun checkForFile(usr: String?): Boolean
    {
        val file = File(getFilesDir().getAbsolutePath(), usr)
        Log.d("acali:checkForFile", file.absolutePath.toString())
        Log.d("acali:checkForFile", file.isFile.toString())
        return file.exists()
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("acali-LaunchActivity", "OnRestart was called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("acali-LaunchActivity", "OnResume was called")
        // launch()
        delayedHide(100)

        launchHandler.postDelayed(launchRunnable, 1000)
    }

    override fun onPause() {
        super.onPause()
        Log.d("acali-LaunchActivity", "onPause was called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("acali-LaunchActivity", "onStop was called")
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d("acali-LaunchActivity", "onDestroy was called")
    }


    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private const val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private const val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private const val UI_ANIMATION_DELAY = 300
    }
}