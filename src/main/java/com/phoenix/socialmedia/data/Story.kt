package com.phoenix.socialmedia.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    val imageUrl : String,
    val email: String,
    val timestamp: Timestamp
) : Parcelable {
    constructor(): this("","", Timestamp.now())
}
