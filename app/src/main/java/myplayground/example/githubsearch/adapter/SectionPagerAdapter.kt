package myplayground.example.githubsearch.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import myplayground.example.githubsearch.activities.detail.FragmentUserFollowers
import myplayground.example.githubsearch.activities.detail.FragmentUserFollowing

class SectionPagerAdapter(activity: AppCompatActivity, private val username: String) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = FragmentUserFollowers()
                fragment.username = username
            }

            1 -> {
                fragment = FragmentUserFollowing()
                fragment.username = username
            }

            else -> {}
        }
        return fragment as Fragment
    }
}