package myplayground.example.githubsearch.activities.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import myplayground.example.githubsearch.repository.FavouriteUsersRepository

@Suppress("UNCHECKED_CAST")
class UserDetailViewModelFactory(private val username: String, private val favouriteUsersRepository: FavouriteUsersRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserDetailViewModel::class.java)) {
            return UserDetailViewModel(username, favouriteUsersRepository) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}