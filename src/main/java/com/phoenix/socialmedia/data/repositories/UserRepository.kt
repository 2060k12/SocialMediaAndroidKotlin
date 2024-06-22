package com.phoenix.socialmedia.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.phoenix.socialmedia.data.Profile


class UserRepository  {

    val db = Firebase.firestore
    private val _searchResults = MutableLiveData<ArrayList<Profile>>()
    val searchResults : LiveData<ArrayList<Profile>> get() = _searchResults

    fun getSearchResult(searchText: String){
        val searchList =ArrayList<Profile>()
        db.collection("users")
            .whereEqualTo("username", searchText)
            .get()
            .addOnSuccessListener {
                    documents->
                for (doc in documents){
                    val profile = doc.toObject(Profile::class.java)
                    searchList.add(profile)
                }
                _searchResults.value = searchList
            }
            .addOnFailureListener { it ->
                Log.i("Error", it.toString())
            }
    }
}