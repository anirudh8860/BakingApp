package simplegamer003.bakingapp;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BakingAppUiTest {

    @Rule
    public ActivityTestRule<Home> mActivityTestRule = new ActivityTestRule<>(Home.class);

    @Test
    public void homeTest() {
        ViewInteraction textView = onView(
                allOf(withId(R.id.dish_name), withText("Nutella Pie"),
                        childAtPosition(
                                allOf(withId(R.id.dish_card_layout),
                                        childAtPosition(
                                                withId(R.id.dish_card_view),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.dish_recycler_view),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                2)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction cardView = onView(
                allOf(withId(R.id.ingredients_card),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.dish_details_scroll_view),
                                        0),
                                0)));
        cardView.perform(scrollTo(), click());

        ViewInteraction cardView2 = onView(
                allOf(withId(R.id.ingredients_card),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.dish_details_scroll_view),
                                        0),
                                0)));
        cardView2.perform(scrollTo(), click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.steps_item), withText("Prep the cookie crust."),
                        childAtPosition(
                                allOf(withId(R.id.short_desc_card_view),
                                        childAtPosition(
                                                withId(R.id.steps_recycler_view),
                                                2)),
                                0)));
        appCompatTextView.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.exo_play), withContentDescription("Play"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.next), withText("Next"),
                        childAtPosition(
                                allOf(withId(R.id.prev_next_layout),
                                        childAtPosition(
                                                withId(R.id.dish_step_fragment),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.prev), withText("Prev"),
                        childAtPosition(
                                allOf(withId(R.id.prev_next_layout),
                                        childAtPosition(
                                                withId(R.id.dish_step_fragment),
                                                1)),
                                0),
                        isDisplayed()));
        appCompatButton2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        pressBack();

        pressBack();

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
