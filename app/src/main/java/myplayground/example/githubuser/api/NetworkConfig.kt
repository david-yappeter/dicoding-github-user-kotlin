package myplayground.example.githubuser.api

import okhttp3.Interceptor
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
    private fun authInterceptor(): Interceptor {
        return Interceptor {chain ->
            val req = chain.request()
            val requestHeaders = req.newBuilder()
                .addHeader("Authorization", "token <PERSONAL ACCESS TOKEN>").
                    build()
            chain.proceed(requestHeaders)
        }
    }

    private fun getInterceptor(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authInterceptor())
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
    fun getUsers(@Query("q") username: String): Call<UserListResponse>

    @GET("users/{username}")
    fun getUserDetail(@Path("username") username: String): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    fun getUserFollowers(@Path("username") username: String): Call<List<UserFollowersResponseItem>>

    @GET("users/{username}/following")
    fun getUserFollowing(@Path("username") username: String): Call<List<UserFollowingResponseItem>>
}
