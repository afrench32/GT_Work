package edu.gatech.seclass;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

// 100% branch coverage MIGHT NOT reveal fault
public class MistakeClassTest5 {

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
    public void testMistakeMethod5_1() {
        boolean ans = mc.mistakeMethod5(true, true);
        System.out.println(ans);
    }

    @Test
    public void testMistakeMethod5_2() {
        boolean ans = mc.mistakeMethod5(true, false);
        System.out.println(ans);
    }

    @Test
    public void testMistakeMethod5_3() {
        boolean ans = mc.mistakeMethod5(false, true);
        System.out.println(ans);
    }

    @Test
    public void testMistakeMethod5_4() {
        boolean ans = mc.mistakeMethod5(false, false);
        System.out.println(ans);
    }


}
