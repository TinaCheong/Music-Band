package com.tina.musicband.data.source.remote

import com.tina.musicband.data.Comments
import com.tina.musicband.data.Posts
import com.tina.musicband.data.Result
import com.tina.musicband.data.source.MusicBandDataSource

//Implementation of the MusicBand source that from network

object MusicBandRemoteDataSource : MusicBandDataSource {
    override suspend fun getPosts(): Result<List<Posts>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getComments(): Result<List<Comments>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}