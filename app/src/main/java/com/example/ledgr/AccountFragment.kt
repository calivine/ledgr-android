package com.example.ledgr

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.graphics.convertTo
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.color.MaterialColors
import com.google.android.material.color.MaterialColors.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_account.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.hypot

class AccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("acaliAccount_FRAGMENT", "onCreateView was called")
        return inflater.inflate(R.layout.fragment_account, container, false)
    }




    override fun onResume() {
        super.onResume()
        val sharedPref =
            activity?.getSharedPreferences(getString(R.string.api_token), Context.MODE_PRIVATE)
                ?: return

        val mode = sharedPref.getString(getString(R.string.theme_button), "Light")

        if (mode == "Dark") {
            toogle_dark_mode.isChecked = true
            toogle_dark_mode.text = getString(R.string.mode_dark)
        }
        else {
            toogle_dark_mode.text = getString(R.string.mode_light)
        }



        val packageInfo = context?.packageManager?.getPackageInfo(context?.packageName.toString(), 0)
        val versionText = if (Build.VERSION.SDK_INT >= 28)
        {
            "App Version: ${packageInfo?.versionName} (${packageInfo?.longVersionCode})"
        }
        else
        {
            "App Version: ${packageInfo?.versionName}"
        }

        version_label.text = versionText

        logout.setOnClickListener {
            // .getFilesDir().getPath()

            if (Build.VERSION.SDK_INT < 26) {
                val path = File(context?.filesDir?.absolutePath, "usr")
                // val path = File("/data/user/0/com.example.ledgr/files/usr")
                path.delete()
            }
            else {
                val user = Paths.get(File(context?.filesDir?.absolutePath, "usr").toString())


                Files.delete(user)
            }
            val myIntent = Intent(context, LaunchActivity::class.java)
            context?.startActivity(myIntent)
        }

        choose_theme.setOnClickListener {
            settings_tray_container.visibility = View.VISIBLE
            findNavController().navigate(R.id.action_account_settings_to_theme_settings)
            /*
            ObjectAnimator.ofFloat(settings_tray_container, "translationY", settings_tray_container.translationY, 1F).apply {
                duration = 1000
                start()
            }

            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.settings_tray_container, ChooseThemeFragment())
                .addToBackStack(null)
                .commit()


            val cx = choose_theme.width / 2
            val cy = choose_theme.height / 2

            val finalRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()

            val anim = ViewAnimationUtils.createCircularReveal(it, cx, cy, 0F, finalRadius)
            it.visibility = View.INVISIBLE
            theme_select_layout.visibility = View.VISIBLE
            logout.visibility = View.INVISIBLE
            version_label.visibility = View.INVISIBLE

            anim.start()
            with (sharedPref.edit()) {
                putInt("Blue", R.style.AppTheme_Blue)
                apply()
            }

            // val theme = sharedPref.getInt("Blue", R.style.AppTheme)
            // requireActivity().setTheme(theme)
            // requireContext().setTheme(R.style.AppTheme_Blue)
            requireActivity().recreate()
            //
            // ContextThemeWrapper(requireContext(), R.style.AppTheme_Blue).setTheme(R.style.AppTheme_Blue)
            // requireActivity().setTheme()


            //requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.ledgr_secondary)
            //requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.ledgr_secondary)
            // findNavController().navigate(R.id.reload)


             */



        }

        toogle_dark_mode.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                toogle_dark_mode.text = getString(R.string.mode_dark)
                with (sharedPref.edit()) {
                    putString(getString(R.string.theme_button), "Dark")
                    apply()
                }
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                toogle_dark_mode.text = getString(R.string.mode_light)
                with (sharedPref.edit()) {
                    putString(getString(R.string.theme_button), "Light")
                    apply()
                }

            }
        }

        // back.setOnClickListener { closeSettingsWindow() }
    }

    private fun closeSettingsWindow() {
        choose_theme.visibility = View.VISIBLE
        logout.visibility = View.VISIBLE
        version_label.visibility = View.VISIBLE

    }



    override fun onStart() {
        super.onStart()
        Log.d("acaliAccount_FRAGMENT", "onStart was called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("acaliAccount_FRAGMENT", "onCreate was called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("acaliAccount_FRAGMENT", "onPause was called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("acaliAccount_FRAGMENT", "onStop was called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("acaliAccount_FRAGMENT", "onDestroyView called")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("acaliAccount_FRAGMENT", "onDetach called")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d("acaliAccount_FRAGMENT", "onViewStateRestored was called")

    }

}