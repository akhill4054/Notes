package com.internshala.notes

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun hypothesis_isCorrect() {
        assertEquals(Sub().javaClass.simpleName, getTag(Sub()))
    }

    private fun getTag(obj: Base): String = obj.javaClass.simpleName

    open class Base
    class Sub : Base()
}