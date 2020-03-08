package com.tina.musicband.data.source

import android.net.Uri
import com.tina.musicband.data.*
import com.tina.musicband.main.PostSealedItem

class DefaultMusicBandRepository(private val remoteDataSource: MusicBandDataSource,
                                 private val localDataSource: MusicBandDataSource)
    : MusicBandRepository {

    override fun detectProfileDataChange(callbackHandler: ((User) -> Unit)?) {
        return remoteDataSource.detectProfileDataChange(callbackHandler)
    }

    override fun getFollowings(callbackHandler: ((List<Following>) -> Unit)?) {
        return remoteDataSource.getFollowings(callbackHandler)
    }

    override fun getFollowers(callbackHandler: ((List<Follower>) -> Unit)?) {
        return remoteDataSource.getFollowers(callbackHandler)
    }

    override fun retrieveFollowingsCount(userID: String, callbackHandler: ((Int) -> Unit)?) {
        return remoteDataSource.retrieveFollowingsCount(userID, callbackHandler)
    }

    override fun retrieveFollowersCount(userID: String, callbackHandler: ((Int) -> Unit)?) {
        return remoteDataSource.retrieveFollowersCount(userID, callbackHandler)
    }

    override suspend fun uploadImage(imageUri: Uri): Result<String> {
        return remoteDataSource.uploadImage(imageUri)
    }

    override suspend fun publishPost(post: Posts): Result<Boolean> {
        return remoteDataSource.publishPost(post)
    }

    override suspend fun publishMusic(posts: Posts): Result<Boolean> {
        return remoteDataSource.publishMusic(posts)
    }

    override suspend fun changeAvatarAndBackground(user: User): Result<Boolean> {
        return remoteDataSource.changeAvatarAndBackground(user)
    }

    override suspend fun retrieveUsersData(userID: String): Result<User> {
        return remoteDataSource.retrieveUsersData(userID)
    }

    override suspend fun retrievePostsCount(userID: String): Result<Int> {
        return remoteDataSource.retrievePostsCount(userID)
    }

    override suspend fun checkIfUserIsFollowed(userID: String): Result<Boolean> {
        return  remoteDataSource.checkIfUserIsFollowed(userID)
    }

    override suspend fun followUser(userID: String): Result<Boolean> {
        return remoteDataSource.followUser(userID)
    }

    override suspend fun unfollowUser(userID: String): Result<Boolean> {
        return remoteDataSource.unfollowUser(userID)
    }

    override suspend fun updateUsersData(data: Map<String, String?>): Result<Boolean> {
        return remoteDataSource.updateUsersData(data)
    }

    override suspend fun updateBackgroundAndAvatar(): Result<User> {
        return remoteDataSource.updateBackgroundAndAvatar()
    }

    override suspend fun retrieveUsersPosts(userID: String): Result<List<PostSealedItem>> {
        return remoteDataSource.retrieveUsersPosts(userID)
    }

    override suspend fun retrieveUsersSongs(userID: String): Result<List<Songs>> {
        return remoteDataSource.retrieveUsersSongs(userID)
    }

    override suspend fun getAllSongs(): Result<List<Songs>> {
        return remoteDataSource.getAllSongs()
    }

    override suspend fun retrieveUsersAvatar(userID: String): Result<User> {
        return remoteDataSource.retrieveUsersAvatar(userID)
    }

}