package edu.gatech.seclass.sdpsalarycalc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private SDPSalaryCalcActivity sca;

    @Before
    public void setUp() {
        sca = new SDPSalaryCalcActivity();
    }

    @After
    public void tearDown() {
        sca = null;
    }

    @Test
    public void testConvertSalary1() {
        String salary = "150500";
        String initialCity = "Athens, GA";
        String destinationCity = "Atlanta, GA";
        String result = sca.convertSalary(salary, initialCity, destinationCity);
        assertEquals("169850", result);
    }

    @Test
    public void testConvertSalary2() {
        String salary = "100101";
        String initialCity = "Tampa, FL";
        String destinationCity = "San Francisco, CA";
        String result = sca.convertSalary(salary, initialCity, destinationCity);
        assertEquals("170102", result);
    }

    @Test
    public void testConvertSalary3() {
        String salary = "100101";
        String initialCity = "TOOTBLAN";
        String destinationCity = "San Francisco, CA";
        String result = sca.convertSalary(salary, initialCity, destinationCity);
        assertEquals("ERROR", result);
    }

    @Test
    public void testConvertSalary4() {
        String salary = "100101";
        String initialCity = "San Francisco, CA";
        String destinationCity = "FARTSLAM";
        String result = sca.convertSalary(salary, initialCity, destinationCity);
        assertEquals("ERROR", result);
    }
}