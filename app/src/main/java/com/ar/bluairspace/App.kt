package com.ar.bluairspace

import android.app.Application
import android.util.Log
import com.bluairspace.sdk.helper.Blu
import com.bluairspace.sdk.model.callback.TaskCallback
import com.bluairspace.sdk.model.exception.BluairspaceSdkException

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val sdkKey = "" //license key for Sdk
        Blu.init(this, sdkKey, object : TaskCallback {
            override fun onSuccess() {
                Log.d("TAG", "sdk initialization success")
            }

            override fun onFail(exception: BluairspaceSdkException) {
                Log.d("TAG", "sdk initialization fail")
            }
        })
    }
}