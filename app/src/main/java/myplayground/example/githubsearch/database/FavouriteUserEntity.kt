package myplayground.example.githubsearch.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "favourite_users"
)

@Parcelize
data class FavouriteUserEntity (
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int? = null,
    @ColumnInfo(name = "login")
    var login: String? = null,
    @ColumnInfo(name = "avatar_url")
    var avatar_url: String? = null,
): Parcelable
