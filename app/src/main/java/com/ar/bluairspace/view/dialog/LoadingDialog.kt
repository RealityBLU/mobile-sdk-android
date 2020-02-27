package com.ar.bluairspace.view.dialog

import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.ar.bluairspace.R
import com.bluairspace.sdk.helper.BluDataHelper

class LoadingDialog : DialogFragment() {

    private var isError = false
    private var message = ""
    private lateinit var loadingText: TextView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView
    private lateinit var loadingButton: Button

    private var isProgress = false
    private var currentItemCount = 0
    private var maxItemCount = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alertDialogBuilder = AlertDialog.Builder(activity!!)
        val view = activity!!.layoutInflater.inflate(R.layout.loading_dialog_view, null)
        initData(savedInstanceState)
        initView(view)
        alertDialogBuilder.setView(view)
        alertDialogBuilder.setNegativeButton("cancel") { _, _ ->
            BluDataHelper.cancelMarkerlessLoadingFilesTask()
            dismiss()
        }
        isCancelable = false
        return alertDialogBuilder.create()
    }

    private fun initData(savedInstanceState: Bundle?){
        arguments?.let { message = arguments!!.getString("message", "") }
        savedInstanceState?.let {
            isError = savedInstanceState.getBoolean("error", false)
            message = savedInstanceState.getString("message", "")
            isProgress = it.getBoolean("progress", false)
            currentItemCount = it.getInt("currentCount", 0)
            maxItemCount = it.getInt("maxCount", 0)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isProgress) showProgress(currentItemCount, maxItemCount)
        else (dialog as AlertDialog).getButton(DialogInterface.BUTTON_NEGATIVE).visibility = View.GONE
    }

    private fun initView(view: View){
        isCancelable = false
        loadingText = view.findViewById(R.id.loadingText)
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar)
        progressText = view.findViewById(R.id.progressText)
        progressBar = view.findViewById(R.id.progressBar)
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

    fun showProgress(currentItemCount: Int, maxItemCount: Int) {
        isProgress = true
        if (currentItemCount == maxItemCount) isProgress = false
        this.currentItemCount = currentItemCount
        this.maxItemCount = maxItemCount
        if (progressBar.visibility == View.GONE) progressBar.visibility = View.VISIBLE
        if (progressText.visibility == View.GONE) progressText.visibility = View.VISIBLE
        (dialog as AlertDialog).getButton(DialogInterface.BUTTON_NEGATIVE).visibility = View.VISIBLE
        if (progressBar.max != maxItemCount) progressBar.max = maxItemCount * 100000
        progressText.text = getString(R.string.loading_models_hint, currentItemCount, maxItemCount)
        val animation = ObjectAnimator.ofInt(progressBar, "progress", progressBar.progress, currentItemCount * 100000)
        animation.duration = 300
        animation.interpolator = DecelerateInterpolator()
        animation.start()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("error", isError)
        outState.putString("message", message)
        outState.putBoolean("progress", isProgress)
        outState.putInt("currentCount", currentItemCount)
        outState.putInt("maxCount", maxItemCount)
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
                    Handler().post { dialog.showError(message) }
                }

                override fun showProgress(currentItemCount: Int, maxItemCount: Int) {
                    Handler().post { dialog.showProgress(currentItemCount, maxItemCount) }
                }
            }
        }
    }
}