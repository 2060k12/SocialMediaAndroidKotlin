package com.phoenix.socialmedia.data.repositories

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class LoginRepository {
    private var auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore

    // To see if the user is currently signed in
    //if a user is logged in, the result will be true
    fun checkCurrentUser() : Boolean{
        val currentUser = auth.currentUser
        return currentUser != null
    }


    // function to sign up as a new user
    fun signUp(email: String, password: String, name: String, userName: String, callback: (Boolean) -> Unit){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    val newUserDetails = hashMapOf(
                        "name" to name,
                        "username" to userName,
                        "email" to email,
                    )
                    // add a documents
                    db.collection("users")
                        .document(email)
                        .set(newUserDetails)
                        .addOnSuccessListener {
                            callback(true)
                        }

                } else {
                    // If sign in fails, set signUp status to false
                    callback(false)
                }
            }
    }

    //    function to sign in
    fun signIn(email: String, password: String, callback: (Boolean) ->Unit) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    callback(true)


                } else {
                    // If sign in fails, set signUp status to false
                    callback(false)
                }


            }


    }




}