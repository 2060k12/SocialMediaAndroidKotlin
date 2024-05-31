package com.phoenix.socialmedia.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.phoenix.socialmedia.data.Post
import kotlinx.coroutines.DelicateCoroutinesApi

class HomePageRepository {

    // initializing firestore database  & auth from firebase
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    // using profile repository to get realtime following data
    private val profileRepository = ProfileRepository()
    val followingListOfCurrentUser = profileRepository.followings


    // live data for all posts
    private val _posts = MutableLiveData<ArrayList<Post>>()
    val post: LiveData<ArrayList<Post>> get() = _posts


    // function to get all posts
    @OptIn(DelicateCoroutinesApi::class)
    fun getAllPosts() {
        val postList = ArrayList<Post>()
        val currentUserEmail = auth.currentUser?.email.toString()

        db.collectionGroup("post")
            .get()
            .addOnSuccessListener { postResult ->
                db.collection("users")
                    .document(currentUserEmail)
                    .collection("following")
                    .get()
                    .addOnSuccessListener { followingResult ->
                        val followingEmails = followingResult.documents.map { it.id }

                        for (document in postResult) {
                            val post = document.toObject(Post::class.java)
                            if (post.email.toString() in followingEmails) {
                                postList.add(post)
                            }
                        }

                        // Update the LiveData once all posts have been processed
                        _posts.value = postList
                    }
                    .addOnFailureListener { e ->
                        Log.i("Error Fetching Following", e.toString())
                    }
            }
            .addOnFailureListener { e ->
                Log.i("Error Fetching Posts", e.toString())
            }
    }

}




