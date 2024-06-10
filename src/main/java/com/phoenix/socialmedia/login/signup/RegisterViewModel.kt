package com.phoenix.socialmedia.login.signup

import androidx.lifecycle.ViewModel
import com.phoenix.socialmedia.data.repositories.LoginRepository

class RegisterViewModel : ViewModel() {


    private val loginRepository = LoginRepository()

    // function to sign up as a new user
    fun signUp(email: String, password: String, name: String, userName: String, callback: (Boolean) -> Unit){
        loginRepository.signUp(email = email, password = password, name = name, userName = userName, callback)
    }
}