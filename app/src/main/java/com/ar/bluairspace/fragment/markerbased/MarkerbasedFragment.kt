package com.ar.bluairspace.fragment.markerbased

import android.os.Bundle
import android.view.View
import com.ar.bluairspace.R
import com.bluairspace.sdk.model.MarkerBasedSettings
import com.ar.bluairspace.activity.AbstractActivity
import com.ar.bluairspace.activity.MainActivity
import com.ar.bluairspace.fragment.AbstractFragment
import kotlinx.android.synthetic.main.fragment_markerbased.*

class MarkerbasedFragment : AbstractFragment(), View.OnClickListener {

    override val layoutId = R.layout.fragment_markerbased

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_markers.setOnClickListener(this)
        btn_start_markerbased.setOnClickListener(this)
        btn_start_markerbased_proofing.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_markers -> (activity as MainActivity?)!!.changeFragment(MarkerFragment())
            R.id.btn_start_markerbased -> startMarkerBased()
            R.id.btn_start_markerbased_proofing -> startMarkerBasedProofing()
            else -> {
            }
        }
    }

    private fun startMarkerBased(){
        (activity as AbstractActivity).startMarkerbased()
    }

    private fun startMarkerBasedProofing(){
        (activity as AbstractActivity).startMarkerbasedProofing()
    }
}