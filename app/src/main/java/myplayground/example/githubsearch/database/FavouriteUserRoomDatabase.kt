package myplayground.example.githubsearch.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavouriteUserEntity::class], version = 1)
abstract class FavouriteUserRoomDatabase : RoomDatabase() {
    abstract fun favouriteUserDao(): FavouriteUserDao

    companion object {
        @Volatile
        private var INSTANCE: FavouriteUserRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavouriteUserRoomDatabase {
            if (INSTANCE == null) {
                synchronized(FavouriteUserRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext, FavouriteUserRoomDatabase::class.java, "db"
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE as FavouriteUserRoomDatabase
        }
    }
}