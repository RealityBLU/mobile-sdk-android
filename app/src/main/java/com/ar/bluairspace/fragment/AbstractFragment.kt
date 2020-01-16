package com.ar.bluairspace.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ar.bluairspace.activity.AbstractActivity

abstract class AbstractFragment : Fragment() {

    abstract val layoutId: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId, container, false)
    }

    fun showLoading() = (activity as AbstractActivity).loadingView.showLoadingIndicator()
    fun hideLoading() = (activity as AbstractActivity).loadingView.hideLoadingIndicator()
    fun showError(errorMessage: String) = (activity as AbstractActivity).loadingView.showError(errorMessage)
}