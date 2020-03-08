package com.tina.musicband.data.source.remote

import android.icu.util.Calendar
import android.net.Uri
import android.os.UserManager
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.tina.musicband.R
import com.tina.musicband.data.*
import com.tina.musicband.data.source.MusicBandDataSource
import com.tina.musicband.follower.FollowerAdapter
import com.tina.musicband.main.POST_TYPES
import com.tina.musicband.main.PostSealedItem
import com.tina.musicband.util.Logger
import java.util.*
import kotlin.concurrent.timerTask
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
            val storageReference = FirebaseStorage.getInstance()
                .reference.child("images/" + UUID.randomUUID().toString())

            storageReference.putFile(imageUri).addOnSuccessListener {
                val uri = it.metadata?.reference?.downloadUrl
                uri?.addOnSuccessListener {

                    continuation.resume(Result.Success(it.toString()))
                }

            }.addOnFailureListener {

                Logger.w("Upload Fail")

            }
        }

    override suspend fun publishPost(post: Posts): Result<Boolean> = suspendCoroutine { continuation ->
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

    override suspend fun changeAvatarAndBackground(user: User): Result<Boolean> = suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance().collection("users")
                .document(com.tina.musicband.login.UserManager.userToken.toString())
                .update("avatar", user.avatar, "background", user.background)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        continuation.resume(Result.Success(true))

                    } else {
                        task.exception?.let {

                            Logger.w("[${this::class.simpleName}] Error changing avatar or background. ${it.message}")
                            continuation.resume(Result.Error(it))
                        }
                    }
                }
        }

    override fun getFollowers(callbackHandler:((List<Follower>)->Unit)?) {
        FirebaseFirestore.getInstance().collection(PATH_USERS)
            .document(com.tina.musicband.login.UserManager.userToken.toString())
            .collection(COLLECTION_FOLLOWER)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->

                if (querySnapshot != null) {

                    val list = querySnapshot.toObjects(Follower::class.java)

                    callbackHandler?.invoke(list)
                }
            }
    }

    override fun getFollowings(callbackHandler:((List<Following>)->Unit)?) {
            FirebaseFirestore.getInstance().collection(PATH_USERS)
                .document(com.tina.musicband.login.UserManager.userToken.toString())
                .collection(COLLECTION_FOLLOWING)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->

                    if (querySnapshot != null) {

                        val list = querySnapshot.toObjects(Following::class.java)

                        callbackHandler?.invoke(list)

                    }
                }
        }

    override suspend fun retrieveUsersData(userID: String): Result<User> = suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance().collection(PATH_USERS)
                .document(userID)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val user = task.result?.toObject(User::class.java)

                        user?.let {

                            continuation.resume(Result.Success(it))
                        }

                    } else {

                        task.exception?.let {

                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                        }
                    }
                }
        }

    override suspend fun retrievePostsCount(userID: String): Result<Int> = suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance().collection(PATH_POSTS)
                .whereEqualTo("userId", userID)
                .get()
                .addOnCompleteListener { task ->

                    var count = 0

                    if (task.isSuccessful) {

                        for (document in task.result!!) {
                            count++

                        }
                        continuation.resume(Result.Success(count))

                    } else {

                        task.exception?.let {

                            Logger.w("[${this::class.simpleName}] Error getting Post. ${it.message}")
                            continuation.resume(Result.Error(it))
                        }

                    }
                }
    }

    override fun retrieveFollowingsCount(userID: String, callbackHandler:((Int)->Unit)?) {
        FirebaseFirestore.getInstance().collection(PATH_USERS)
            .document(userID)
            .collection(COLLECTION_FOLLOWING)
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                var count = 0
                if (documentSnapshot != null) {

                    val followings = documentSnapshot.toObjects(Following::class.java)

                    for (following in followings) {

                        count++
                    }
                }
                callbackHandler?.invoke(count)
            }
    }

    override fun retrieveFollowersCount(userID: String, callbackHandler:((Int)->Unit)?) {
            FirebaseFirestore.getInstance().collection(PATH_USERS)
                .document(userID)
                .collection(COLLECTION_FOLLOWER)
                .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->

                    if (documentSnapshot != null) {

                        var count = 0

                        val followers = documentSnapshot.toObjects(Following::class.java)

                        for (follower in followers) {

                            count++

                        }
                        callbackHandler?.invoke(count)
                    }
                }
        }

    override suspend fun checkIfUserIsFollowed(userID: String): Result<Boolean> =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance().collection(PATH_USERS)
                .document(userID)
                .collection(COLLECTION_FOLLOWER)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val followers = task.result!!.toObjects(Follower::class.java)

                        for (follower in followers) {

                            if (follower.userId == com.tina.musicband.login.UserManager.userToken.toString()) {

                                continuation.resume(Result.Success(true))
                                break
                            }

                        }
                    } else {

                        task.exception?.let {

                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                        }

                    }

                }
        }

    override suspend fun followUser(userID: String): Result<Boolean> =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance().collection(PATH_USERS)
                .document(com.tina.musicband.login.UserManager.userToken.toString())
                .collection(COLLECTION_FOLLOWING)
                .document(userID)
                .set(mapOf("userId" to userID))
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        FirebaseFirestore.getInstance().collection(PATH_USERS)
                            .document(userID)
                            .collection(COLLECTION_FOLLOWER)
                            .document(com.tina.musicband.login.UserManager.userToken.toString())
                            .set(mapOf("userId" to com.tina.musicband.login.UserManager.userToken.toString()))
                            .addOnCompleteListener {

                                if (it.isSuccessful) {

                                    continuation.resume(Result.Success(true))


                                } else {

                                    it.exception?.let {

                                        Logger.w("[${this::class.simpleName}] Error adding follower ${it.message}")
                                        continuation.resume(Result.Error(it))
                                    }

                                }

                            }
                    } else {

                        task.exception?.let {

                            Logger.w("[${this::class.simpleName}] Error adding follower ${it.message}")
                            continuation.resume(Result.Error(it))
                        }

                    }

                }
        }


    override suspend fun unfollowUser(userID: String): Result<Boolean> =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance().collection(PATH_USERS)
                .document(com.tina.musicband.login.UserManager.userToken.toString())
                .collection(COLLECTION_FOLLOWING)
                .document(userID)
                .delete()
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        FirebaseFirestore.getInstance().collection(PATH_USERS)
                            .document(userID)
                            .collection(COLLECTION_FOLLOWER)
                            .document(com.tina.musicband.login.UserManager.userToken.toString())
                            .delete()
                            .addOnCompleteListener {

                                if (it.isSuccessful) {

                                    continuation.resume(Result.Success(false))

                                } else {

                                    it.exception?.let {

                                        Logger.w("[${this::class.simpleName}] Error adding follower ${it.message}")
                                        continuation.resume(Result.Error(it))
                                    }

                                }

                            }
                    } else {

                        task.exception?.let {

                            Logger.w("[${this::class.simpleName}] Error getting songs ${it.message}")
                            continuation.resume(Result.Error(it))
                        }

                    }

                }
        }

    override suspend fun updateUsersData(data: Map<String, String?>): Result<Boolean> =
        suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance().collection("users")
            .document(com.tina.musicband.login.UserManager.userToken.toString())
            .update(data)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    continuation.resume(Result.Success(true))

                } else {

                    task.exception?.let {

                        Logger.w("[${this::class.simpleName}] Fail to update data ${it.message}")
                        continuation.resume(Result.Error(it))

                    }
                }

            }
    }

    override suspend fun updateBackgroundAndAvatar(): Result<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun detectProfileDataChange(callbackHandler: ((User) -> Unit)?) {

        FirebaseFirestore.getInstance().collection(PATH_USERS)
            .document(com.tina.musicband.login.UserManager.userToken.toString())
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->

                if (documentSnapshot != null) {

                    val user = documentSnapshot.toObject(User::class.java)

                    callbackHandler?.invoke(user!!)
                }
            }
    }

    override suspend fun retrieveUsersPosts(userID: String): Result<List<PostSealedItem>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance().collection("posts")
            .whereEqualTo("userId", userID)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val posts = task.result?.toObjects(Posts::class.java)

                    val items = mutableListOf<PostSealedItem>()

                    posts?.forEach {
                        when (it.type) {
                            POST_TYPES.MUSIC.value -> {
                                items.add(PostSealedItem.MusicItem(it))
                            }

                            POST_TYPES.EVENT.value -> {
                                items.add(PostSealedItem.EventItem(it))
                            }
                        }
                    }

                    continuation.resume(Result.Success(items))

                } else {

                    task.exception?.let {

                        Logger.w("[${this::class.simpleName}] Error getting songs ${it.message}")
                        continuation.resume(Result.Error(it))
                    }
                }
            }
    }



    override suspend fun retrieveUsersSongs(userID: String): Result<List<Songs>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance().collection(PATH_SONGS)
            .whereEqualTo("userId", userID)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val songs = task.result!!.toObjects(Songs::class.java)

                    continuation.resume(Result.Success(songs))

                } else {

                    task.exception?.let {

                        Logger.w("[${this::class.simpleName}] Error getting songs ${it.message}")
                        continuation.resume(Result.Error(it))
                    }
                }

            }
    }

    override suspend fun getAllSongs(): Result<List<Songs>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance().collection(PATH_SONGS)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val songs = task.result?.toObjects(Songs::class.java)

                    continuation.resume(Result.Success(songs!!))

                } else {

                    task.exception?.let {

                        Logger.w("[${this::class.simpleName}] Error getting songs ${it.message}")
                        continuation.resume(Result.Error(it))
                    }

                }
            }
    }


    override suspend fun retrieveUsersAvatar(userID: String): Result<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}