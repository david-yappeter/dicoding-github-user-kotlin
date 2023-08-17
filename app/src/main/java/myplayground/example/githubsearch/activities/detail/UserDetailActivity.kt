package myplayground.example.githubsearch.activities.detail

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import myplayground.example.githubsearch.R
import myplayground.example.githubsearch.activities.di.Injection
import myplayground.example.githubsearch.activities.drawer.DrawerActivity
import myplayground.example.githubsearch.adapter.SectionPagerAdapter
import myplayground.example.githubsearch.database.FavouriteUserEntity
import myplayground.example.githubsearch.databinding.ActivityUserDetailBinding
import myplayground.example.githubsearch.models.User

class UserDetailActivity : DrawerActivity() {
    private var _binding: ActivityUserDetailBinding? = null
    private val binding get() = _binding!!


    private val viewModel: UserDetailViewModel by viewModels {
        UserDetailViewModelFactory(login, Injection.provideFavouriteUsersRepository(this))
    }

    private lateinit var id: String
    private lateinit var login: String
    private lateinit var avatarUrl: String
    private var user: User? = null
    private var favouriteUser: FavouriteUserEntity? = null
    private var isFavourite = false


    companion object {
        const val INTENT_KEY_ID = "USER_DETAIL_ID"
        const val INTENT_KEY_LOGIN = "USER_DETAIL_LOGIN"
        const val INTENT_KEY_AVATAR_URL = "USER_DETAIL_AVATAR_URL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ActivityUserDetailBinding.inflate(layoutInflater)

        id = intent.getStringExtra(INTENT_KEY_ID)!!
        login = intent.getStringExtra(INTENT_KEY_LOGIN)!!
        avatarUrl = intent.getStringExtra(INTENT_KEY_AVATAR_URL)!!
        favouriteUser = FavouriteUserEntity(
            id = id,
            login = login,
            avatar_url = avatarUrl,
        )

        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        changeAppbarTitle(login.lowercase().replaceFirstChar { it.titlecase() })
        handleOrientationChanged(this.resources.configuration.orientation)
        fillView()

        addFab()


        loadTab()
        loadData()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun addFab() {
        viewModel.getFavouriteUserById(id).observe(this) { favouriteUser ->
            isFavourite = favouriteUser != null

            if (favouriteUser == null) {
                binding.favFab.setImageDrawable(resources.getDrawable(R.drawable.favourite_border, null))
            } else {
                binding.favFab.setImageDrawable(resources.getDrawable(R.drawable.favourite_filled, null))
            }
        }

        binding.favFab.setOnClickListener {
            if (isFavourite) {
                viewModel.deleteFavouriteUser(favouriteUser!!)
                Toast.makeText(
                    this@UserDetailActivity,
                    "${favouriteUser!!.login} telah dihapus dari User Favorit",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.insertFavouriteUser(favouriteUser!!)
                Toast.makeText(
                    this@UserDetailActivity,
                    "${favouriteUser!!.login} telah ditambahkan dari User Favorit",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun loadData() {
        viewModel.user.observe(this@UserDetailActivity) { userObs ->
            if (userObs != null) {
                user = userObs
                fillView()
            }
        }
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

    private fun handleOrientationChanged(orientation: Int) {
        val layoutParams = binding.viewPager.layoutParams
        var heightInDp = 0

        when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                heightInDp = 200
            }

            Configuration.ORIENTATION_PORTRAIT -> {
                heightInDp = 0
            }
        }

        // Convert dp to pixels based on device's screen density
        val density = resources.displayMetrics.density
        val heightInPixel = (heightInDp * density).toInt()

        layoutParams.height = heightInPixel
        binding.viewPager.layoutParams = layoutParams
    }

    private fun changeAppbarTitle(title: String) {
        val actionbar = supportActionBar!!
        actionbar.title = title
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        handleOrientationChanged(newConfig.orientation)
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}