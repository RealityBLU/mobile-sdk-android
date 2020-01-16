package com.ar.bluairspace.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ar.bluairspace.view.dialog.LoadingDialog
import com.ar.bluairspace.view.dialog.LoadingView
import com.bluairspace.sdk.activity.wikitude.ARActivityWikitude
import com.bluairspace.sdk.helper.Blu
import com.bluairspace.sdk.helper.callback.TaskCallback
import com.bluairspace.sdk.model.MarkerBasedSettings
import com.bluairspace.sdk.model.MarkerlessExperience
import com.bluairspace.sdk.util.PermissionUtil

abstract class AbstractActivity : AppCompatActivity() {
    lateinit var loadingView: LoadingView
        private set

    private var callback = object : TaskCallback {
        override fun onSuccess() {
            loadingView.hideLoadingIndicator()
        }

        override fun onFail(errorMessage: String) {
            loadingView.showError(errorMessage)
        }
    }

    private var arType: ARActivityWikitude.Type = ARActivityWikitude.Type.CLOUDRECOGNITION
    private var list: List<MarkerlessExperience> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingView = LoadingDialog.view(supportFragmentManager)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (PermissionUtil.isPermissionsGrantedAndAsk(this, requestCode, grantResults)) {
            when(arType){
                ARActivityWikitude.Type.CLOUDRECOGNITION -> startMarkerbased(MarkerBasedSettings.defaultMarkerBasedSettings())
                ARActivityWikitude.Type.INSTANTTRACKING -> startMarkerless(list)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun checkPermissions(): Boolean {
        if (!PermissionUtil.isPermissionsGranted(this)) {
            PermissionUtil.askPermission(this)
            return false
        }
        return true
    }

    fun startMarkerbased(markerBasedSettings: MarkerBasedSettings) {
        arType = ARActivityWikitude.Type.CLOUDRECOGNITION
        if (checkPermissions()) {
            loadingView.showLoadingIndicator()
            Blu.startMarkerbased(this, callback, markerBasedSettings)
        }
    }

    fun startMarkerless(list: List<MarkerlessExperience>) {
        arType = ARActivityWikitude.Type.INSTANTTRACKING
        this.list = list
        if (checkPermissions()) {
            loadingView.showLoadingIndicator()
            Blu.startMarkerless(this, list, callback)
        }
    }

}