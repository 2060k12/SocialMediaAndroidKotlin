package com.phoenix.socialmedia.homepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenix.socialmedia.data.Post
import com.phoenix.socialmedia.data.Story
import com.phoenix.socialmedia.data.repositories.HomePageRepository
import kotlinx.coroutines.launch

class HomePageViewModel : ViewModel() {

    // initializing our HomePageRepository & the live data
    private val homePageRepository = HomePageRepository()
    val post: LiveData<ArrayList<Post>> get() = homePageRepository.post
    val story: LiveData< ArrayList<Story>> get() = homePageRepository.story

    fun getCurrentUser(): String{
        return homePageRepository.getCurrentUser()
    }

    // function to get all posts
    fun getALlPost(){
        homePageRepository.getAllPosts()
    }

    fun getUserProfileImage(email: String, callback: (String, String)-> Unit ){
        homePageRepository.getUserProfileImage(email, callback)
    }

    fun getTimeDifference(time:Long): String{
        return homePageRepository.getTimeDifference(time)
    }

    fun getAllStory() {
        viewModelScope.launch {
            homePageRepository.getAllStory()

        }
    }

    fun isPostLiked(postId: String, postedBy: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            return@launch homePageRepository.isPostLiked(postId, postedBy, callback)

        }}


    }


