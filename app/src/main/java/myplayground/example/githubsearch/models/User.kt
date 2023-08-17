package myplayground.example.githubsearch.models

import myplayground.example.githubsearch.database.FavouriteUserEntity

data class User(
    val id: String,
    val login: String,
    val avatar_url: String,
    val name: String?,
    val followers: Int?,
    val following: Int?,
    val blog: String?,
    val location: String?,
    val bio: String?,
) {
    companion object {
        fun fromUserResponse(resp: UserResponse): User {
            return User(
                id = resp.id,
                login = resp.login,
                avatar_url = resp.avatar_url,
                name = resp.name,
                followers = resp.followers,
                following = resp.following,
                blog = resp.blog,
                location = resp.location,
                bio = resp.bio,
            )
        }

        fun fromFavouriteUserEntity(resp: FavouriteUserEntity): User {
            return User(
                id = resp.id,
                login = resp.login ?: "",
                avatar_url = resp.avatar_url ?: "",
                name = "",
                followers = 0,
                following = 0,
                blog = "",
                location = "",
                bio = "",
            )
        }
    }
}
