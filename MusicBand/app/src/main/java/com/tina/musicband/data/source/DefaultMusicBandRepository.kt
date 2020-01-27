package com.tina.musicband.data.source

import com.tina.musicband.data.Comments
import com.tina.musicband.data.Posts
import com.tina.musicband.data.Result

class DefaultMusicBandRepository(private val remoteDataSource: MusicBandDataSource,
                                 private val localDataSource: MusicBandDataSource)
    : MusicBandRepository {
    override suspend fun getPosts(): Result<List<Posts>> {
        return remoteDataSource.getPosts()
    }

    override suspend fun getComments(): Result<List<Comments>> {
        return remoteDataSource.getComments()
    }


}