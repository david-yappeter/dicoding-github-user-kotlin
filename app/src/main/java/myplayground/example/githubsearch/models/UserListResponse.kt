package myplayground.example.githubsearch.models

import java.io.Serializable

data class UserListResponse (
    val items: List<UserResponse>,
): Serializable
