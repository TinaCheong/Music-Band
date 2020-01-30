package com.tina.musicband.data.source.local

import android.content.Context
import com.tina.musicband.data.Comments
import com.tina.musicband.data.Posts
import com.tina.musicband.data.Result
import com.tina.musicband.data.source.MusicBandDataSource

class MusicBandLocalDataSource (val context: Context) : MusicBandDataSource{

    override suspend fun publishEvents(posts: Posts): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getComments(): Result<List<Comments>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}