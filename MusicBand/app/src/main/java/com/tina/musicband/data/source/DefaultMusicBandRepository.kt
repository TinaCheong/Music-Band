package com.tina.musicband.data.source

import android.net.Uri
import com.tina.musicband.data.*
import com.tina.musicband.main.PostSealedItem

class DefaultMusicBandRepository(private val remoteDataSource: MusicBandDataSource,
                                 private val localDataSource: MusicBandDataSource)
    : MusicBandRepository {

    override suspend fun uploadImage(imageUri: Uri): Result<String> {
        return remoteDataSource.uploadImage(imageUri)
    }

    override suspend fun publishPost(post: Posts): Result<Boolean> {
        return remoteDataSource.publishPost(post)
    }

    override suspend fun publishMusic(posts: Posts): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun changeAvatarAndBackground(user: User): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getFollowers(): Result<List<Follower>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getFollowings(): Result<List<Following>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun fetchOtherUsersDataFromSong(userID: String): Result<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun retrievePostsCount(userID: String): Result<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun retrieveFollowingsCount(userID: String): Result<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun retrieveFollowersCount(userID: String): Result<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun checkIfUserIsFollowed(userID: String): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun followUser(userID: String): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun unfollowUser(userID: String): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updateUsersData(data: Map<String, String>): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updateBackgroundAndAvatar(): Result<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getProfileData(): Result<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun retrieveUsersPosts(userID: String): Result<List<PostSealedItem>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun retrieveUsersSongs(userID: String): Result<List<Songs>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAllSongs(): Result<List<Songs>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun retrieveUsersAvatar(userID: String): Result<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}