package com.phoenix.socialmedia.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.phoenix.socialmedia.data.Profile
import com.phoenix.socialmedia.data.repositories.UserRepository

class SearchViewModel : ViewModel() {
    val userRepository = UserRepository()
    val searchResults :LiveData<ArrayList<Profile>> get() = userRepository.searchResults

    fun getSearchResult(searchText: String){
      userRepository.getSearchResult(searchText) }



}

