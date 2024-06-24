package com.phoenix.socialmedia.homepage.posts.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.phoenix.socialmedia.data.Comments
import com.phoenix.socialmedia.data.repositories.HomePageRepository

class CommentViewModel : ViewModel() {

    private val homePageRepository = HomePageRepository()

    val comments : LiveData<ArrayList<Comments>> get() = homePageRepository.comments

    // function to like the post
    fun likeThePost(postId: String, email: String){
        homePageRepository.likeThePost(postId, email)
    }

    // function to comment on the post
    fun commentThePost(postId: String, email: String, comment: String){
        homePageRepository.commentOnThePost(postId, email, comment)
    }

    fun getCurrentUser() : String {
        return homePageRepository.getCurrentUser()
    }

    fun getAllComments(postUserEmail: String, postId: String){
    homePageRepository.getALlComments(postUserEmail, postId)    }

    fun deleteLike(email: String, postId: String) {
        homePageRepository.deleteLike(email, postId)

    }

}