package com.tina.musicband.data.source

import com.tina.musicband.data.*

class DefaultMusicBandRepository(private val remoteDataSource: MusicBandDataSource,
                                 private val localDataSource: MusicBandDataSource)
    : MusicBandRepository {
    override suspend fun publishEvents(posts: Posts): Result<Boolean> {
        return remoteDataSource.publishEvents(posts)
    }

    override suspend fun getComments(): Result<List<Comments>> {
        return remoteDataSource.getComments()
    }

    override suspend fun getSongs(): Result<List<Songs>> {
        return remoteDataSource.getSongs()
    }

    override suspend fun getUserData(): Result<User> {
        return remoteDataSource.getUserData()
    }


}