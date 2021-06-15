package com.example.ledgr

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_account.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class AccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }





    override fun onResume() {
        super.onResume()
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

    }

}