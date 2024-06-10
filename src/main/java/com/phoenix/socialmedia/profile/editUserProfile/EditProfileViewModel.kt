package com.phoenix.socialmedia.profile.editUserProfile

import androidx.lifecycle.ViewModel
import com.phoenix.socialmedia.data.repositories.ProfileRepository

class EditProfileViewModel : ViewModel() {

    private val profileRepository = ProfileRepository()
    fun updateUserInformation(editType: String, editInformation: String){
        profileRepository.updateUserInformation(editType, editInformation)
    }
}