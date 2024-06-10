package com.phoenix.socialmedia.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    var imageUrl: String,
    var caption: String,
    var email: String,
    var username: String,
    var postId: String,
    var likedByCurrentUser: Boolean,
    var time: Timestamp): Parcelable {
    constructor(): this("","" ,"","","",false,Timestamp.now())
}
