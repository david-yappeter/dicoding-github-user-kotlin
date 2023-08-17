package myplayground.example.githubsearch.activities.favourite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import myplayground.example.githubsearch.activities.detail.UserDetailActivity
import myplayground.example.githubsearch.adapter.UserListAdapter
import myplayground.example.githubsearch.databinding.ActivityFavouriteUsersBinding
import myplayground.example.githubsearch.models.User

class FavouriteUsersActivity : AppCompatActivity() {
    private var _binding: ActivityFavouriteUsersBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ActivityFavouriteUsersBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupAppbar()
        setupAdapter()

        loadData()
    }

    private fun setupAppbar() {
        val actionbar = supportActionBar!!
        actionbar.title = "Favourite Users"
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupAdapter() {
        binding.rvUsers.adapter = UserListAdapter { user ->
            val intent = Intent(this@FavouriteUsersActivity, UserDetailActivity::class.java)
            intent.putExtra(UserDetailActivity.INTENT_KEY_LOGIN, user.login)
            intent.putExtra(UserDetailActivity.INTENT_KEY_AVATAR_URL, user.avatar_url)
            startActivity(intent)
        }

        binding.rvUsers.layoutManager = LinearLayoutManager(this@FavouriteUsersActivity)

        binding.rvUsers.addItemDecoration(
            DividerItemDecoration(
                this@FavouriteUsersActivity,
                DividerItemDecoration.VERTICAL,
            )
        )
    }

    private fun loadData() {
        val viewModel: FavouriteUsersViewModel by viewModels {
            FavouriteUsersViewModelFactory.getInstance(this)
        }

        viewModel.fetchAll().observe(this) { favouriteUsers ->
            setLoadingAnimation(true)
            if (favouriteUsers.isEmpty()) {
                setNotData()
            } else {
                (binding.rvUsers.adapter as UserListAdapter?)?.apply {
                    this.setData(favouriteUsers.map { User.fromFavouriteUserEntity(it) })
                }
            }
            setLoadingAnimation(false)
        }
    }


    private fun setLoadingAnimation(v: Boolean) {
        if (v) {
            binding.rvUsers.visibility = View.GONE
            binding.noData.visibility = View.GONE
            binding.loading.visibility = View.VISIBLE
        } else {
            binding.loading.visibility = View.GONE
            binding.rvUsers.visibility = View.VISIBLE
        }
    }

    private fun setNotData() {
        binding.noData.visibility = View.VISIBLE
        binding.rvUsers.visibility = View.GONE

        Toast.makeText(this@FavouriteUsersActivity, "No data", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}