package com.tina.musicband.data

data class User (
    var username: String,
    var userId: String,
    var background: String,
    var avatar: String,
    var speciality: String,
    var education: String,
    var position: String,
    var favouriteMusic: String,
    var introduction: String,
    var experience: String,
    var songs: Songs,
    var posts: Posts

)