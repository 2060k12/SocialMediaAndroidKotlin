package com.phoenix.socialmedia.data

import com.google.firebase.Timestamp

data class Messages (
    val messageContent : String,
    val messageTimeStamp : Timestamp,
    val emailOfMessenger : String
)