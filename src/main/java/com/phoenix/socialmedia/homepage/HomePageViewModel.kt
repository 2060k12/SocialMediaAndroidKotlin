package com.phoenix.socialmedia.homepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.phoenix.socialmedia.data.Post
import com.phoenix.socialmedia.data.repositories.HomePageRepository

class HomePageViewModel : ViewModel() {

    // initializing our HomePageRepository & the live data
    private val homePageRepository = HomePageRepository()
    val post: LiveData<ArrayList<Post>> get() = homePageRepository.post

    // function to get all posts
    fun getALlPost(){
        homePageRepository.getAllPosts()
    }



}