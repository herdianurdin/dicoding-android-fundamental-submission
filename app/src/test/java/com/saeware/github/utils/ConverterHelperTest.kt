package com.saeware.github.utils

import org.junit.Assert.*
import org.junit.Test

class ConverterHelperTest {
    @Test
    fun abbreviationNum() {
        assertEquals("5", ConverterHelper.abbreviationNum(5))
        assertEquals("5K", ConverterHelper.abbreviationNum(5_000))
        assertEquals("5M", ConverterHelper.abbreviationNum(5_000_000))
        assertEquals("5B", ConverterHelper.abbreviationNum(5_000_000_000))
    }
}