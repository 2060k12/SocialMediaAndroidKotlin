package com.phoenix.socialmedia.messages

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenix.socialmedia.data.Messages
import com.phoenix.socialmedia.data.repositories.MessageRepository
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel() {


    // using message repository
    private val messageRepository = MessageRepository()

    // livedata
    val messageList : LiveData<ArrayList<Messages>> get() =   messageRepository.messageList

    fun uploadMessage(messageOf: String, messageContent: String){

        viewModelScope.launch {
            messageRepository.uploadMessage(messageOf = messageOf, messageContent = messageContent)
        }
    }

    fun getMessage(messageOf : String) {
        viewModelScope.launch {
            messageRepository.getMessage(messageOf)
        }
    }

}