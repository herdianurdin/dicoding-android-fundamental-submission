package com.saeware.github.ui.activity

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.saeware.github.R
import com.saeware.github.adapter.UserAdapter
import com.saeware.github.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {
    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
        ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }

    @Test
    fun checkMainComponent() {
        onView(withId(R.id.tv_result_count)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_user)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_user)).perform(
            scrollToPosition<UserAdapter.ListViewHolder>(29)
        )
    }

    @Test
    fun toggleDarkMode() {
        onView(withId(R.id.settings_action)).check(matches(isDisplayed()))
        onView(withId(R.id.settings_action)).perform(click())

        onView(withId(R.id.switch_dark_mode)).check(matches(isDisplayed()))
        onView(withId(R.id.switch_dark_mode)).perform(click())

        pressBack()
    }

    @Test
    fun selectFirstUserAndShowIt() {
        onView(withId(R.id.tv_result_count)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_user)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_user)).perform(
            actionOnItemAtPosition<UserAdapter.ListViewHolder>(0, click())
        )

        onView(withId(R.id.app_bar_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.tabs)).check(matches(isDisplayed()))
        onView(withId(R.id.nested_scroll_view)).check(matches(isDisplayed()))
        onView(withId(R.id.view_pager)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_favorite)).check(matches(isDisplayed()))
    }

    @Test
    fun toggleFavoriteUser() {
        onView(withId(R.id.tv_result_count)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_user)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_user)).perform(
            actionOnItemAtPosition<UserAdapter.ListViewHolder>(0, click())
        )

        onView(withId(R.id.app_bar_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.tabs)).check(matches(isDisplayed()))
        onView(withId(R.id.nested_scroll_view)).check(matches(isDisplayed()))
        onView(withId(R.id.view_pager)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_favorite)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_favorite)).perform(click())

        pressBack()

        onView(withId(R.id.favorite_action)).check(matches(isDisplayed()))
        onView(withId(R.id.favorite_action)).perform(click())
    }
}