package myplayground.example.githubsearch.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavouriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favouriteUser: FavouriteUserEntity)

    @Delete
    fun delete(favouriteUser: FavouriteUserEntity)

    @Query("SELECT * FROM favourite_users")
    fun fetchAll(): LiveData<List<FavouriteUserEntity>>
}