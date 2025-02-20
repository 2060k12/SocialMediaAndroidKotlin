package com.phoenix.socialmedia.data.repositories

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.phoenix.socialmedia.data.Post
import com.phoenix.socialmedia.data.Profile
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID

class ProfileRepository {

    // firestorage
    private val fireStorage = FirebaseStorage.getInstance()


    // firebase auth & firestore database
    private val auth = Firebase.auth
    private var db = Firebase.firestore

    //Livedata to get profile data
    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile> get() = _profile

    // live data to getUserImage
    private val _imageUrls = MutableLiveData<ArrayList<Post>>()
    val imageUrls: LiveData<ArrayList<Post>> get() = _imageUrls


    //all  livedata for our following features
    private var _followings = MutableLiveData<ArrayList<Profile>>()
    var followings: LiveData<ArrayList<Profile>> = _followings

    private var _followingCount = MutableLiveData<Int>(0)
    var followingCount: LiveData<Int> = _followingCount


    // all liveData for our followers
    private var _followers = MutableLiveData<ArrayList<Profile>>()
    var followers: LiveData<ArrayList<Profile>> = _followers

    private var _followersCount = MutableLiveData<Int>(0)
    var followersCount: LiveData<Int> = _followersCount


    // Function to get all details of userProfile
    fun getCurrentProfileDetails(userEmail: String) {

        val docRef = db.collection("users").document(userEmail)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    _profile.value = document.toObject<Profile>() ?: Profile("", "", "", "", "")
                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }

    }

    fun getUserProfileDetails(email: String) {

        val docRef = db.collection("users").document(email)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    _profile.value = document.toObject<Profile>() ?: Profile("", "", "", "", "")
                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }

    }

    fun getUserProfileDetails(email: String, callback : (Profile) -> Unit) {

        var tempProfile: Profile
        val docRef = db.collection("users").document(email)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    tempProfile = document.toObject<Profile>() ?: Profile("", "", "", "", "")
                    callback(tempProfile)
                } else {
                    Log.d("TAG", "No such document")
                    callback(Profile("","","","",""))
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
                callback(Profile("","","","",""))

            }


    }



    // Function to get the images uploaded by the user
    fun getUserAddedImages(userEmail: String) {
        var postList = ArrayList<Post>()

        val docRef = db.collection("users")
            .document(userEmail)
            .collection("post")
            .orderBy("time", Query.Direction.DESCENDING)

        docRef.get()
            .addOnSuccessListener { results ->
                for (result in results) {
                    val newPost = result.toObject(Post::class.java)
                    newPost.postId = result.id
                    postList.add(newPost)

                }
                _imageUrls.value = postList
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }


    }

    // this function will add the email of the current user as a follower in  anotherUser profile
    fun followUser(email: String) {

        val timestamp: Date = Timestamp.now().toDate()
        val currentUserInfo = hashMapOf(
            "email" to auth.currentUser?.email.toString(),
            "followedDate" to timestamp
        )

        val searchedUserInfo = hashMapOf(
            "email" to email,
            "followedDate" to timestamp

        )
        db.collection("users")
            .document(email)
            .collection("followers")
            .document(auth.currentUser?.email.toString())
            .set(currentUserInfo)

            .addOnSuccessListener {
                db.collection("users")
                    .document(auth.currentUser?.email.toString())
                    .collection("following")
                    .document(email)
                    .set(searchedUserInfo)
                    .addOnSuccessListener {
                        Log.i("Success", "Successfully followed $email")
                    }
                    .addOnFailureListener {


                    }
            }

    }


    // function to get following of an user
    suspend fun getFollowing(email: String) {
        val listOfFollowings = ArrayList<Profile>()
        var count = 0
        try {
            val document = db.collection("users")
                .document(email)
                .collection("following")
                .get()
                .await()

            for (doc in document) {

                var eachFollowingUser = db.collection("users")
                    .document(doc.id)
                    .get()
                    .await()

                val profile = eachFollowingUser.toObject(Profile::class.java)
                if (profile != null) {
                    listOfFollowings.add(profile)
                }
                count += 1

            }

            _followings.value = listOfFollowings
            _followingCount.value = count
        } catch (e: Exception) {
            Log.i("Error", e.message.toString())

        }

    }


    // fun to get followers of an user
    suspend fun getFollowers(email: String) {
        val listOfFollowers = ArrayList<Profile>()
        var count = 0
        try {
            val document = db.collection("users")
                .document(email)
                .collection("followers")
                .get()
                .await()
            for (doc in document) {

                    var eachFollowers = db.collection("users")
                        .document(doc.id)
                        .get()
                        .await()

                    val profile = eachFollowers.toObject(Profile::class.java)
                    if (profile != null) {
                        listOfFollowers.add(profile)
                    }
                    count += 1


            }
            _followers.value = listOfFollowers
            _followersCount.value = count
        }
            catch( e : Exception) {
                Log.i("Error", e.message.toString())

            }
    }

    fun updateUserInformation(editType: String, editInformation: String) {
        val newInfo = mapOf(
            editType to editInformation
        )

        db.collection("users")
            .document(auth.currentUser?.email.toString())
            .update(newInfo)
            .addOnSuccessListener {
                Log.i("Success", "successfully edited")
            }
            .addOnFailureListener {
                Log.i("Failed", it.message.toString())

            }
    }

     fun removeFollowing(email: String) {
        db.collection("users")
            .document(auth.currentUser?.email.toString())
            .collection("following")
            .document(email)
            .delete()
            .addOnSuccessListener {
                db.collection("users")
                    .document(email)
                    .collection("followers")
                    .document(auth.currentUser?.email.toString())
                    .delete()
            }

    }

    fun removeFollowers(email: String) {
        db.collection("users")
            .document(auth.currentUser?.email.toString())
            .collection("followers")
            .document(email)
            .delete()
            .addOnSuccessListener {
                db.collection("users")
                    .document(email)
                    .collection("following")
                    .document(auth.currentUser?.email.toString())
                    .delete()
            }
    }

//     Check if the searched user has been followed by current user

    fun checkIfFollowed(searchedUser : String, state: (Boolean) -> Unit) {
        db.collection("users")
            .document(auth.currentUser?.email.toString())
            .collection("following")
            .get()
            .addOnSuccessListener {
              for (doc in it){
                  if(doc.id == searchedUser){
                      state(true)
                  }
              }
            }
            .addOnFailureListener {
                state(false)
            }

    }

    fun updateProfileImage(selectedImageUri: Uri) {

        var downloadUrl : Uri
        val path: String = "${auth.currentUser?.email}/post" + UUID.randomUUID() + ".png"
        val storageReference = fireStorage.getReference(path)
        val putFile = storageReference.putFile(selectedImageUri)
        putFile.continueWithTask(){
            storageReference.downloadUrl
                .addOnCompleteListener {
                    uri->
                    downloadUrl = uri.result
                    val updateDownloadUrl = hashMapOf("userImageUrl" to downloadUrl)
                    db.collection("users")
                        .document(auth.currentUser?.email.toString())
                        .update(updateDownloadUrl as Map<String, Any>)
                }
        }

    }

    fun deleteAccount() {
//        db.collection("users")
//            .document(auth.currentUser?.email.toString()).delete()
//            .addOnSuccessListener{
                auth.currentUser?.delete()
//            }
    }

}