package com.tina.musicband.data.source

import com.tina.musicband.data.Comments
import com.tina.musicband.data.Posts
import com.tina.musicband.data.Result

interface MusicBandDataSource {

    suspend fun getPosts(): Result<List<Posts>>

    suspend fun getComments(): Result<List<Comments>>

}