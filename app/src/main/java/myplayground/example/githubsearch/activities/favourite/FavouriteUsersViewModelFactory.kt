package myplayground.example.githubsearch.activities.favourite

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import myplayground.example.githubsearch.activities.di.Injection
import myplayground.example.githubsearch.repository.FavouriteUsersRepository


class FavouriteUsersViewModelFactory private constructor(private val favouriteUsersRepository: FavouriteUsersRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavouriteUsersViewModel::class.java)) {
            return FavouriteUsersViewModel(favouriteUsersRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: FavouriteUsersViewModelFactory? = null
        fun getInstance(context: Context): FavouriteUsersViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: FavouriteUsersViewModelFactory(Injection.provideFavouriteUsersRepository(context))
            }.also { instance = it }
    }
}