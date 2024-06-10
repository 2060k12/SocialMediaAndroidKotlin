package com.phoenix.socialmedia.data.repositories

import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class UploadImagesRepository {


    // Firebase auth and firebase database & storage initialization
    val auth : FirebaseAuth = Firebase.auth
    val db = Firebase.firestore
    private val storage = FirebaseStorage.getInstance()


    // Firebase CloudStorage
    fun uploadPost(postText: String, postImageUrl: String, timestamp: Date, callback: (Boolean)-> Unit ){
        val currentDateAndTime = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val currentTime = Timestamp.now().toDate()
        val newPost = hashMapOf(
            "caption" to postText,
            "imageUrl" to postImageUrl,
//            "uploadDateTimestamp" to currentDateAndTime,
            "email" to auth.currentUser?.email,
            "time" to currentTime
            )




        if (auth.currentUser != null){
            db.collection("users")
                .document(auth.currentUser?.email.toString())
                .collection("post")
                .add(newPost)
                .addOnSuccessListener {
                    callback(true)
                }
                .addOnFailureListener(){
                    callback(false)
                }
        }
    }

    fun uploadImage(image: Uri, callback: (Uri?)-> Unit) {
        var downloadUrl : Uri
        val path: String = "${auth.currentUser?.email}/post" + UUID.randomUUID() + ".png"
        val storageReference = storage.getReference(path)


        val uploadTask = storageReference.putFile(image)
            .addOnSuccessListener {
                Log.i("Success", "Done")
            }
        val uriTask = uploadTask.continueWithTask{
            storageReference.downloadUrl
                .addOnCompleteListener(){
                        task ->
                    if(task.isSuccessful){
                        downloadUrl = task.result
                        callback(downloadUrl)
                        Log.i("Url", downloadUrl.toString())
                    }
                    else{
                        Log.i("Failed", "Failed to get download link")
                        callback(null)
                    }
                }
        }

    }

    fun uploadStory(downloadUrl: Uri, timeStamp: Date) {
        if(auth.currentUser?.email!= null){
           val storyInfo = hashMapOf(
                "imageUrl" to downloadUrl,
                "timeStamp" to timeStamp,
                "email" to auth.currentUser?.email.toString()
            )

            db.collection("users")
                .document(auth.currentUser?.email.toString())
                .collection("story")
                .add(storyInfo)

        }
        }

        }



