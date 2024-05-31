package com.phoenix.socialmedia.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.phoenix.socialmedia.data.Post
import com.phoenix.socialmedia.data.Profile
import java.util.Date

class ProfileRepository {

    // firebase auth & firestore database
    private val auth= Firebase.auth
    private var db = Firebase.firestore

    //Livedata to get profile data
    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile> get() = _profile

    // live data to getUserImage
    private val _imageUrls = MutableLiveData<ArrayList<Post>>()
    val imageUrls : LiveData<ArrayList<Post>> get() = _imageUrls



    //all  livedata for our following features
    private var _followings= MutableLiveData<ArrayList<Profile>>()
    var followings: LiveData<ArrayList<Profile>> =_followings

    private var _followingCount = MutableLiveData<Int>(0)
    var followingCount : LiveData<Int> = _followingCount


    // all liveData for our followers
    private var _followers= MutableLiveData<ArrayList<Profile>>()
    var followers: LiveData<ArrayList<Profile>> =_followers

    private var _followersCount = MutableLiveData<Int>(0)
    var followersCount : LiveData<Int> = _followersCount



    // Function to get all details of userProfile
    fun getCurrentProfileDetails(userEmail: String){

        val docRef = db.collection("users").document(userEmail)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    _profile.value = document.toObject<Profile>()?: Profile("","","","","")
                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }

    }

    fun getUserProfileDetails(email: String){

        val docRef = db.collection("users").document(email)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    _profile.value = document.toObject<Profile>()?: Profile("","","","","")
                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }

    }


    // Function to get the images uploaded by the user
    fun getUserAddedImages(userEmail : String){
        var postList = ArrayList<Post>()

        val docRef = db.collection("users")
            .document(userEmail)
            .collection("post")


        docRef.get()
            .addOnSuccessListener { results->
                for (result in results){
                    postList.add(result.toObject(Post:: class.java))
                }
                _imageUrls.value = postList
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }


    }

    // this function will add the email of the current user as a follower in  anotherUser profile
    fun followUser(email: String){

        val timestamp : Date = Timestamp.now().toDate()
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
                    .addOnFailureListener(){


                    }


            }

    }


    // function to get following of an user
    fun getFollowing(email: String){
        val listOfFollowers= ArrayList<Profile>()
        var count = 0
        db.collection("users")
            .document(email)
            .collection("following")
            .get()

            .addOnSuccessListener {
                for (doc in it) {
                    var eachFollowingUser = doc.toObject(Profile::class.java)
                    listOfFollowers.add(eachFollowingUser)
                    count +=1
                }
                _followings.value = listOfFollowers
                _followingCount.value = count
            }
            .addOnFailureListener(){
                Log.i("Error", it.message.toString())

            }
    }



    // fun to get followers of an user
    fun getFollowers(email: String){
        val listOfFollowers= ArrayList<Profile>()
        var count = 0
        db.collection("users")
            .document(email)
            .collection("followers")
            .get()

            .addOnSuccessListener {
                for (doc in it) {
                    var eachFollowers = doc.toObject(Profile::class.java)
                    listOfFollowers.add(eachFollowers)
                    count +=1
                }
                _followers.value = listOfFollowers
                _followersCount.value = count
            }
            .addOnFailureListener(){
                Log.i("Error", it.message.toString())

            }
    }

}