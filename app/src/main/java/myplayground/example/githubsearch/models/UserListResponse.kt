package myplayground.example.githubsearch.models

import java.io.Serializable

data class UserListResponse (
    val total_count: Int,
    val incompleted_results: Boolean,
    val items: List<UserResponse>,
): Serializable
