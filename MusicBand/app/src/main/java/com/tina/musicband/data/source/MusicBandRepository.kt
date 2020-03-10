package com.tina.musicband.data.source

import android.net.Uri
import com.facebook.CallbackManager
import com.tina.musicband.data.*
import com.tina.musicband.main.PostSealedItem

//Interface to the Publisher layers

interface MusicBandRepository {

    suspend fun publishEventPost(post: Posts): Result<Boolean>

    suspend fun publishMusicPost(post: Posts): Result<Boolean>

    suspend fun publishSong(song: Songs): Result<Boolean>

    suspend fun uploadSong(songUri: Uri): Result<String>

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

    fun retrievePostsDataInstantly(userID: String, callbackHandler:((List<Posts>)->Unit)?)

    fun detectProfileDataChange(callbackHandler:((User)->Unit)?)

    suspend fun retrieveUsersPosts(userID: String): Result<List<PostSealedItem>>

    suspend fun retrieveUsersSongs(userID: String): Result<List<Songs>>

    suspend fun getAllSongs(): Result<List<Songs>>

    suspend fun retrieveUsersFollowings(userID: String): Result<List<Following>>

}