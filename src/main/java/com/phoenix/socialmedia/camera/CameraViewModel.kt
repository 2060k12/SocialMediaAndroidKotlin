package com.phoenix.socialmedia.camera

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel

class CameraViewModel: ViewModel() {



    companion object{
     const val TAG = "CameraXApp"
     const val FileName_Format = "yyyy-MM-dd-HH-mm-ss-SSS"
    val REQUIRED_PERMISSION =
        mutableListOf(
            android.Manifest.permission.CAMERA
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
                add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }

        }
            .toTypedArray()

    fun hasPermission(context: Context) = REQUIRED_PERMISSION.all {
        ContextCompat.checkSelfPermission(context,it) == PackageManager.PERMISSION_GRANTED
    }
}


}