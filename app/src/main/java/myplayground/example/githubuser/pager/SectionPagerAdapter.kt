package myplayground.example.githubuser.pager

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import myplayground.example.githubuser.UserFollowersFragment
import myplayground.example.githubuser.UserFollowingFragment

class SectionPagerAdapter(activity: AppCompatActivity, username: String): FragmentStateAdapter(activity) {
    private val username = username
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position) {
            0 -> {
                fragment = UserFollowersFragment()
                (fragment as UserFollowersFragment).username = username
            }
            1 -> {
                fragment = UserFollowingFragment()
                (fragment as UserFollowingFragment).username = username
            }
            else -> {}
        }
        return fragment as Fragment
    }
}