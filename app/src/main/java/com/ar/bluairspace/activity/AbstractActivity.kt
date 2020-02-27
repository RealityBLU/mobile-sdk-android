package com.ar.bluairspace.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ar.bluairspace.view.dialog.LoadingDialog
import com.ar.bluairspace.view.dialog.LoadingView
import com.bluairspace.sdk.helper.Blu
import com.bluairspace.sdk.model.MarkerBasedSettings
import com.bluairspace.sdk.model.MarkerlessExperience
import com.bluairspace.sdk.model.callback.TaskCallback
import com.bluairspace.sdk.model.exception.BluairspaceSdkException
import com.bluairspace.sdk.util.publicutil.PermissionUtil

abstract class AbstractActivity : AppCompatActivity() {
    lateinit var loadingView: LoadingView
        private set

    private var callback = object : TaskCallback {
        override fun onSuccess() {
            loadingView.hideLoadingIndicator()
        }

        override fun onFail(exception: BluairspaceSdkException) {
            loadingView.showError(exception.toString())
        }

        override fun onProgress(currentItemCount: Int, maxItemCount: Int) {
            loadingView.showProgress(currentItemCount, maxItemCount)
        }
    }

    private var arType: Int = -1
    private var list: List<MarkerlessExperience> = ArrayList()
    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingView = LoadingDialog.view(supportFragmentManager)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (PermissionUtil.isPermissionsGrantedAndAsk(this, requestCode, grantResults)) {
            when(arType){
                1 -> startMarkerbased()
                4 -> startMarkerbasedProofing()
                2 -> startMarkerless(list)
                3 -> startMarkerlessById(id)
                else -> return
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

    fun startMarkerbased() {
        arType = 1
        if (checkPermissions()) {
            loadingView.showLoadingIndicator()
            Blu.startMarkerbased(this, callback, MarkerBasedSettings.defaultMarkerBasedSettings())
        }
    }

    fun startMarkerbasedProofing() {
        arType = 4
        if (checkPermissions()) {
            loadingView.showLoadingIndicator()
            Blu.startMarkerbased(this, callback, MarkerBasedSettings.proofingMarkerBasedSettings())
        }
    }

    fun startMarkerless(list: List<MarkerlessExperience>) {
        arType = 2
        this.list = list
        if (checkPermissions()) {
            loadingView.showLoadingIndicator()
            Blu.startMarkerless(this, list, callback)
        }
    }

    fun startMarkerlessById(id: Int){
        this.id = id
        arType = 3
        if (checkPermissions()) {
            loadingView.showLoadingIndicator()
            Blu.startMarkerlessById(this, id, callback)
        }
    }

}