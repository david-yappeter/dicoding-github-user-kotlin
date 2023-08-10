package myplayground.example.githubsearch.network

import myplayground.example.githubsearch.models.UserListResponse
import myplayground.example.githubsearch.models.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {
    @GET("search/users")
    fun searchUsers(
        @Query("q") search: String,
    ): Call<UserListResponse>

    @GET("users/{login}")
    fun getUser(
        @Path("login") login: String,
    ): Call<UserResponse>

    @GET("users/{login}/following")
    fun getUserFollowing(
        @Path("login") login: String,
    ): Call<UserResponse>

    @GET("users/{login}/follower")
    fun getUserFollower(
        @Path("login") login: String,
    ): Call<UserResponse>
}