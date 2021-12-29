package com.home.opencarshare

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
    fun addNewItemToList_isCorrect() {
        val list = emptyList<String>()
        val newList = list + "new_item"
        assertEquals(1, newList.size)
    }
}