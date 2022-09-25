package com.saeware.github.utils

object ConverterHelper {
    fun abbreviationNum(num: Long): String {
        return when (num) {
            in 0..999 -> num.toString()
            in 1_000..999999 -> "${num / 1_000}K"
            in 1_000_000..9999999 -> "${num / 1_000_000}M"
            else -> "${num / 1000_000_000}B"
        }
    }
}