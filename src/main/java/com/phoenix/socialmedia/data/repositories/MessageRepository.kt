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
    val conversationList : LiveData<ArrayList<String>> get() = _conversationList

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
    suspend fun uploadMessage(messageOf : String, messageContent: String){
        try{
            val timestamp = Timestamp.now()
            val msg = hashMapOf(
                "emailOfMessenger" to auth.currentUser?.email.toString(),
                "messageContent" to messageContent,
                "messageTimeStamp" to timestamp)

                    var messagesRef = db.collection("users")
                    .document(auth.currentUser?.email.toString())
                    .collection("messages")
                    .document(messageOf)

                    messagesRef.set(hashMapOf("email" to auth.currentUser?.email.toString() ))
                    messagesRef.collection("message")
                    .add(msg)
                    .await()

                val receiverMsg = hashMapOf(
                    "emailOfMessenger" to auth.currentUser?.email.toString(),
                    "messageContent" to messageContent,
                    "messageTimeStamp" to timestamp)

                var receiverMessages = db.collection("users")
                    .document(messageOf)
                    .collection("messages")
                    .document(auth.currentUser?.email.toString())
                    .collection("message")
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
            _conversationList.value = listOfEmail
        }
        catch (e: Exception){
            Log.e("Error", e.message.toString())
        }
    }


}