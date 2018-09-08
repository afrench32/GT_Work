package edu.gatech.seclass;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

// 100% branch coverage MIGHT NOT reveal fault
public class MistakeClassTestBC2 {

    private MistakeClass mc;

    @Before
    public void setUp() {
        mc = new MistakeClass();
    }

    @After
    public void breakDown() {
        mc = null;
    }

    @Test
    public void testMistakeMethod2_1() {
        assertEquals(1.5, mc.mistakeMethod2(3, 2), 0.0f);
    }

    @Test
    public void testMistakeMethod2_2() {
        assertEquals(31, mc.mistakeMethod2(-3, 2), 0.0f);
    }


}
