package com.saeware.github.config

object Config {
    init { System.loadLibrary("github") }

    external fun apiKey(): String

    external fun baseUrl(): String
}