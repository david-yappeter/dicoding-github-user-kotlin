package myplayground.example.githubsearch.network

import io.github.cdimascio.dotenv.dotenv
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkConfig() {
    companion object {
        const val GITHUB_SERVICE_BASE_URL="https://api.github.com/"

        inline fun <reified T> Create(baseUrl: String): T {
            val retrofit: Retrofit = Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(T::class.java)
        }
    }

    private fun preInterceptor(): OkHttpClient {
        val dotenvironment = dotenv {
            directory = "./assets"
            filename = "env"
        }
        val githubToken = dotenvironment.get("GITHUB_TOKEN", "")

        // logging
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        // auth
        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val reqBuilder = req.newBuilder()

            if (githubToken != "") {
                reqBuilder.addHeader("Authorization", "token $githubToken")
            }

            val requestHeaders = reqBuilder.build()
            chain.proceed(requestHeaders)
        }

        return OkHttpClient.Builder().addInterceptor(logging).addInterceptor(authInterceptor)
            .build()
    }
}