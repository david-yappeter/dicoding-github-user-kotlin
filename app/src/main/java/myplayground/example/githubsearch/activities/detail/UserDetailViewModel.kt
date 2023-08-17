package myplayground.example.githubsearch.activities.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import myplayground.example.githubsearch.database.FavouriteUserEntity
import myplayground.example.githubsearch.models.User
import myplayground.example.githubsearch.network.GithubService
import myplayground.example.githubsearch.network.NetworkConfig
import myplayground.example.githubsearch.repository.FavouriteUsersRepository

class UserDetailViewModel(
    private val username: String,
    private val favouriteUsersRepository: FavouriteUsersRepository
) : ViewModel() {
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        viewModelScope.launch {
            getUserDetail(username)
        }
    }


    fun insertFavouriteUser(favEntity: FavouriteUserEntity) {
        favouriteUsersRepository.insert(favEntity)
    }

    fun deleteFavouriteUser(favEntity: FavouriteUserEntity) {
        favouriteUsersRepository.delete(favEntity)
    }

    fun getFavouriteUserById(id: String): LiveData<FavouriteUserEntity?> =
        favouriteUsersRepository.getById(id)

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