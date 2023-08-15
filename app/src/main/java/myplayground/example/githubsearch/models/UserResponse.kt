package myplayground.example.githubsearch.models

import java.io.Serializable

data class UserResponse(
    val id: String,
    val login: String,
    val avatar_url: String,
    val name: String?,
    val followers: Int?,
    val following: Int?,
    val blog: String?,
    val location: String?,
    val bio: String?,
) : Serializable
