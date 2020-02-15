package com.tina.musicband

import android.app.Application
import com.tina.musicband.data.User
import com.tina.musicband.data.source.MusicBandRepository
import com.tina.musicband.util.ServiceLocator
import kotlin.properties.Delegates

//An application that lazily provides a repository. Note that this Service Locator pattern is
//used to simplify the sample. Consider a Dependency Injection framework

class MusicBandApplication : Application() {

    // Depends on the flavor,
    val repository: MusicBandRepository
        get() = ServiceLocator.provideRepository(this)
    val user = User()

    companion object {
        var instance: MusicBandApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }


}