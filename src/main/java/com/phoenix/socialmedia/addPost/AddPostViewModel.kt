package com.phoenix.socialmedia.addPost

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import java.util.Date

class AddPostViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val auth :FirebaseAuth = Firebase.auth
    val db = Firebase.firestore

    fun uploadPost(postText: String, postImageUrl: String, timestamp: Date, callback: (Boolean)-> Unit ){
        val newPost = hashMapOf(
            "caption" to postText,
            "imageUrl" to postImageUrl,
            "uploadDateTimestamp" to timestamp.toString()
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


}