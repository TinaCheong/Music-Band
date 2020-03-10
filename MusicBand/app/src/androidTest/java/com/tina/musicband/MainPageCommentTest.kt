package com.tina.musicband

import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test

@LargeTest
class MainPageCommentTest {
    @Rule
    @JvmField
    var activityActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun commentTest(){

        Thread.sleep(3000)
        onView(withId(R.id.enter_button)).perform(click())

        Thread.sleep(3000)
        onView(withId(R.id.recycler_view_main_page))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, clickChildViewWithId(R.id.comment)))

        Thread.sleep(3000)
        onView(withId(R.id.recycler_view_main_page))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, clickChildViewWithId(R.id.comment_field)), typeTextWithId(R.id.comment_field))

        Thread.sleep(3000)
        onView(withId(R.id.recycler_view_main_page))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, clickChildViewWithId(R.id.send_button)))

        Thread.sleep(3000)

    }

    private fun clickChildViewWithId(id: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "Click on a child view with specified id.";
            }

            override fun getConstraints(): Matcher<View> {
                return Matchers.allOf(
                    isAssignableFrom(
                        RecyclerView::class.java
                    ), isDisplayed()
                )
            }

            override fun perform(uiController: UiController?, view: View?) {
                view?.let {
                    val v = it.findViewById<View>(id)
                    v.performClick()
                }
            }
        }
    }

    private fun typeTextWithId(id: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "Click on a child view with specified id.";
            }

            override fun getConstraints(): Matcher<View> {
                return Matchers.allOf(
                    isAssignableFrom(
                        RecyclerView::class.java
                    ), isDisplayed()
                )
            }

            override fun perform(uiController: UiController?, view: View?) {
                view?.let {
                    val v = it.findViewById<View>(id)
                    v as EditText
                    v.setText("Comment Comment")
                }
            }
        }
    }
}