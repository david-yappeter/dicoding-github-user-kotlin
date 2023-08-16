package myplayground.example.githubsearch.activities.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import myplayground.example.githubsearch.R
import myplayground.example.githubsearch.activities.drawer.DrawerActivity
import myplayground.example.githubsearch.databinding.ActivitySettingBinding

class SettingActivity : DrawerActivity() {
    private var _binding: ActivitySettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ActivitySettingBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.darkMode.setOnCheckedChangeListener { _, isChecked ->
            binding.darkMode.isChecked = isChecked

            Log.i("ASDASDASDASD", isChecked.toString())

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                delegate.applyDayNight()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                delegate.applyDayNight()
            }
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