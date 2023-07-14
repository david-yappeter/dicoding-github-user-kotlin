package myplayground.example.githubuser

import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import myplayground.example.githubuser.databinding.ActivityUserDetailBinding
import myplayground.example.githubuser.pager.SectionPagerAdapter

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

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        username = intent.getStringExtra("username")!!

        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val actionbar = supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)

        val sectionPagerAdapter = SectionPagerAdapter(this, username)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) {tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}