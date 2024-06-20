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
    val messageList : LiveData<ArrayList<Messages>> =   messageRepository.messageList

//    Livedata of list of email of users we had conversation with
    val conversationList get() = messageRepository.conversationList


    //    Send a message to an user
    fun uploadMessage(messageOf: String, messageContent: String){

        viewModelScope.launch {
            messageRepository.uploadMessage(messageOf = messageOf, messageContent = messageContent)
        }
    }

//    function to get message from current username
    fun getMessage(messageOf : String) {
        viewModelScope.launch {
            messageRepository.getMessage(messageOf)
        }
    }

//    function to get all the users we have previously sent or received a message.
    fun getAllMessageOverview() {
        viewModelScope.launch {
            messageRepository.getAllMessageOverView()
        }

    }

//    function to set read status of a conversation
    fun setReadStatus(messageOf: String) {
    viewModelScope.launch {
        messageRepository.setReadStatus(messageOf)
    }
    }

    fun getReadStatus(messageOf: String, status : (String) -> Unit) {
       viewModelScope.launch {
           messageRepository.getReadStatus(messageOf, status)
       }
    }
    fun getReadStatusOverview(messageOf: String, status : (String) -> Unit) {
        viewModelScope.launch {
            messageRepository.getReadStatusOverview(messageOf, status)
        }
    }

}