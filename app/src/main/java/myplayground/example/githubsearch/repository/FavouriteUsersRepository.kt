package myplayground.example.githubsearch.repository

import androidx.lifecycle.LiveData
import myplayground.example.githubsearch.database.FavouriteUserDao
import myplayground.example.githubsearch.database.FavouriteUserEntity
import myplayground.example.githubsearch.util.AppExecutors

class FavouriteUsersRepository(
    private val favouriteUserDao: FavouriteUserDao,
    private val appExecutor: AppExecutors
) {

    fun fetchAll(): LiveData<List<FavouriteUserEntity>> = favouriteUserDao.fetchAll()

    fun insert(favouriteUser: FavouriteUserEntity) {
        appExecutor.diskIO.execute { favouriteUserDao.insert(favouriteUser) }
    }

    fun delete(favouriteUser: FavouriteUserEntity) {
        appExecutor.diskIO.execute { favouriteUserDao.delete(favouriteUser) }
    }


    companion object {
        @Volatile
        private var instance: FavouriteUsersRepository? = null
        fun getInstance(
            favouriteUserDao: FavouriteUserDao,
            appExecutor: AppExecutors,
        ): FavouriteUsersRepository =
            instance ?: synchronized(this) {
                instance ?: FavouriteUsersRepository(favouriteUserDao, appExecutor)
            }.also { instance = it }
    }
}