package myplayground.example.githubsearch.activities.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import myplayground.example.githubsearch.models.User
import myplayground.example.githubsearch.network.GithubService
import myplayground.example.githubsearch.network.NetworkConfig

class UserDetailViewModel(username: String) : ViewModel() {
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        viewModelScope.launch {
            getUserDetail(username)
        }
    }

    private suspend fun getUserDetail(username: String) {
        coroutineScope.launch {

            val getUserDetailAsync =
                NetworkConfig.create<GithubService>(NetworkConfig.GITHUB_SERVICE_BASE_URL)
                    .getUserAsync(username)
            _user.postValue(User.fromUserResponse(getUserDetailAsync))
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}