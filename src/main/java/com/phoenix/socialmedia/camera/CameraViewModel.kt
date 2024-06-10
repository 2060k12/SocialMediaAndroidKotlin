package com.phoenix.socialmedia.camera

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.google.firebase.storage.FirebaseStorage
import com.phoenix.socialmedia.data.repositories.UploadImagesRepository
import java.util.Date

class CameraViewModel: ViewModel() {
    private val storage = FirebaseStorage.getInstance()
    private val uploadImagesRepository = UploadImagesRepository()



    companion object{
     const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
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

    fun uploadImage(image: Uri, callback: (Uri?)-> Unit) {
        uploadImagesRepository.uploadImage(image, callback)

    }

    fun uploadPost(postText: String, postImageUrl: String, timestamp: Date, callback: (Boolean)-> Unit ){
        uploadImagesRepository.uploadPost(postText, postImageUrl, timestamp, callback)
    }

    fun uploadStory(downloadUrl: Uri, timeStamp: Date) {
        uploadImagesRepository.uploadStory(downloadUrl, timeStamp)

    }

}