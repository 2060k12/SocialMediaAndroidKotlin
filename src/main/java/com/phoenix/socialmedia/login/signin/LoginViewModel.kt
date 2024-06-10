package com.phoenix.socialmedia.login.signin

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.phoenix.socialmedia.data.repositories.LoginRepository

class LoginViewModel: ViewModel() {

    private var loginRepository = LoginRepository()
    private val db = Firebase.firestore

    // To see if the user is currently signed in
    //if a user is logged in, the result will be true
    fun checkCurrentUser() : Boolean{
       return loginRepository.checkCurrentUser()
    }



//    function to sign in
    fun signIn(email: String, password: String, callback: (Boolean) ->Unit) {
        loginRepository.signIn(email, password, callback)
    }


}