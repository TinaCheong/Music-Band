package com.tina.musicband.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.tina.musicband.data.source.DefaultMusicBandRepository
import com.tina.musicband.data.source.MusicBandDataSource
import com.tina.musicband.data.source.MusicBandRepository
import com.tina.musicband.data.source.local.MusicBandLocalDataSource
import com.tina.musicband.data.source.remote.MusicBandRemoteDataSource

//A Service Locator for the [MusicBandRepository]

object ServiceLocator {

    @Volatile
    var repository: MusicBandRepository? = null
        @VisibleForTesting set

    fun provideRepository(context: Context): MusicBandRepository {
        synchronized(this) {
            return repository
                ?: repository
                ?: createPublisherRepository(context)
        }
    }

    private fun createPublisherRepository(context: Context): MusicBandRepository {
        return DefaultMusicBandRepository(
            MusicBandRemoteDataSource,
            createLocalDataSource(context)
        )
    }

    private fun createLocalDataSource(context: Context): MusicBandDataSource {
        return MusicBandLocalDataSource(context)
    }

}