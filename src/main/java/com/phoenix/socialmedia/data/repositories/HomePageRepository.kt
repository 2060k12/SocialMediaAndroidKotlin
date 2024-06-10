package com.phoenix.socialmedia.data.repositories

import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.phoenix.socialmedia.data.Comments
import com.phoenix.socialmedia.data.Post
import com.phoenix.socialmedia.data.Profile
import com.phoenix.socialmedia.data.Story
import kotlinx.coroutines.tasks.await

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


    // Live data for comment section
    private val _comments = MutableLiveData<ArrayList<Comments>>()
    val comments: LiveData<ArrayList<Comments>> get() = _comments

    // Livedata for Story List
    private val _story =MutableLiveData<ArrayList<Story>>()
    val story: LiveData<ArrayList<Story>> get() = _story



    // function to get all posts
    fun getAllPosts() {
        val postList = ArrayList<Post>()
        val currentUserEmail = auth.currentUser?.email.toString()

        db.collectionGroup("post")
            .get()

            .addOnSuccessListener {
                postResult ->
                var count = 0
                db.collection("users")
                    .document(currentUserEmail)
                    .collection("following")
                    .get()
                        .addOnSuccessListener { followingResult ->
                        val followingEmails = followingResult.documents.map { it.id }

                        for (document in postResult) {
                            val post = document.toObject(Post::class.java)
                            post.postId = document.id
                            if (post.email in followingEmails) {
                                postList.add(post)
                                count ++
                            }


                            if(count> 1 && (count % 2 ==0)){
//                                postList.add(Post("","","","","",false ,post.time ))
                            }

                        }
                            //this will update the list when all post has been processed
                            postList.sortByDescending { it.time.seconds }
                            fetchAllPostLikes(postList)

                    }
                    .addOnFailureListener { e ->
                        Log.i("Error Fetching Following", e.toString())
                    }
            }
            .addOnFailureListener { e ->
                Log.i("Error Fetching Posts", e.toString())
            }
    }

    private fun fetchAllPostLikes(postList: ArrayList<Post>) {
        postList.forEach {
            post  ->
            isPostLiked(postId = post.postId, postUploadedBy = post.email){
                post.likedByCurrentUser = it
            }
        }
        _posts.value = postList
    }

    fun isPostLiked(postId: String, postUploadedBy: String, callback: ( Boolean) -> Unit) {
        db.collection("users")
            .document(postUploadedBy)
            .collection("post")
            .document(postId)
            .collection("like")
            .get()
            .addOnSuccessListener {
                    document ->
                for (doc in document){
                    if(doc.id.contains (auth.currentUser?.email.toString()) ) {
                        callback(true)
                    }
                }

            }
            .addOnFailureListener{
                callback(false)
            }

    }



    // this function will like the post
    fun likeThePost(postId: String, email: String) {

        val newCollection = hashMapOf(
            "email" to email)

        db.collection("users")
            .document(email)
            .collection("post")
            .document(postId)
            .collection("like")
            .document(email)
            .set(newCollection)
            .addOnSuccessListener (){
                Log.i("Success", "$postId $email Successfully added")
            }
            .addOnFailureListener(){
                Log.i("Failed", it.message.toString())
            }

    }

    fun commentOnThePost(postId: String, email: String, comment: String){
        val newCollection = hashMapOf(
            "email" to auth.currentUser?.email.toString(),
            "comment" to comment
            )

        db.collection("users")
            .document(email)
            .collection("post")
            .document(postId)
            .collection("comment")
            .document(auth.currentUser?.email.toString())
            .set(newCollection)
            .addOnSuccessListener (){
                Log.i("Success", "${postId} $email Successfully added")
            }
            .addOnFailureListener(){
                Log.i("Failed", it.message.toString())
            }
    }


    fun getCurrentUser() : String {
        return auth.currentUser?.email.toString()
    }

    fun getUserProfileImage(email: String, callback :(String, String)-> Unit ){
        var profile : Profile
        val profileUrl = db.collection("users")
            .document(email)
            .get()
            .addOnSuccessListener {
                doc ->
                profile = doc.toObject(Profile::class.java)!!
                callback(profile.userImageUrl, profile.username)
            }
    }

    fun getALlComments(postUserEmail: String, postId: String){

        var commentList = ArrayList<Comments>()

        db.collection("users")
            .document(postUserEmail)
            .collection("post")
            .document(postId)
            .collection("comment")
            .get()
            .addOnSuccessListener {document ->
                for (doc in document){
                     val comment = doc.toObject(Comments::class.java)
                    commentList.add(comment)
                }
                _comments.value = commentList
            }

            .addOnFailureListener(){
                Log.i("Failed", it.message.toString())
            }


    }

    fun getTimeDifference(time:Long): String
    {
        val now = System.currentTimeMillis()
        val difference = now - time

        return when {
            difference <DateUtils.MINUTE_IN_MILLIS -> "Just Now"
            difference < DateUtils.HOUR_IN_MILLIS ->{
                val min = difference/DateUtils.MINUTE_IN_MILLIS
                "${min}m  ago"
            }
            difference < DateUtils.DAY_IN_MILLIS ->{
                val hour = difference/DateUtils.HOUR_IN_MILLIS
                "${hour}h ago"
            }
            difference < DateUtils.WEEK_IN_MILLIS ->{
                val days = difference/DateUtils.DAY_IN_MILLIS
                "${days}d  ago"
            }
            else -> {
                val weeks = difference / DateUtils.WEEK_IN_MILLIS
                "${weeks}w ago"
            }
        }

    }

    suspend fun getAllStory() {
        val storyList = ArrayList<Story>()
        val currentUserEmail = auth.currentUser?.email.toString()

        try {
            val storyResult = db.collectionGroup("story")
                .get()
                .await()

            try {
                val followingResult = db.collection("users")
                    .document(currentUserEmail)
                    .collection("following")
                    .get()
                    .await()

                val followingEmails = followingResult.documents.map { it.id }

                for (document in storyResult) {
                    val story = document.toObject(Story::class.java)
                    if (story.email in followingEmails) {
                        storyList.add(story)
                    }
                }


                storyList.sortBy { it.timestamp }
                _story.value = storyList

            }
            catch(e : Exception) {
                Log.i("Error Fetching Following", e.toString())
            }
        }
           catch (e: Exception){
                Log.i("Error Fetching Story", e.toString())
            }
    }


}




