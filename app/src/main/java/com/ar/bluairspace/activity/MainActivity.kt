package com.ar.bluairspace.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.ar.bluairspace.R
import com.ar.bluairspace.fragment.MainFragment

class MainActivity : AbstractActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        if (savedInstanceState == null) inflateUi()
    }

    fun changeFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.base_content_frame, fragment)
            .addToBackStack(fragment.javaClass.simpleName)
            .commit()
        return true
    }

    private fun inflateUi() {
        val fragment: Fragment = MainFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.base_content_frame, fragment)
            .commit()
    }
}