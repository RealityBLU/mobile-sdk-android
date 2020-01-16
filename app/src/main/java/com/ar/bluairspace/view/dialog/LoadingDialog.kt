package com.ar.bluairspace.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.ar.bluairspace.R

class LoadingDialog : DialogFragment() {

    private var isError = false
    private var message = ""
    private lateinit var loadingText: TextView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var loadingButton: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alertDialogBuilder = AlertDialog.Builder(activity!!)
        val view = activity!!.layoutInflater.inflate(R.layout.loading_dialog_view, null)
        initData(savedInstanceState)
        initView(view)
        alertDialogBuilder.setView(view)
        return alertDialogBuilder.create()
    }

    private fun initData(savedInstanceState: Bundle?){
        arguments?.let { message = arguments!!.getString("message", "") }
        savedInstanceState?.let {
            isError = savedInstanceState.getBoolean("error", false)
            message = savedInstanceState.getString("message", "")
        }
    }

    private fun initView(view: View){
        isCancelable = false
        loadingText = view.findViewById(R.id.loadingText)
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar)
        loadingButton = view.findViewById(R.id.loadingButton)
        loadingText.text = message
        if (isError) showError(message)
    }

    fun showError(message: String) {
        isError = true
        this.message = message
        loadingText.text = message
        loadingProgressBar.visibility = View.GONE
        loadingButton.visibility = View.VISIBLE
        loadingButton.setOnClickListener { dismiss() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("error", isError)
        outState.putString("message", message)
    }

    companion object{
        fun view(fm: FragmentManager): LoadingView {
            return object : LoadingView {
                lateinit var dialog: LoadingDialog

                override fun showLoadingIndicator() {
                    dialog = LoadingDialog()
                    dialog.show(fm, LoadingDialog::class.java.name)
                }

                override fun hideLoadingIndicator() {
                    Handler().post { dialog.dismissAllowingStateLoss() }
                }

                override fun showError(message: String) {
                    Handler().post {
                        dialog.showError(message) }
                }
            }
        }
    }
}