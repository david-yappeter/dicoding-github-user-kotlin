package myplayground.example.githubsearch.activities

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.animation.AnticipateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import myplayground.example.githubsearch.R
import myplayground.example.githubsearch.adapter.UserListAdapter
import myplayground.example.githubsearch.databinding.ActivitySearchBinding
import myplayground.example.githubsearch.models.User
import myplayground.example.githubsearch.models.UserListResponse
import myplayground.example.githubsearch.network.GithubService
import myplayground.example.githubsearch.network.NetworkConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal

class SearchActivity : DrawerActivity() {
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) { // initialize splash screen
        initSplashScreen()

        binding = ActivitySearchBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupAdapter()
        setupSearchBar()
        showPrompt()

        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    private fun setupAdapter() {
        with(binding) {
            rvUsers.adapter = UserListAdapter { user ->
                val intent = Intent(this@SearchActivity, UserDetailActivity::class.java)
                intent.putExtra(UserDetailActivity.INTENT_KEY_ID, user.id)
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
            svUser.editText.setOnEditorActionListener { textView, actionId, event ->
                sbUser.text = svUser.text
                svUser.hide()
                loadData(svUser.text.toString()) //                Toast.makeText(this@MenuActivity, searchView.text, Toast.LENGTH_SHORT).show()
                false
            }
        }
    }

    private fun loadData(search: String) {
        NetworkConfig.Create<GithubService>(NetworkConfig.GITHUB_SERVICE_BASE_URL)
            .searchUsers(search).enqueue(object : Callback<UserListResponse> {
                override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

                override fun onResponse(
                    call: Call<UserListResponse>, response: Response<UserListResponse>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()!!
                        (binding.rvUsers.adapter as? UserListAdapter)?.apply {
                            setData(body.items.map { item -> User.fromUserResponse(item) })
                        }
                    } else {
                        TODO("not implemented")
                    }
                }
            })
    }

    private fun initSplashScreen() {
        val splashScreen = installSplashScreen()

        splashScreen.setOnExitAnimationListener { splashScreenView -> // Create fade out animation
            val slideUp = ObjectAnimator.ofFloat(
                splashScreenView.view,
                "alpha",
                1f,
                0f,
            )
            slideUp.interpolator = AnticipateInterpolator()
            slideUp.duration = 300L

            // Call SplashScreenView.remove at the end of your custom animation.
            slideUp.doOnEnd { splashScreenView.remove() }

            // Run your animation.
            slideUp.start()
        }
    }

    private fun showPrompt() {
        MaterialTapTargetPrompt.Builder(this).setTarget(binding.sbUser)
            .setPrimaryText("Search Github User").setBackButtonDismissEnabled(true)
            .setPromptBackground(RectanglePromptBackground()).setPromptFocal(RectanglePromptFocal())
            .show()
    }
}