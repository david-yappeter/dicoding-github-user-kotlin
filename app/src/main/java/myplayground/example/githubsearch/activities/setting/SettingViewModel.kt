package myplayground.example.githubsearch.activities.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import myplayground.example.githubsearch.util.SettingPreferences

class SettingViewModel(private val pref: SettingPreferences) : ViewModel() {
    fun isDarkMode(): LiveData<Boolean> {
        return pref.getDarkModeSetting().asLiveData()
    }

    fun setDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            pref.saveDarkModeSetting(isDarkMode)
        }
    }
}