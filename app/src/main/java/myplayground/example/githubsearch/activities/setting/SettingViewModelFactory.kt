package myplayground.example.githubsearch.activities.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import myplayground.example.githubsearch.util.SettingPreferences
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class SettingViewModelFactory(private val prefs: SettingPreferences) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(prefs) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}