package com.animesh.social_media.Model

data class User (
     val name: String = "",
     val profession: String = "",
     val email: String = "",
     val password: String = "",
     val coverPhoto: String = "",
     var uid: String = "",
     val followerCount: Int = 0,
     val profilePhoto: String = "",
)