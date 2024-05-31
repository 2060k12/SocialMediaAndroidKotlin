package com.phoenix.socialmedia.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Profile(
    val name: String,
    val username : String,
    val userCaption: String,
    val userImageUrl: String,
    val email: String
) : Parcelable {
    constructor() : this("", "", "", "","")
}
