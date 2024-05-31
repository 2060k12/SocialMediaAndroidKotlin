package com.phoenix.socialmedia.data

data class Post(
    var imageUrl: String,
    var caption: String,
    var email: String,
    var username: String,
    var uploadDateTimestamp: String){
    constructor(): this("","" ,"","","")
}
