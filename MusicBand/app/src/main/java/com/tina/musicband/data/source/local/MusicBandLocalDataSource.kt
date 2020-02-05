package com.tina.musicband.data.source.local

import android.content.Context
import com.tina.musicband.data.*
import com.tina.musicband.data.source.MusicBandDataSource

class MusicBandLocalDataSource (val context: Context) : MusicBandDataSource{

    override suspend fun publishEvents(posts: Posts): Result<Boolean> {
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