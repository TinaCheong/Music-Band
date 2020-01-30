package com.tina.musicband.data.source

import com.tina.musicband.data.Comments
import com.tina.musicband.data.Posts
import com.tina.musicband.data.Result

interface MusicBandDataSource {

    suspend fun publishEvents(posts: Posts): Result<Boolean>

    suspend fun getComments(): Result<List<Comments>>

}