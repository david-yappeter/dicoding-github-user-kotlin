package myplayground.example.githubsearch.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import myplayground.example.githubsearch.R
import myplayground.example.githubsearch.databinding.ActivityUserDetailBinding

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding

    companion object {
        const val INTENT_KEY_ID = "USER_DETAIL_ID"
        const val INTENT_KEY_NAME = "USER_DETAIL    _NAME"
        const val INTENT_KEY_NAME = "USER_DETAIL_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUserDetailBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupAppbar()
    }

    private fun setupAppbar() {
        val actionbar = supportActionBar!!

        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}