package myplayground.example.githubsearch.activities.setting

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import myplayground.example.githubsearch.activities.detail.UserDetailViewModel
import myplayground.example.githubsearch.activities.detail.UserDetailViewModelFactory
import myplayground.example.githubsearch.activities.drawer.DrawerActivity
import myplayground.example.githubsearch.databinding.ActivitySettingBinding
import myplayground.example.githubsearch.util.SettingPreferences
import myplayground.example.githubsearch.util.dataStore

class SettingActivity : DrawerActivity() {
    private var _binding: ActivitySettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ActivitySettingBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        setupViewModel()

    }

    private fun setupViewModel() {
        val viewModel: SettingViewModel by viewModels {
            val settingPref = SettingPreferences.getInstance(application.dataStore)
            SettingViewModelFactory(settingPref)
        }

        viewModel.isDarkMode().observe(this) {isDarkMode->
            if (isDarkMode) {
                binding.darkMode.isChecked = true
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                binding.darkMode.isChecked = false
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.darkMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setDarkMode(isChecked)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}