package myplayground.example.githubsearch.activities

import android.os.Bundle
import com.bumptech.glide.Glide
import myplayground.example.githubsearch.databinding.ActivityUserDetailBinding
import myplayground.example.githubsearch.models.User
import myplayground.example.githubsearch.models.UserResponse
import myplayground.example.githubsearch.network.GithubService
import myplayground.example.githubsearch.network.NetworkConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailActivity : DrawerActivity() {
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var id: String
    private lateinit var login: String
    private lateinit var avatarUrl: String
    private var user: User? = null

    companion object {
        const val INTENT_KEY_ID = "USER_DETAIL_ID"
        const val INTENT_KEY_LOGIN = "USER_DETAIL_LOGIN"
        const val INTENT_KEY_AVATAR_URL = "USER_DETAIL_AVATAR_URL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUserDetailBinding.inflate(layoutInflater)

        id = intent.getStringExtra(INTENT_KEY_ID)!!
        login = intent.getStringExtra(INTENT_KEY_LOGIN)!!
        avatarUrl = intent.getStringExtra(INTENT_KEY_AVATAR_URL)!!

        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        changeAppbarTitle(login.lowercase().replaceFirstChar { it.titlecase() })
        fillView()

        loadData()
    }

    private fun loadData() {
        NetworkConfig.Create<GithubService>(NetworkConfig.GITHUB_SERVICE_BASE_URL).getUser(login)
            .enqueue(object : Callback<UserResponse> {
                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if(response.isSuccessful) {
                        val body = response.body()!!
                        user = User.fromUserResponse(body)
                        fillView()
                    } else {
                        TODO("not implemented")
                    }
                }
            })
    }

    private fun fillView() {
        with(binding) {
            Glide.with(this@UserDetailActivity).load(avatarUrl).into(binding.cvUser)
            tvUsername.text = login

            if(user != null) {
                val nameCapitalized = user?.name?.lowercase()?.replaceFirstChar { it.titlecase() } ?: "-"
                changeAppbarTitle(nameCapitalized)

                tvName.text = nameCapitalized
                tvBlog.text = user?.blog ?: "-"
                tvBio.text = user?.bio ?: "-"
                tvLocation.text = user?.location?.toString() ?: "-"
                tvFollowers.text = user?.followers?.toString() ?: "-"
                tvFollowings.text = user?.following?.toString() ?: "-"
            }
        }
    }

    private fun changeAppbarTitle(title: String) {
        val actionbar = supportActionBar!!
        actionbar.title = title
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}