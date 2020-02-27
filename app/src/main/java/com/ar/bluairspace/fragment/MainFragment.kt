package com.ar.bluairspace.fragment

import android.os.Bundle
import android.view.View
import com.ar.bluairspace.R
import com.ar.bluairspace.activity.AbstractActivity
import com.ar.bluairspace.activity.MainActivity
import com.ar.bluairspace.fragment.markerbased.MarkerbasedFragment
import com.ar.bluairspace.fragment.markerless.MarkerlessGroupFragment
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : AbstractFragment(), View.OnClickListener {

    override val layoutId: Int
        get() = R.layout.fragment_main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_markerbased.setOnClickListener(this)
        btn_markerless.setOnClickListener(this)
        btn_markerless_by_id.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_markerbased -> (activity as MainActivity?)!!.changeFragment(MarkerbasedFragment())
            R.id.btn_markerless -> (activity as MainActivity?)!!.changeFragment(MarkerlessGroupFragment())
            R.id.btn_markerless_by_id -> (activity as AbstractActivity).startMarkerlessById((edt_experience_id.text.toString().toInt()))
            else -> {
            }
        }
    }
}