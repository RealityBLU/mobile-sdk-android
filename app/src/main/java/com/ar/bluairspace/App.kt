package com.ar.bluairspace

import android.app.Application
import android.util.Log
import com.bluairspace.sdk.helper.Blu.init
import com.bluairspace.sdk.helper.callback.TaskCallback

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val sdkKey = "" //license key for Sdk
        init(this, sdkKey, object : TaskCallback {
            override fun onSuccess() {
            }

            override fun onFail(errorMessage: String) {
                Log.d("TAG", "sdk initialization fail")
            }
        })
    }
}