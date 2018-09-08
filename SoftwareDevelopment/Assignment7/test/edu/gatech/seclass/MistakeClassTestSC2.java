package edu.gatech.seclass;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

// <100% statement coverage CAN reveal fault
public class MistakeClassTestSC2 {

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
        assertEquals(3/0, mc.mistakeMethod2(3, 0), 0.0f);
    }


}
