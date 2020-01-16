package com.ar.bluairspace.view.dialog

interface LoadingView {
    fun showLoadingIndicator()
    fun hideLoadingIndicator()
    fun showError(message: String)
}