package com.tina.musicband.data.source.remote

import android.icu.util.Calendar
import android.net.Uri
import android.os.UserManager
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.tina.musicband.data.*
import com.tina.musicband.data.source.MusicBandDataSource
import com.tina.musicband.main.PostSealedItem
import com.tina.musicband.util.Logger
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

//Implementation of the MusicBand source that from network

private const val PATH_POSTS = "posts"
private const val PATH_SONGS = "songs"
private const val PATH_USERS = "users"
private const val COLLECTION_COMMENTS = "comments"
private const val COLLECTION_LIKE = "like"
private const val COLLECTION_FOLLOWER = "follower"
private const val COLLECTION_FOLLOWING = "following"

object MusicBandRemoteDataSource : MusicBandDataSource {

    override suspend fun uploadImage(imageUri: Uri): Result<String> = suspendCoroutine { continuation ->
        var storageReference = FirebaseStorage.getInstance().reference.child("images/" + UUID.randomUUID().toString())

        storageReference.putFile(imageUri).addOnSuccessListener {
            val uri = it.metadata?.reference?.downloadUrl
            uri?.addOnSuccessListener {

                continuation.resume(Result.Success(it.toString()))
            }

        }.addOnFailureListener{

            Logger.w("Upload Fail")

        }
    }

    override suspend fun publishPost(post: Posts): Result<Boolean>  = suspendCoroutine { continuation ->
        val posts = FirebaseFirestore.getInstance().collection(PATH_POSTS)
        val document = posts.document()

        post.postId = document.id
        post.createdTime = java.util.Calendar.getInstance().timeInMillis
        post.userName = com.tina.musicband.login.UserManager.userName
        post.userId = com.tina.musicband.login.UserManager.userToken.toString()

        document
            .set(post)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Logger.i("PublishPost: $post")

                    continuation.resume(Result.Success(true))
                } else {
                    task.exception?.let {

                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                    }
                }



            }

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