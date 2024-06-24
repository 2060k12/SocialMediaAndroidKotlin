package com.phoenix.socialmedia.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.phoenix.socialmedia.data.Messages
import kotlinx.coroutines.tasks.await

class MessageRepository {

//    firestore and firebase auth from Firebase
    private val db = Firebase.firestore
    private val auth = Firebase.auth

//    Live data for messages
    private val _messageList = MutableLiveData<ArrayList<Messages>>()
    val messageList : LiveData<ArrayList<Messages>> get() = _messageList


//    Live data for all users we had conversation with
    private val _conversationList = MutableLiveData<ArrayList<String>>()
    val conversationList : LiveData<ArrayList<String>> = _conversationList

//    messageOf variable is an email of the user of whose conversation we are loading from current user profile
//    It is going to be a async function
    suspend fun getMessage(messageOf : String){
        try {
            val msgList = ArrayList<Messages>()
            val messages = db.collection("users")
                .document(auth.currentUser?.email.toString())
                .collection("messages")
                .document(messageOf)
                .collection("message")
                .get()
                .await()

            for (message in messages){
                val msg = message.toObject(Messages::class.java)
                msgList.add(msg)

            }
            msgList.sortBy { it.messageTimeStamp }
            _messageList.value = msgList


        }
        catch (e: Exception){
            Log.i("Error", e.message.toString())
        }


    }
    // function to send message to another user
    // async function
    suspend fun uploadMessage(messageOf : String, messageContent: String){
        try{
            val timestamp = Timestamp.now()
            val msg = hashMapOf(
                "emailOfMessenger" to auth.currentUser?.email.toString(),
                "messageContent" to messageContent,
                "messageTimeStamp" to timestamp)

                    val messagesRef = db.collection("users")
                    .document(auth.currentUser?.email.toString())
                    .collection("messages")
                    .document(messageOf)

                    // if the read status is already sent this if condition will not update it
                    if(messagesRef.get().await().get("readStatus").toString().lowercase() != "sent"){
                    messagesRef.set(hashMapOf("readStatus" to "sent",
                        "lastUpdated" to timestamp
                        ))}

                    messagesRef.collection("message")
                    .add(msg)
                    .await()

                val receiverMsg = hashMapOf(
                    "emailOfMessenger" to auth.currentUser?.email.toString(),
                    "messageContent" to messageContent,
                    "messageTimeStamp" to timestamp)

                    val receiverMessages = db.collection("users")
                    .document(messageOf)
                    .collection("messages")
                    .document(auth.currentUser?.email.toString())

                    // if the status is already received it wont be changed again
                    if(receiverMessages.get().await().get("readStatus").toString().lowercase() != "received"){
                    receiverMessages.set(hashMapOf("readStatus" to "received",
                        "lastUpdated" to timestamp))}
                    receiverMessages.collection("message")
                    .document()
                    .set(receiverMsg)
                    .await()
        }

        catch (e : Exception){
                println(e.message.toString())
            }
    }

    suspend fun getAllMessageOverView() {
        val listOfEmail = ArrayList<String>()
        try {
            val docs = db
                .collection("users")
                .document(auth.currentUser?.email.toString())
                .collection("messages")
                .get()
                .await()

            for(doc in docs){
                listOfEmail.add(doc.id)
            }
            _conversationList.value = listOfEmail  }
        catch (e: Exception){
            Log.e("Error", e.message.toString())
        }
    }

    // When user sees the message, the status will be set to seen
    // messageOf is a variable which contains the other user with whom the conversation is held in between
    suspend fun setReadStatus(messageOf: String) {
        try {
            val messagesRef = db.collection("users")
                .document(auth.currentUser?.email.toString())
                .collection("messages")
                .document(messageOf)


            if(messagesRef.get().await().get("readStatus").toString().lowercase() != "seen"){
            messagesRef.set(hashMapOf("readStatus" to "Seen")).await()}
        }
        catch (e: Exception){
            Log.e("Error", e.message.toString())
        }
    }

    // function to get the status of message
    suspend fun getReadStatus(messageOf: String, status : (String) -> Unit ){
        try {
            val messagesRef = db.collection("users")
                .document(messageOf)
                .collection("messages")
                .document(auth.currentUser?.email.toString())
                .get()
                .await()
            status(messagesRef.get("readStatus").toString())
        }
        catch (e: Exception){
            Log.e("Error", e.message.toString())
        }
    }

    suspend fun getReadStatusOverview(messageOf: String, status : (String) -> Unit ){
        try {
            val messagesRef = db.collection("users")
                .document(auth.currentUser?.email.toString())
                .collection("messages")
                .document(messageOf)
                .get()
                .await()
            status(messagesRef.get("readStatus").toString())
        }
        catch (e: Exception){
            Log.e("Error", e.message.toString())
        }
    }

}