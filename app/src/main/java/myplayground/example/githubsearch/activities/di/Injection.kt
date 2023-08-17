package myplayground.example.githubsearch.activities.di

import android.content.Context
import myplayground.example.githubsearch.database.FavouriteUserRoomDatabase
import myplayground.example.githubsearch.repository.FavouriteUsersRepository
import myplayground.example.githubsearch.util.AppExecutors

object Injection {
    fun provideFavouriteUsersRepository(context: Context) : FavouriteUsersRepository {
        val database = FavouriteUserRoomDatabase.getDatabase(context)
        val dao = database.favouriteUserDao()
        val appExecutors = AppExecutors()
        return FavouriteUsersRepository.getInstance(dao, appExecutors)
    }
}