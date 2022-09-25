package com.saeware.github.utils

import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResource {
    private const val RESOURCE_KEY = "GLOBAL"
    private val countingIdlingResource = CountingIdlingResource(RESOURCE_KEY)
    val idlingResource: IdlingResource get() = countingIdlingResource

    fun increment() { countingIdlingResource.increment() }

    fun decrement() { countingIdlingResource.decrement() }
}