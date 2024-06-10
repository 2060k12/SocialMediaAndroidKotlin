package com.phoenix.socialmedia.data

import com.google.firebase.Timestamp

data class Story(
    val imageUrl : String,
    val email: String,
    val timestamp: Timestamp
){
    constructor(): this("","", Timestamp.now())
}
