package myplayground.example.githubsearch.activities.favourite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import myplayground.example.githubsearch.database.FavouriteUserEntity
import myplayground.example.githubsearch.repository.FavouriteUsersRepository

class FavouriteUsersViewModel(private val mFavouriteUsersRepository: FavouriteUsersRepository) :
    ViewModel() {
//    private val _favouriteUsers = MutableLiveData<List<FavouriteUserEntity>>(listOf())
//    val favouriteUsers: LiveData<List<FavouriteUserEntity>> get() = _favouriteUsers

    fun fetchAll() : LiveData<List<FavouriteUserEntity>> {
        // Fetch the data from the repository
        val fetchedFavouriteUsers = mFavouriteUsersRepository.fetchAll()
        // Update the MutableLiveData with the new data

        Log.i("INFOOOOOOO", fetchedFavouriteUsers.value.toString())

//        _favouriteUsers.postValue(fetchedFavouriteUsers.value)

        return fetchedFavouriteUsers
    }
}