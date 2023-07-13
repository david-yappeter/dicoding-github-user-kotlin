package myplayground.example.githubuser.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

class NetworkConfig {
    private fun getInterceptor(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(getInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getService() = getRetrofit().create(GithubApi::class.java)
}

interface GithubApi {
    @GET("search/users")
    @Headers("Authorization: token <GITHUB PERSONAL ACCESS TOKEN>")
    fun getUsers(@Query("q") username: String): Call<UserListResponse>

    @GET("search/users/{username}")
    @Headers("Authorization: token <GITHUB PERSONAL ACCESS TOKEN>")
    fun getUserDetail(@Path("username") username: String): Call<UserDetailResponse>

    @GET("search/users/{username}/followers")
    @Headers("Authorization: token <GITHUB PERSONAL ACCESS TOKEN>")
    fun getUserFollowers(@Path("username") username: String): Call<UserFollowersResponse>

    @GET("search/users/{username}/following")
    @Headers("Authorization: token <GITHUB PERSONAL ACCESS TOKEN>")
    fun getUserFollowing(@Path("username") username: String): Call<UserFollowingResponse>
}
