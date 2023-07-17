package myplayground.example.githubuser

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import myplayground.example.githubuser.api.NetworkConfig
import myplayground.example.githubuser.api.UserDetailResponse
import myplayground.example.githubuser.databinding.ActivityUserDetailBinding
import myplayground.example.githubuser.pager.SectionPagerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailActivity : AppCompatActivity() {
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.user_detail_tab_text_1,
            R.string.user_detail_tab_text_2,
        )
    }

    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var username: String
    private lateinit var actionbar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        username = intent.getStringExtra("username")!!

        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        actionbar = supportActionBar!!
        actionbar.setDisplayHomeAsUpEnabled(true)

        val sectionPagerAdapter = SectionPagerAdapter(this, username)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) {tab, position ->
            val customTab = TextView(this)
            customTab.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            customTab.text = resources.getString(TAB_TITLES[position])
            customTab.gravity = Gravity.CENTER
            customTab.textSize = 20f
            tab.customView = customTab
        }.attach()

        loadData()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    private fun loadData() {
        loadSkeleton()
        NetworkConfig()
            .getService()
            .getUserDetail(username)
            .enqueue(object: Callback<UserDetailResponse> {
                override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                    hideSkeleton()
                    Toast.makeText(this@UserDetailActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<UserDetailResponse>,
                    response: Response<UserDetailResponse>
                ) {
                    hideSkeleton()
                    if(response.isSuccessful) {
                        val dataResponse = response.body()!!
                        GlideApp.with(this@UserDetailActivity).load(dataResponse.avatarUrl).into(binding.ivUser)
                        binding.tvUserName.text = dataResponse.name
                        binding.tvGithubUserName.text = dataResponse.login
                        actionbar.title = dataResponse.name
                    } else {
                        Toast.makeText(this@UserDetailActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    private fun loadSkeleton() {
        binding.tvUserName.loadSkeleton()
        binding.tvGithubUserName.loadSkeleton()
        binding.ivUser.loadSkeleton()
    }

    private fun hideSkeleton() {
        binding.tvUserName.hideSkeleton()
        binding.tvGithubUserName.hideSkeleton()
        binding.ivUser.hideSkeleton()
    }
}