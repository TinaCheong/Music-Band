package com.tina.musicband

import com.tina.musicband.ext.createTime
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun checkSongsCreatedTime(){
        assertEquals("06:30", 390000.createTime())
    }

    @Test
    fun checkSongCreatedTime2(){
        assertEquals("05:24", 324000.createTime())

    }
}
