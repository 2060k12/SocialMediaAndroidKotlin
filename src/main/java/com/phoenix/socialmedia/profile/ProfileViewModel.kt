package com.phoenix.socialmedia.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.phoenix.socialmedia.data.Post
import com.phoenix.socialmedia.data.Profile
import com.phoenix.socialmedia.data.repositories.ProfileRepository
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    // firebase auth & firestore database
    val auth= Firebase.auth
    private val db = Firebase.firestore

    // Repository
    private val profileRepository = ProfileRepository()

    //Livedata to get profile data
    val profile: LiveData<Profile> get() = profileRepository.profile

    // live data to getUserImage
    val imageUrls : LiveData<ArrayList<Post>> get() = profileRepository.imageUrls


    //live Data for our following features & following count
    var followings: LiveData<ArrayList<Profile>> = profileRepository.followings
    var followingCount :LiveData<Int> = profileRepository.followingCount


    //  live Data for our followers & followers count
    var followers: LiveData<ArrayList<Profile>> =profileRepository.followers
    var followersCount :LiveData<Int> = profileRepository.followersCount


    // Function to get all details of userProfile
    fun getCurrentProfileDetails(userEmail: String){
        profileRepository.getCurrentProfileDetails(userEmail)
    }

    fun getUserProfileDetails(email: String){
        profileRepository.getUserProfileDetails(email)

    }


    // Function to get the images uploaded by the user
    fun getUserAddedImages(userEmail : String){
        profileRepository.getUserAddedImages(userEmail)

    }

    // this function will add the email of the current user as a follower in  anotherUser profile
    fun followUser(email: String){
        profileRepository.followUser(email)
    }


    // function to get following of an user
      fun getFollowing(email: String){

         viewModelScope.launch {
             profileRepository.getFollowing(email)

         }
    }



    // fun to get followers of an user
    fun getFollowers(email: String){
        viewModelScope.launch {
            profileRepository.getFollowers(email)
        }
    }

    fun removeFollowing(email: String) {
        profileRepository.removeFollowing(email)

    }

    fun removeFollwers(email: String) {
        profileRepository.removeFollowers(email)


    }

}