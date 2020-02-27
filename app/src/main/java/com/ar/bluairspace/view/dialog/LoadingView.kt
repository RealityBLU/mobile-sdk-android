package com.ar.bluairspace.view.dialog

interface LoadingView {
    fun showLoadingIndicator()
    fun hideLoadingIndicator()
    fun showError(message: String)
    fun showProgress(currentItemCount: Int, maxItemCount: Int)
}