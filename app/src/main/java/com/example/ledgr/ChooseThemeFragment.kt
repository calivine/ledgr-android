package com.example.ledgr

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableRow
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.choose_theme_fragment.*
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.coroutines.NonCancellable.start
import kotlinx.coroutines.delay


/**
 * A simple [Fragment] subclass.
 * Use the [ChooseThemeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChooseThemeFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.choose_theme_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("acali", "ChooseThemeFragment-onViewCreated")
        if (savedInstanceState == null) {
            ObjectAnimator.ofFloat(
                select_theme_tray,
                "translationY",
                select_theme_tray.translationY,
                1F
            ).apply {
                duration = 600
                start()
            }
        } else {
            select_theme_tray.translationY = 1F
        }

        for (i in 0 until select_theme_list.childCount) {
            val row = select_theme_list.getChildAt(i) as TableRow

            for (c in 0 until row.childCount) {
                val button = row.getChildAt(c) as Button
                button.setOnClickListener {
                    selectTheme(it)
                    requireActivity().recreate()

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        Log.i("acali", "ChooseThemeFragment-onResume")

        back.setOnClickListener {
            ObjectAnimator.ofFloat(select_theme_tray, "translationY", 1F, 3000F).apply {
                duration = 1000
                start()
            }
            suspend {
                delay(1000)
            }

            findNavController().navigate(R.id.action_close_theme_tray)
        }


    }

    override fun onPause() {
        super.onPause()

        Log.i("acali", "ChooseThemeFragment-onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("acali", "ChooseThemeFragment-onDestroy")

    }


    private fun selectTheme(v: View) {
        val sharedPref =
            activity?.getSharedPreferences(getString(R.string.api_token), Context.MODE_PRIVATE)
                ?: return
        val chosenTheme = v.contentDescription.toString()

        val themes = mapOf(
            "Secondary" to R.style.AppTheme_Secondary,
            "Ledgr" to R.style.AppTheme,
            "Red" to R.style.AppTheme_Red,
            "Yellow" to R.style.AppTheme_Yellow,
            "Purple" to R.style.AppTheme_Purple
        )
        // Save New Theme to SharedPreferences
        with(sharedPref.edit()) {
            themes[chosenTheme]?.let {
                putInt(getString(R.string.current_theme), it)
            }
            apply()
        }


        // val theme = sharedPref.getInt("Blue", R.style.AppTheme)
        themes[chosenTheme]?.let { requireActivity().setTheme(it) }
    }


}