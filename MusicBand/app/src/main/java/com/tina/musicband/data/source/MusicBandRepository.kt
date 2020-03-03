package com.tina.musicband.data.source

import com.facebook.CallbackManager
import com.tina.musicband.data.*
import com.tina.musicband.main.PostSealedItem

//Interface to the Publisher layers

interface MusicBandRepository {

    suspend fun publishEvents(posts: Posts): Result<Boolean>

    suspend fun publishMusic(posts: Posts): Result<Boolean>

    suspend fun changeAvatarAndBackground(user: User): Result<Boolean>

    suspend fun getFollowers(): Result<List<Follower>>

    suspend fun getFollowings(): Result<List<Following>>

    suspend fun fetchOtherUsersDataFromSong(userID: String): Result<User>

    suspend fun retrievePostsCount(userID: String): Result<Int>

    suspend fun retrieveFollowingsCount(userID: String): Result<Int>

    suspend fun retrieveFollowersCount(userID: String): Result<Int>

    suspend fun checkIfUserIsFollowed(userID: String): Result<Boolean>

    suspend fun followUser(userID: String): Result<Boolean>

    suspend fun unfollowUser(userID: String): Result<Boolean>

    suspend fun updateUsersData(data: Map<String, String>): Result<Boolean>

    suspend fun updateBackgroundAndAvatar(): Result<User>

    suspend fun getProfileData(): Result<User>

    suspend fun retrieveUsersPosts(userID: String): Result<List<PostSealedItem>>

    suspend fun retrieveUsersSongs(userID: String): Result<List<Songs>>

    suspend fun getAllSongs(): Result<List<Songs>>

    suspend fun retrieveUsersAvatar(userID: String): Result<User>

}