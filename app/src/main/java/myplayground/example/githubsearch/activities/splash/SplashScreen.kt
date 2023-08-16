package myplayground.example.githubsearch.activities.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.airbnb.lottie.model.KeyPath
import com.airbnb.lottie.value.LottieValueCallback
import myplayground.example.githubsearch.activities.search.SearchActivity
import myplayground.example.githubsearch.activities.setting.SettingViewModel
import myplayground.example.githubsearch.activities.setting.SettingViewModelFactory
import myplayground.example.githubsearch.databinding.ActivitySplashScreenBinding
import myplayground.example.githubsearch.util.SettingPreferences
import myplayground.example.githubsearch.util.dataStore

@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {
    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        checkTheme()

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }

    private fun checkTheme() {
        val viewModel: SettingViewModel by viewModels {
            val settingPref = SettingPreferences.getInstance(application.dataStore)
            SettingViewModelFactory(settingPref)
        }

        viewModel.isDarkMode().observe(this) { isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                binding.logo.addValueCallback(
                    KeyPath("**"), LottieProperty.COLOR_FILTER, LottieValueCallback(
                        SimpleColorFilter(
                            Color.parseColor("#FFFFFF")
                        )
                    )
                )
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                binding.logo.addValueCallback(
                    KeyPath("**"), LottieProperty.COLOR_FILTER, LottieValueCallback(
                        SimpleColorFilter(
                            Color.parseColor("#000000")
                        )
                    )
                )
            }
        }
    }
}