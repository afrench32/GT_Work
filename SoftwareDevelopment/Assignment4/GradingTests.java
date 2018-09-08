package edu.gatech.seclass.sdpsalarycalc;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;



@RunWith(AndroidJUnit4.class)

public class GradingTests {

    public static Matcher<View> hasTextViewError (final String expectedErrorText) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextView)) {
                    return false;
                }

                CharSequence error = ((TextView) view).getError();

                if (error == null) {
                    return false;
                }

                String hint = error.toString();

                return expectedErrorText.equals(hint);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }


    @Rule
    public ActivityTestRule<SDPSalaryCalcActivity> tActivityRule = new ActivityTestRule<>(
            SDPSalaryCalcActivity.class);

    @Test
    public void CheckInitialCities() {

        onView(withId(R.id.initialCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Atlanta, GA"))).perform(click());
        onView(withId(R.id.initialCity)).check(matches(withSpinnerText(containsString("Atlanta, GA"))));

        onView(withId(R.id.initialCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Athens, GA"))).perform(click());
        onView(withId(R.id.initialCity)).check(matches(withSpinnerText(containsString("Athens, GA"))));

        onView(withId(R.id.initialCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("New York City, NY"))).perform(click());
        onView(withId(R.id.initialCity)).check(matches(withSpinnerText(containsString("New York City, NY"))));

        onView(withId(R.id.initialCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Austin, TX"))).perform(click());
        onView(withId(R.id.initialCity)).check(matches(withSpinnerText(containsString("Austin, TX"))));

        onView(withId(R.id.initialCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Seattle, WA"))).perform(click());
        onView(withId(R.id.initialCity)).check(matches(withSpinnerText(containsString("Seattle, WA"))));

        onView(withId(R.id.initialCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("San Francisco, CA"))).perform(click());
        onView(withId(R.id.initialCity)).check(matches(withSpinnerText(containsString("San Francisco, CA"))));

        onView(withId(R.id.initialCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Washington D.C."))).perform(click());
        onView(withId(R.id.initialCity)).check(matches(withSpinnerText(containsString("Washington D.C."))));

        onView(withId(R.id.initialCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Boston, MA"))).perform(click());
        onView(withId(R.id.initialCity)).check(matches(withSpinnerText(containsString("Boston, MA"))));

        onView(withId(R.id.initialCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Tampa, FL"))).perform(click());
        onView(withId(R.id.initialCity)).check(matches(withSpinnerText(containsString("Tampa, FL"))));

        onView(withId(R.id.initialCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Las Vegas, NV"))).perform(click());
        onView(withId(R.id.initialCity)).check(matches(withSpinnerText(containsString("Las Vegas, NV"))));


    }

    @Test
    public void CheckDestinationCities() {

        onView(withId(R.id.destinationCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Atlanta, GA"))).perform(click());
        onView(withId(R.id.destinationCity)).check(matches(withSpinnerText(containsString("Atlanta, GA"))));

        onView(withId(R.id.destinationCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Athens, GA"))).perform(click());
        onView(withId(R.id.destinationCity)).check(matches(withSpinnerText(containsString("Athens, GA"))));

        onView(withId(R.id.destinationCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("New York City, NY"))).perform(click());
        onView(withId(R.id.destinationCity)).check(matches(withSpinnerText(containsString("New York City, NY"))));

        onView(withId(R.id.destinationCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Austin, TX"))).perform(click());
        onView(withId(R.id.destinationCity)).check(matches(withSpinnerText(containsString("Austin, TX"))));

        onView(withId(R.id.destinationCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Seattle, WA"))).perform(click());
        onView(withId(R.id.destinationCity)).check(matches(withSpinnerText(containsString("Seattle, WA"))));

        onView(withId(R.id.destinationCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("San Francisco, CA"))).perform(click());
        onView(withId(R.id.destinationCity)).check(matches(withSpinnerText(containsString("San Francisco, CA"))));

        onView(withId(R.id.destinationCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Washington D.C."))).perform(click());
        onView(withId(R.id.destinationCity)).check(matches(withSpinnerText(containsString("Washington D.C."))));

        onView(withId(R.id.destinationCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Boston, MA"))).perform(click());
        onView(withId(R.id.destinationCity)).check(matches(withSpinnerText(containsString("Boston, MA"))));

        onView(withId(R.id.destinationCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Tampa, FL"))).perform(click());
        onView(withId(R.id.destinationCity)).check(matches(withSpinnerText(containsString("Tampa, FL"))));

        onView(withId(R.id.destinationCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Las Vegas, NV"))).perform(click());
        onView(withId(R.id.destinationCity)).check(matches(withSpinnerText(containsString("Las Vegas, NV"))));

    }

    @Test
    public void SalaryEmpty() {
        onView(withId(R.id.salary)).check(matches(withText("")));
    }

    @Test
    public void CheckNoSalary() {

        onView(withId(R.id.initialCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("tlanta"))).perform(click());

        onView(withId(R.id.destinationCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("oston"))).perform(click());

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.runButton)).perform(click());

        onView(withId(R.id.salary)).check(matches(hasTextViewError("Salary Required")));

    }

    @Test
    public void CheckNoSalaryAndMatchingCities_FirstError() {

        onView(withId(R.id.initialCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("eattle"))).perform(click());

        onView(withId(R.id.destinationCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("eattle"))).perform(click());

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.runButton)).perform(click());

       // onView(withId(R.id.salary)).check(matches(hasTextViewError("Salary Required")));
        onView(withId(R.id.labelDestinationCity)).check(matches(hasTextViewError("Cities Must Be Different")));

    }

    @Test
    public void CheckNoSalaryAndMatchingCities_SecondError() {

        onView(withId(R.id.initialCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("eattle"))).perform(click());

        onView(withId(R.id.destinationCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("eattle"))).perform(click());

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.runButton)).perform(click());

        onView(withId(R.id.salary)).check(matches(hasTextViewError("Salary Required")));
        //onView(withId(R.id.labelDestinationCity)).check(matches(hasTextViewError("Cities Must Be Different")));

    }

    @Test
    public void CheckMatchingCitiesError() {

        onView(withId(R.id.salary)).perform(clearText(), typeText("10000"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.initialCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("oston"))).perform(click());

        onView(withId(R.id.destinationCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("oston"))).perform(click());

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.runButton)).perform(click());

        onView(withId(R.id.labelDestinationCity)).check(matches(hasTextViewError("Cities Must Be Different")));

    }

    @Test
    public void Screenshot1SalaryCheck() {

        onView(withId(R.id.salary)).perform(clearText(), typeText("150500"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.initialCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("thens"))).perform(click());

        onView(withId(R.id.destinationCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("tlanta"))).perform(click());

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.runButton)).perform(click());

        onView(withId(R.id.resultSalary)).check(matches(withText("169850")));

    }

    @Test
    public void Screenshot2SalaryCheck() {

        onView(withId(R.id.salary)).perform(clearText(), typeText("100101"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.initialCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("ampa"))).perform(click());

        onView(withId(R.id.destinationCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("rancisco"))).perform(click());

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.runButton)).perform(click());

        onView(withId(R.id.resultSalary)).check(matches(withText("170102")));

    }

    @Test
    public void AustinSeattle130000() {

        onView(withId(R.id.salary)).perform(clearText(), typeText("130000"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.initialCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("ustin"))).perform(click());

        onView(withId(R.id.destinationCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("eattle"))).perform(click());

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.runButton)).perform(click());

        onView(withId(R.id.resultSalary)).check(matches(withText("169603")));

    }

    @Test
    public void TampaBoston56100() {

        onView(withId(R.id.salary)).perform(clearText(), typeText("56100"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.initialCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("ampa"))).perform(click());

        onView(withId(R.id.destinationCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("oston"))).perform(click());

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.runButton)).perform(click());

        onView(withId(R.id.resultSalary)).check(matches(withText("78854")));

    }

    @Test
    public void AtlantaLasVegas130000() {

        onView(withId(R.id.salary)).perform(clearText(), typeText("130000"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.initialCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("tlanta"))).perform(click());

        onView(withId(R.id.destinationCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("egas"))).perform(click());

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.runButton)).perform(click());

        onView(withId(R.id.resultSalary)).check(matches(withText("121772")));

    }

    @Test
    public void NewYorkAthens130000() {

        onView(withId(R.id.salary)).perform(clearText(), typeText("130000"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.initialCity)).perform(click());
        //NY
        onData(allOf(is(instanceOf(String.class)), containsString("ork"))).perform(click());

        onView(withId(R.id.destinationCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("thens"))).perform(click());

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.runButton)).perform(click());

        onView(withId(R.id.resultSalary)).check(matches(withText("80176")));

    }

    @Test
    public void SanFranciscoNewYork130000() {

        onView(withId(R.id.salary)).perform(clearText(), typeText("130000"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.initialCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("rancisco"))).perform(click());

        onView(withId(R.id.destinationCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("ork"))).perform(click());

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.runButton)).perform(click());

        onView(withId(R.id.resultSalary)).check(matches(withText("121440")));

    }

    @Test
    public void SeattleAustin130000() {

        onView(withId(R.id.salary)).perform(clearText(), typeText("130000"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.initialCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("eattle"))).perform(click());

        onView(withId(R.id.destinationCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("ustin"))).perform(click());

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.runButton)).perform(click());

        onView(withId(R.id.resultSalary)).check(matches(withText("99645")));

    }

    @Test
    public void WashingtonBoston130000() {

        onView(withId(R.id.salary)).perform(clearText(), typeText("130000"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.initialCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("ashington D"))).perform(click());

        onView(withId(R.id.destinationCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("oston"))).perform(click());

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.runButton)).perform(click());

        onView(withId(R.id.resultSalary)).check(matches(withText("118773")));

    }

    @Test
    public void BostonWashington130000() {

        onView(withId(R.id.salary)).perform(clearText(), typeText("130000"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.initialCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("oston"))).perform(click());

        onView(withId(R.id.destinationCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("ashington D"))).perform(click());

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.runButton)).perform(click());

        onView(withId(R.id.resultSalary)).check(matches(withText("142289")));

    }

    @Test
    public void LasVegasTampa130000() {

        onView(withId(R.id.salary)).perform(clearText(), typeText("130000"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.initialCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("egas"))).perform(click());

        onView(withId(R.id.destinationCity)).perform(click());
        onData(allOf(is(instanceOf(String.class)), containsString("ampa"))).perform(click());

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.runButton)).perform(click());

        onView(withId(R.id.resultSalary)).check(matches(withText("125608")));

    }

}