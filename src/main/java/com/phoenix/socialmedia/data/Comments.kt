package com.phoenix.socialmedia.data

data class Comments(
    val comment: String,
    val email: String,
)
{
    constructor(): this("","")
}