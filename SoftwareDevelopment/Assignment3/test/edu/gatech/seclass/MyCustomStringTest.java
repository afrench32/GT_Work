package edu.gatech.seclass;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MyCustomStringTest {

    private MyCustomStringInterface mycustomstring;

    @Before
    public void setUp() {
        mycustomstring = new MyCustomString();
    }

    @After
    public void tearDown() {
        mycustomstring = null;
    }

    /**
     * Tests basic functionality of countNumbers
     */
    @Test
    public void testCountNumbers1() {
        mycustomstring.setString("H3y, l3t'5 put s0me d161ts in this 5tr1n6!11!!");
        assertEquals(9, mycustomstring.countNumbers());
    }

    /**
     * Tests that countNumbers correctly identifies when there are no numbers in a string
     */
    @Test
    public void testCountNumbers2() {
        mycustomstring.setString("My mother is a fish!");
        assertEquals(0, mycustomstring.countNumbers());
    }

    /**
     * Tests that countNumbers correctly identifies that the entire string is one number
     */
    @Test
    public void testCountNumbers3() {
        mycustomstring.setString("6942060");
        assertEquals(1, mycustomstring.countNumbers());
    }

    /**
     * Tests that countNumbers correctly identifies numbers that start and end a string
     */
    @Test
    public void testCountNumbers4() {
        mycustomstring.setString("60hehehehe420hehehehe69");
        assertEquals(3, mycustomstring.countNumbers());
    }

    /**
     * Tests that countNumbers correctly separates numbers separated only by spaces
     */
    @Test
    public void testCountNumbers5() {
        mycustomstring.setString("31 44 17");
        assertEquals(3, mycustomstring.countNumbers());
    }

    /**
     * Tests that countNumbers correctly treats floating point numbers as two integers
     */
    @Test
    public void testCountNumbers6() {
        mycustomstring.setString("31.44");
        assertEquals(2, mycustomstring.countNumbers());
    }

    /**
     * Tests basic left rotation of rotateCharacters
     */
    @Test
    public void testRotateCharacters1() {
        mycustomstring.setString("1234!!! H3y, l3t'5 put 50me d161ts in this 5tr1n6!11!1");
        assertEquals("2341!! !3y,Hl3t 5 p't 5ume 0161ds it thns 5ir1nt!1161!",
                mycustomstring.rotateCharacters(4, false));
    }

    /**
     * Tests basic right rotation of rotateCharacters
     */
    @Test
    public void testRotateCharacters2() {
        mycustomstring.setString("1234!!! H3y, l3t'5 put 50me d161ts in this 5tr1n6!11!1");
        assertEquals("4123 !!!,H3yt l3p'5 5ut  0me1d16its hn t5is ntr116!11!",
                mycustomstring.rotateCharacters(4, true));
    }

    /**
     * Tests that rotateCharacters correctly throws a NullPointerException when the string is null
     */
    @Test(expected = NullPointerException.class)
    public void testRotateCharacters3() {
        MyCustomString string = new MyCustomString();
        string.rotateCharacters(5, false);
    }

    /**
     * Tests that rotateCharacters correctly throws an IllegalArgumentException when n is negative
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRotateCharacters4() {
        mycustomstring.setString("Hello GA Tech!");
        mycustomstring.rotateCharacters(-10, false);
    }

    /**
     * Tests that rotateCharacters correctly throws an IllegalArgumentException when n is 0
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRotateCharacters5() {
        mycustomstring.setString("Hello GA Tech!");
        mycustomstring.rotateCharacters(0, false);
    }

    /**
     * Tets that rotateCharacters correctly yields the original string when n = 1
     */
    @Test
    public void testRotateCharacters6() {
        mycustomstring.setString("Hello GA Tech!");
        mycustomstring.rotateCharacters(1, false);
        assertEquals("Hello GA Tech!", mycustomstring.getString());
    }

    /**
     * Tests that rotateCharacters correctly yields the string when n=2 to the right
     */
    @Test
    public void testRotateCharacters7() {
        mycustomstring.setString("GEORGIA TECH");
        assertEquals("EGROIG AETHC", mycustomstring.rotateCharacters(2, true));
    }

    /**
     * Tests that rotateCharacters correctly yields the string when n=2 to the left
     */
    @Test
    public void testRotateCharacters8() {
        mycustomstring.setString("GEORGIA TECH");
        assertEquals("EGROIG AETHC", mycustomstring.rotateCharacters(2, false));
    }

    /**
     * Tests that rotateCharacters correctly yields the string when n is the string length (left)
     */
    @Test
    public void testRotateCharacters9() {
        mycustomstring.setString("GATECH");
        assertEquals("ATECHG", mycustomstring.rotateCharacters(6,false));
    }

    /**
     * Tests that rotateCharacters correctly yields the string when n is the string length (right)
     */
    @Test
    public void testRotateCharacters10() {
        mycustomstring.setString("GATECH");
        assertEquals("HGATEC", mycustomstring.rotateCharacters(6,true));
    }

    /**
     * Tests that rotateCharacters correctly handles the last substring when the string length is
     * divisible by the substring length (right)
     */
    @Test
    public void testRotateCharacters11() {
        mycustomstring.setString("dividing by 5 is fun");
        assertEquals("ividdng bi 5 iy funs",
                mycustomstring.rotateCharacters(5, false));
    }

    /**
     * Tests that rotateCharacters correctly handles the last substring when the string length is
     * divisible by the substring length (left)
     */
    @Test
    public void testRotateCharacters12() {
        mycustomstring.setString("dividing by 5 is fun");
        assertEquals("ddivibing iy 5 ns fu",
                mycustomstring.rotateCharacters(5, true));
    }


    /**
     * Tests basic functionality of convertLettersToDigitsInSubstring
     */
    @Test
    public void testConvertLettersToDigitsInSubstring1() {
        mycustomstring.setString("H3y, l3t'5 put 50me D161ts in this 5tr1n6!11!!");
        mycustomstring.convertLettersToDigitsInSubstring(18, 30);
        assertEquals("H3y, l3t'5 put 50m5 416120,19 9,14 20his 5tr1n6!11!!", mycustomstring.getString());
    }

    /**
     * Tests that convertLettersToDigitsInSubstring correctly throws a NullPointerException
     * when the string is null
     */
    @Test(expected = NullPointerException.class)
    public void testConvertLettersToDigitsInSubstring2() {
        mycustomstring.setString("");
        mycustomstring.convertLettersToDigitsInSubstring(0, 1);
    }

    /**
     * Tests that convertLettersToDigitsInSubstring correctly throws a MyIndexOutOfBoundsException
     * in the case where the start index is negative
     */
    @Test(expected = MyIndexOutOfBoundsException.class)
    public void testConvertLettersToDigitsInSubstring3() { 
        mycustomstring.setString("Wazaaaaaaaaaaap?");
        mycustomstring.convertLettersToDigitsInSubstring(-5, 10);
    }

    /**
     * Tests that convertLettersToDigitsInSubstring correctly throws a MyIndexOutOfBoundsException
     * in the case where the end index is too large
     */
    @Test(expected = MyIndexOutOfBoundsException.class)
    public void testConvertLettersToDigitsInSubstring4() {
        mycustomstring.setString("Wazaaaaaaaaaaap?");
        mycustomstring.convertLettersToDigitsInSubstring(5, 100);
    }

    /**
     * Tests that convertLettersToDigitsInSubstring correctly throws an IllegalArgumentException
     * when the start index is larger than the end index
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConvertLettersToDigitsInSubstring5() {
        mycustomstring.setString("Wazaaaaaaaaaaap?");
        mycustomstring.convertLettersToDigitsInSubstring(100, 5);
    }

    /**
     * Tests that convertLettersToDigitsInSubstring correctly converts just the first character
     */
    @Test
    public void testConvertLettersToDigitsInSubstring6() {
        mycustomstring.setString("Wazaeioup?");
        mycustomstring.convertLettersToDigitsInSubstring(2,2);
        assertEquals("Wa26aeioup?", mycustomstring.getString());
    }

    /**
     * Tests that convertLettersToDigitsInSubstring correctly converts the entire string
     */
    @Test
    public void testConvertLettersToDigitsInSubstring7() {
        mycustomstring.setString("Wazaeioup");
        mycustomstring.convertLettersToDigitsInSubstring(0,8);
        assertEquals("23,1,26,1,5,9,15,21,16", mycustomstring.getString());
    }

    /**
     * Tests whether convertLettersToDigitsInSubstring correctly uses commas
     */
    @Test
    public void testConvertLettersToDigitsInSubstring8() {
        mycustomstring.setString("hello0oo00ooo0 world");
        mycustomstring.convertLettersToDigitsInSubstring(4,13);
        assertEquals("hell15015,150015,15,150 world", mycustomstring.getString());
    }

    /**
     * Tests that convertLettersToDigitsInSubstring correctly converts all lower-case letters
     * of the alphabet
     */
    @Test
    public void testConvertLettersToDigitsInSubstring9() {
        mycustomstring.setString("abcdefghijklmnopqrstuvwxyz");
        mycustomstring.convertLettersToDigitsInSubstring(0,25);
        assertEquals("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26",
                mycustomstring.getString());
    }

    /**
     * Tests that convertLettersToDigitsInSubstring correctly converts all upper-case letters
     * of the alphabet
     */
    @Test
    public void testConvertLettersToDigitsInSubstring10() {
        mycustomstring.setString("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        mycustomstring.convertLettersToDigitsInSubstring(0,25);
        assertEquals("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26",
                mycustomstring.getString());
    }

}
