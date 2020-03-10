package com.tina.musicband.data.source.local

import android.content.Context
import android.net.Uri
import com.tina.musicband.data.*
import com.tina.musicband.data.source.MusicBandDataSource
import com.tina.musicband.main.PostSealedItem

class MusicBandLocalDataSource (val context: Context) : MusicBandDataSource{

    override suspend fun retrieveAllUsersData(userIDs: List<String>): Result<List<User>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun retrievePostsDataInstantly(userIDs: MutableList<String>, callbackHandler: ((List<Posts>) -> Unit)?
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getUsersFollowingsID(): Result<MutableList<String>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun publishSong(song: Songs): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun publishMusicPost(post: Posts): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun uploadSong(songUri: Uri): Result<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun detectProfileDataChange(callbackHandler: ((User) -> Unit)?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFollowers(callbackHandler: ((List<Follower>) -> Unit)?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFollowings(callbackHandler: ((List<Following>) -> Unit)?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun retrieveFollowingsCount(userID: String, callbackHandler: ((Int) -> Unit)?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun retrieveFollowersCount(userID: String, callbackHandler: ((Int) -> Unit)?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun uploadImage(imageUri: Uri): Result<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun publishEventPost(post: Posts): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun changeAvatarAndBackground(user: User): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun retrieveUsersData(userID: String): Result<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun retrievePostsCount(userID: String): Result<Int> {
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

    override suspend fun updateUsersData(data: Map<String, String?>): Result<Boolean> {
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

}