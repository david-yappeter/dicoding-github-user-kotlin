package myplayground.example.githubsearch.activities

import android.os.Bundle
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import myplayground.example.githubsearch.adapter.SectionPagerAdapter
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
        loadTab()

        loadData()
    }

    private fun loadTab() {
        val tabTitles = arrayOf("Followers", "Following")
        val sectionPagerAdapter = SectionPagerAdapter(this, login)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    private fun loadData() {
        NetworkConfig.create<GithubService>(NetworkConfig.GITHUB_SERVICE_BASE_URL).getUser(login)
            .enqueue(object : Callback<UserResponse> {
                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Toast.makeText(this@UserDetailActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()!!
                        user = User.fromUserResponse(body)
                        fillView()
                    } else {
                        Toast.makeText(this@UserDetailActivity, "FAILED TO DO REQUEST", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })
    }


    private fun fillView() {
        with(binding) {
            Glide.with(this@UserDetailActivity).load(avatarUrl).into(binding.cvUser)
            tvUsername.text = login

            if (user != null) {
                val nameCapitalized =
                    user?.name?.lowercase()?.replaceFirstChar { it.titlecase() } ?: "-"
                changeAppbarTitle(nameCapitalized)

                tvName.text = nameCapitalized
                tvBlog.text = user?.blog ?: "-"
                tvBio.text = user?.bio ?: "-"
                tvLocation.text = user?.location ?: "-"
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