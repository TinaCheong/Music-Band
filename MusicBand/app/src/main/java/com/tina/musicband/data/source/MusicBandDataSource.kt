package com.tina.musicband.data.source

import android.net.Uri
import com.tina.musicband.data.*
import com.tina.musicband.main.PostSealedItem

interface MusicBandDataSource {

    suspend fun publishPost(post: Posts): Result<Boolean>

    suspend fun publishMusic(posts: Posts): Result<Boolean>

    suspend fun uploadImage(imageUri: Uri): Result<String>

    suspend fun changeAvatarAndBackground(user: User): Result<Boolean>

    fun getFollowers(callbackHandler:((List<Follower>)->Unit)?)

    fun getFollowings(callbackHandler:((List<Following>)->Unit)?)

    suspend fun retrieveUsersData(userID: String): Result<User>

    suspend fun retrievePostsCount(userID: String): Result<Int>

    fun retrieveFollowingsCount(userID: String, callbackHandler:((Int)->Unit)?)

    fun retrieveFollowersCount(userID: String, callbackHandler:((Int)->Unit)?)

    suspend fun checkIfUserIsFollowed(userID: String): Result<Boolean>

    suspend fun followUser(userID: String): Result<Boolean>

    suspend fun unfollowUser(userID: String): Result<Boolean>

    suspend fun updateUsersData(data: Map<String, String?>): Result<Boolean>

    suspend fun updateBackgroundAndAvatar(): Result<User>

    fun detectProfileDataChange(callbackHandler:((User)->Unit)?)

    suspend fun retrieveUsersPosts(userID: String): Result<List<PostSealedItem>>

    suspend fun retrieveUsersSongs(userID: String): Result<List<Songs>>

    suspend fun getAllSongs(): Result<List<Songs>>

    suspend fun retrieveUsersAvatar(userID: String): Result<User>
}