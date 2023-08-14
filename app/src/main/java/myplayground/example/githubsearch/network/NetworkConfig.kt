package myplayground.example.githubsearch.network

import io.github.cdimascio.dotenv.dotenv
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("SameParameterValue")
class NetworkConfig {
    companion object {
        const val GITHUB_SERVICE_BASE_URL="https://api.github.com/"

        inline fun <reified T> create(baseUrl: String): T {
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

            val httpClient = OkHttpClient.Builder().addInterceptor(logging).addInterceptor(authInterceptor)
                .build()

            val retrofit: Retrofit = Retrofit.Builder().baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(T::class.java)
        }
    }
}