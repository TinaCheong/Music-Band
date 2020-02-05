package com.tina.musicband.data.source

import com.tina.musicband.data.*

//Interface to the Publisher layers

interface MusicBandRepository {

    suspend fun publishEvents(posts: Posts): Result<Boolean>

    suspend fun getComments(): Result<List<Comments>>

    suspend fun getSongs(): Result<List<Songs>>

    suspend fun getUserData(): Result<User>
}