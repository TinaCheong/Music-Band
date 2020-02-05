package com.tina.musicband.data.source.remote

import android.icu.util.Calendar
import com.google.firebase.firestore.FirebaseFirestore
import com.tina.musicband.data.*
import com.tina.musicband.data.source.MusicBandDataSource

//Implementation of the MusicBand source that from network

object MusicBandRemoteDataSource : MusicBandDataSource {

    private const val PATH_ARTICLES = "posts"

    override suspend fun publishEvents(posts: Posts): Result<Boolean> {

//        val post = FirebaseFirestore.getInstance().collection(PATH_ARTICLES)
//        val document = post.document()
//
//        posts.postId = document.id

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.


    }

    override suspend fun getComments(): Result<List<Comments>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getSongs(): Result<List<Songs>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getUserData(): Result<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}