package com.tina.musicband.data.source

import com.tina.musicband.data.*

interface MusicBandDataSource {

    suspend fun publishEvents(posts: Posts): Result<Boolean>

    suspend fun getComments(): Result<List<Comments>>

    suspend fun getSongs(): Result<List<Songs>>

    suspend fun getUserData(): Result<User>

}