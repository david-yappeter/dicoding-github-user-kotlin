package myplayground.example.githubsearch.activities.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import myplayground.example.githubsearch.database.FavouriteUserEntity
import myplayground.example.githubsearch.repository.FavouriteUsersRepository

class FavouriteUsersViewModel(private val mFavouriteUsersRepository: FavouriteUsersRepository) :
    ViewModel() {
    fun fetchAll(): LiveData<List<FavouriteUserEntity>> = mFavouriteUsersRepository.fetchAll()
}