package edu.gatech.seclass.sdpcryptogram;


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
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentTest {
    @Rule
    public ActivityTestRule<LandingPageActivity> mActivityTestRule = new ActivityTestRule<>(LandingPageActivity.class);

    private LandingPageActivity mainActivity;

    @Test
    public void testDefaultCreateAccountLogout() {
        onView(withId(R.id.ca_button)).perform(click());
        ViewInteraction textView = onView(
                allOf(withId(R.id.textView2), withText("Enter your account credentials"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Enter your account credentials")));

        ViewInteraction editText = onView(
                allOf(withId(R.id.first_name_et), withText("FirstName"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                        0),
                                1),
                        isDisplayed()));
        editText.check(matches(withText("FirstName")));

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.last_name_et), withText("LastName"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                        0),
                                2),
                        isDisplayed()));
        editText2.check(matches(withText("LastName")));

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.username_et), withText("Username"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                        0),
                                3),
                        isDisplayed()));
        editText3.check(matches(withText("Username")));

        ViewInteraction editText4 = onView(
                allOf(withId(R.id.email_et), withText("emailAddress@server.domain"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                        0),
                                4),
                        isDisplayed()));
        editText4.check(matches(withText("emailAddress@server.domain")));

        ViewInteraction button = onView(
                allOf(withId(R.id.create_account),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                        0),
                                5),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.cancel_button),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                        0),
                                6),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.create_account), withText("Create account!"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        0),
                                5),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.welcome_tv), withText("Welcome, FirstName LastName!"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                        0),
                                0),
                        isDisplayed()));
        textView4.check(matches(withText("Welcome, FirstName LastName!")));

        ViewInteraction button3 = onView(
                allOf(withId(R.id.create_button),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                        0),
                                1),
                        isDisplayed()));
        button3.check(matches(isDisplayed()));

        ViewInteraction button4 = onView(
                allOf(withId(R.id.solve_button),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                        0),
                                2),
                        isDisplayed()));
        button4.check(matches(isDisplayed()));

        ViewInteraction button5 = onView(
                allOf(withId(R.id.view_button),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                        0),
                                3),
                        isDisplayed()));
        button5.check(matches(isDisplayed()));

        ViewInteraction button6 = onView(
                allOf(withId(R.id.logout_button),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                        0),
                                4),
                        isDisplayed()));
        button6.check(matches(isDisplayed()));

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.logout_button), withText("Log out"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        0),
                                4),
                        isDisplayed()));
        appCompatButton3.perform(click());

    }

    @Test
    public void testDefaultLoginLogout() {
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.logout_button)).perform(click());
    }

    @Test
    public void testCreateUsername1() {
        onView(withId(R.id.ca_button)).perform(click());
        onView(withId(R.id.username_et)).perform(click())
                .perform(replaceText("Username1"));
        onView(withId(R.id.create_account)).perform(click());
        onView(withText("Welcome, FirstName LastName!")).check(matches(isDisplayed()));
        onView(withId(R.id.logout_button)).perform(click());
    }

    @Test
    public void testCreateDuplicateUsernameError() {
        onView(withId(R.id.ca_button)).perform(click());
        onView(withId(R.id.username_et)).perform(click())
                .perform(replaceText("Username11"));
        onView(withId(R.id.create_account)).perform(click());
        onView(withText("Welcome, FirstName LastName!")).check(matches(isDisplayed()));
        onView(withId(R.id.logout_button)).perform(click());

        boolean flag = false;

        while (flag == false) {
            try {
                onView(withId(R.id.ca_button)).perform(click());
                onView(withId(R.id.username_et)).perform(click())
                        .perform(replaceText("Username11"));
                onView(withId(R.id.create_account)).perform(click());
            }
            catch (Exception e) {
                flag = true;
            }
        }
        assertEquals(true, flag);
    }

    @Test
    public void testLoginNonExistUsernameError() {
        boolean flag = false;
        onView(withId(R.id.login_button)).perform(click());

        while (flag == false) {
            try {
                onView(withId(R.id.username_et)).perform(click())
                        .perform(replaceText("Username100"));
                onView(withId(R.id.login_button)).perform(click());
                onView(withText("Welcome, FirstName LastName!")).check(matches(isDisplayed()));
            }
            catch (Exception e) {
                flag = true;
            }
        }
        assertEquals(true, flag);
    }

    @Test
    public void testLoginUsername1Logout() {
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.username_et)).perform(click())
                .perform(replaceText("Username1"));
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.logout_button)).perform(click());
    }

//    PuzzleName1
//    Solution1
    @Test
    public void testCreateCryptogramRandomByUsername() {
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.create_button)).perform(click());
        onView(withId(R.id.puzzle_name_et)).perform(click())
                .perform(replaceText("PuzzleName1"));
        onView(withId(R.id.solution_et)).perform(click())
                .perform(replaceText("Solution1"));
        onView(withId(R.id.randomize_cypher_button)).perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.encoded_phrase), withText("Bluahplg1"), isDisplayed()));
        textView.check(matches(withText("Bluahplg1")));

        onView(withId(R.id.allowed_attempts_et)).perform(click())
                .perform(replaceText("3"));

        onView(withId(R.id.save_button)).perform(click());
    }

//    PuzzleName2
//    Solution2
    @Test
    public void testCreateCryptogramCustomCypherByUsername() {
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.create_button)).perform(click());
        onView(withId(R.id.puzzle_name_et)).perform(click())
                .perform(replaceText("PuzzleName2"));
        onView(withId(R.id.solution_et)).perform(click())
                .perform(replaceText("Solution2"),closeSoftKeyboard());
        onView(withId(R.id.randomize_cypher_button)).perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.encoded_phrase), withText("Bluahplg2"), isDisplayed()));
        textView.check(matches(withText("Bluahplg2")));

        onView(withId(R.id.et_i)).perform(scrollTo(), replaceText("w"));

//        onView(withId(R.id.et_i)).perform(replaceText("w"));

        onView(withId(R.id.customize_cypher_button)).perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.encoded_phrase), withText("Bluahwlg2"), isDisplayed()));
        textView2.check(matches(withText("Bluahwlg2")));

        onView(withId(R.id.allowed_attempts_et)).perform(click())
                .perform(replaceText("3"));

        onView(withId(R.id.save_button)).perform(click());
    }

//    PuzzleName3
//    New Solution3
    @Test
    public void testCreateCryptogramReencodeByUsername() {
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.create_button)).perform(click());
        onView(withId(R.id.puzzle_name_et)).perform(click())
                .perform(replaceText("PuzzleName3"));
        onView(withId(R.id.solution_et)).perform(click())
                .perform(replaceText("Solution3"));
        onView(withId(R.id.randomize_cypher_button)).perform(click());

//        onView(withId(R.id.et_i)).perform(click())
//                .perform(replaceText("w"));
//        onView(withId(R.id.customize_cypher_button)).perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.encoded_phrase), withText("Bluahplg3"), isDisplayed()));
        textView.check(matches(withText("Bluahplg3")));

        onView(withId(R.id.solution_et)).perform(click())
                .perform(replaceText("New Solution3"));
        onView(withId(R.id.encode_solution)).perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.encoded_phrase), withText("Gew Bluahplg3"), isDisplayed()));
        textView2.check(matches(withText("Gew Bluahplg3")));
//                .perform(replaceText("w"));
//        onView(withId(R.id.customize_cypher_button)).perform(click());

        onView(withId(R.id.allowed_attempts_et)).perform(click())
                .perform(replaceText("3"));

        onView(withId(R.id.save_button)).perform(click());
    }

    @Test
    public void testViewUncompletedList() {
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.username_et)).perform(click())
                .perform(replaceText("Username1"));
        onView(withId(R.id.login_button)).perform(click());

        onView(withId(R.id.view_button)).perform(click());
        onView(withId(R.id.incomplete_button)).perform(click());
        onView(withId(R.id.incomplete_button)).perform(click());

        ViewInteraction TextView = onView(
                allOf(withId(R.id.incomplete_cryptograms_text), isDisplayed()));
        TextView.check(matches(isDisplayed()));
    }

    @Test
    public void testSolveCryptogramPuzzleName1ByUsername1() {
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.username_et)).perform(click())
                .perform(replaceText("Username1"));
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.solve_button)).perform(click());

//        Enter puzzlename
        onView(withId(R.id.puzzle_name)).perform(click())
                .perform(replaceText("PuzzleName1"), closeSoftKeyboard());
        onView(withId(R.id.solve_button)).perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.puzzle_name), withText("You are solving: PuzzleName1"), isDisplayed()));
        textView.check(matches(withText("You are solving: PuzzleName1")));

//        Start to solve:Bluahplg1
        onView(withId(R.id.et_b)).perform(closeSoftKeyboard(), scrollTo(), replaceText("s"));
        onView(withId(R.id.et_l)).perform(scrollTo(), replaceText("o"));
        onView(withId(R.id.et_u)).perform(scrollTo(), replaceText("l"));
        onView(withId(R.id.et_a)).perform(scrollTo(), replaceText("u"));
        onView(withId(R.id.et_h)).perform(scrollTo(), replaceText("t"));
        onView(withId(R.id.et_p)).perform(scrollTo(), replaceText("i"));
        onView(withId(R.id.et_g)).perform(scrollTo(), replaceText("n"));

//        Use cipher
        onView(withId(R.id.cypher_button)).perform(click());
        ViewInteraction textView2 = onView(
                allOf(withId(R.id.decoded_phrase), withText("Solution1"), isDisplayed()));
        textView2.check(matches(withText("Solution1")));

//        Enter solution
        onView(withId(R.id.enter_solution_button)).perform(click());
    }

//    @Test
//    public void testViewUncompletedList() {
//
//    }

    @Test
    public void testViewCompletedList() {
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.username_et)).perform(click())
                .perform(replaceText("Username1"));
        onView(withId(R.id.login_button)).perform(click());

        onView(withId(R.id.view_button)).perform(click());
        onView(withId(R.id.complete_button)).perform(click());
        onView(withId(R.id.complete_button)).perform(click());

        ViewInteraction TextView = onView(
                allOf(withId(R.id.complete_cryptograms_text), isDisplayed()));
        TextView.check(matches(isDisplayed()));
    }

    @Test
    public void testBackToMainMenuDuringSolving() {
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.username_et)).perform(click())
                .perform(replaceText("Username1"));
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.solve_button)).perform(click());

//        Enter puzzlename
        onView(withId(R.id.puzzle_name)).perform(click())
                .perform(replaceText("PuzzleName1"), closeSoftKeyboard());
        onView(withId(R.id.solve_button)).perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.puzzle_name), withText("You are solving: PuzzleName1"), isDisplayed()));
        textView.check(matches(withText("You are solving: PuzzleName1")));

//        Start to solve:Bluahplg1
        onView(withId(R.id.et_b)).perform(closeSoftKeyboard(), scrollTo(), replaceText("s"));
        onView(withId(R.id.et_l)).perform(scrollTo(), replaceText("o"));
        onView(withId(R.id.et_u)).perform(scrollTo(), replaceText("l"));
        onView(withId(R.id.et_a)).perform(scrollTo(), replaceText("u"));
        onView(withId(R.id.et_h)).perform(scrollTo(), replaceText("t"));
        onView(withId(R.id.et_p)).perform(scrollTo(), replaceText("i"));
        onView(withId(R.id.et_g)).perform(scrollTo(), replaceText("n"));

//        Use cipher
        onView(withId(R.id.cypher_button)).perform(click());
        ViewInteraction textView2 = onView(
                allOf(withId(R.id.decoded_phrase), withText("Solution1"), isDisplayed()));
        textView2.check(matches(withText("Solution1")));

//        Back to main menu
        onView(withId(R.id.cancel_button)).perform(click());
        ViewInteraction textView4 = onView(
                allOf(withId(R.id.welcome_tv), withText("Welcome, FirstName LastName!"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                        0),
                                0),
                        isDisplayed()));
        textView4.check(matches(withText("Welcome, FirstName LastName!")));

    }

//    @Test
//    public void testViewStatisticsList() {
//        onView(withId(R.id.login_button)).perform(click());
//        onView(withId(R.id.username_et)).perform(click())
//                .perform(replaceText("Username1"));
//        onView(withId(R.id.login_button)).perform(click());
//
//        onView(withId(R.id.view_button)).perform(click());
//        onView(withId(R.id.statistics_button)).perform(click());
//        onView(withId(R.id.statistics_button)).perform(click());
//
//        ViewInteraction TextView = onView(
//                allOf(withId(R.id.statistics_text), isDisplayed()));
//        TextView.check(matches(isDisplayed()));
//    }

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

