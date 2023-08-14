package myplayground.example.githubsearch.activities.search

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import myplayground.example.githubsearch.R
import myplayground.example.githubsearch.activities.drawer.DrawerActivity
import myplayground.example.githubsearch.activities.detail.UserDetailActivity
import myplayground.example.githubsearch.adapter.UserListAdapter
import myplayground.example.githubsearch.databinding.ActivitySearchBinding
import myplayground.example.githubsearch.models.User
import myplayground.example.githubsearch.models.UserListResponse
import myplayground.example.githubsearch.network.GithubService
import myplayground.example.githubsearch.network.NetworkConfig
import myplayground.example.githubsearch.util.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal

class SearchActivity : DrawerActivity() {
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySearchBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupAdapter()
        setupSearchBar()

        if (shouldShowPrompt()) {
            showPrompt()
            markPromptShown()
        }

        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    private fun setupAdapter() {
        with(binding) {
            rvUsers.adapter = UserListAdapter { user ->
                val intent = Intent(this@SearchActivity, UserDetailActivity::class.java)
                intent.putExtra(UserDetailActivity.INTENT_KEY_LOGIN, user.login)
                intent.putExtra(UserDetailActivity.INTENT_KEY_AVATAR_URL, user.avatar_url)
                startActivity(intent)
            }

            rvUsers.layoutManager = LinearLayoutManager(this@SearchActivity)

            rvUsers.addItemDecoration(
                DividerItemDecoration(
                    this@SearchActivity,
                    DividerItemDecoration.VERTICAL,
                )
            )
        }
    }

    private fun setupSearchBar() {
        with(binding) {
            svUser.setupWithSearchBar(sbUser)
            svUser.editText.setOnEditorActionListener { _, _, _ ->


                sbUser.text = svUser.text
                svUser.hide()

                if (svUser.text.toString().isEmpty()) {
                    Toast.makeText(this@SearchActivity, "Empty Input", Toast.LENGTH_SHORT).show()
                } else {
                    loadData(svUser.text.toString()) //                Toast.makeText(this@MenuActivity, searchView.text, Toast.LENGTH_SHORT).show()
                }
                false
            }
        }
    }

    private fun loadData(search: String) {
        setLoadingAnimation(true)
        NetworkConfig.create<GithubService>(NetworkConfig.GITHUB_SERVICE_BASE_URL)
            .searchUsers(search).enqueue(object : Callback<UserListResponse> {
                override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                    setLoadingAnimation(false)
                    Toast.makeText(this@SearchActivity, t.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onResponse(
                    call: Call<UserListResponse>, response: Response<UserListResponse>
                ) {
                    setLoadingAnimation(false)
                    if (response.isSuccessful) {
                        val body = response.body()!!

                        if (body.items.isEmpty()) {
                            setNotData()
                            return
                        }

                        (binding.rvUsers.adapter as? UserListAdapter)?.apply {
                            setData(body.items.map { item -> User.fromUserResponse(item) })
                        }
                    } else {
                        Toast.makeText(
                            this@SearchActivity,
                            "FAILED TO DO REQUEST",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            })
    }

    private fun setLoadingAnimation(v: Boolean) {
        if (v) {
            binding.rvUsers.visibility = View.GONE
            binding.noData.visibility = View.GONE
            binding.idle.visibility = View.GONE
            binding.loading.visibility = View.VISIBLE
        } else {
            binding.loading.visibility = View.GONE
            binding.rvUsers.visibility = View.VISIBLE
        }
    }

    private fun setNotData() {
        binding.noData.visibility = View.VISIBLE
        binding.rvUsers.visibility = View.GONE

        Toast.makeText(this@SearchActivity, "No user found", Toast.LENGTH_SHORT)
            .show()
    }

    private fun shouldShowPrompt(): Boolean {
        val sharedPreferences: SharedPreferences = SharedPreferencesManager.getSharedPrefs(this)
        return !sharedPreferences.getBoolean("prompt_shown", false)
    }

    private fun showPrompt() {
        MaterialTapTargetPrompt.Builder(this).setTarget(binding.sbUser)
            .setPrimaryText("Welcome to Github Search")
            .setSecondaryText("Let's try to search your first user")
            .setBackButtonDismissEnabled(true)
            .setBackgroundColour(ContextCompat.getColor(this, R.color.green))
            .setPromptBackground(RectanglePromptBackground()).setPromptFocal(RectanglePromptFocal())
            .show()
    }

    private fun markPromptShown() {
        val sharedPreferences: SharedPreferences = SharedPreferencesManager.getSharedPrefs(this)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("prompt_shown", true)
        editor.apply()
    }
}