package edu.gatech.seclass;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MyCustomStringTestSolution {

    private MyCustomStringInterface mycustomstring;

    @Before
    public void setUp() {
        mycustomstring = new MyCustomString();
    }

    @After
    public void tearDown() {
        mycustomstring = null;
    }

    //given standard string with numbers
    @Test
    public void testCountNumbers1() {
        mycustomstring.setString("H3y, l3t'5 put s0me d161ts in this 5tr1n6!11!!");
        assertEquals(9, mycustomstring.countNumbers());
    }

    //special characters, line break, first character, last character
    @Test
    public void testCountNumbers2() {
        mycustomstring.setString("1'd b3tt3r put s10me d1.61ts in thi5 5tr1n6" + System.lineSeparator() +", right? *()'& some math 123-08 5");
        assertEquals(13, mycustomstring.countNumbers());
    }

    //no digits
    @Test
    public void testCountNumbers3() {
        mycustomstring.setString("And what if I have no digits?");
        assertEquals(0, mycustomstring.countNumbers());
    }

    //all digits, whitespace start/end
    @Test
    public void testCountNumbers4() {
        mycustomstring.setString(" 1 2 3 4 5 01234567890 6 7 8 9 0 ");
        assertEquals(11, mycustomstring.countNumbers());
    }

    //empty string
    @Test
    public void testCountNumbers5() {
        mycustomstring.setString("");
        assertEquals(0, mycustomstring.countNumbers());
    }


    //unset (unitialized string)
    @Test
    public void testCountNumbers6() {

        assertEquals(0, mycustomstring.countNumbers());
    }

    //basic test when right is false
    @Test
    public void testRotateCharacters1() {
        mycustomstring.setString("1234!!! H3y, l3t'5 put 50me d161ts in this 5tr1n6!11!1");
        assertEquals("2341!! !3y,Hl3t 5 p't 5ume 0161ds it thns 5ir1nt!1161!", mycustomstring.rotateCharacters(4, false));
    }

    //basic test when right is true
    @Test
    public void testRotateCharacters2() {
        mycustomstring.setString("1234!!! H3y, l3t'5 put 50me d161ts in this 5tr1n6!11!1");
        assertEquals("4123 !!!,H3yt l3p'5 5ut  0me1d16its hn t5is ntr116!11!", mycustomstring.rotateCharacters(4, true));
    }

    //0 length string
    @Test
    public void testRotateCharacters3() {
        mycustomstring.setString("");
        assertEquals("", mycustomstring.rotateCharacters(1, true));
    }


    //single character
    @Test
    public void testRotateCharacters4() {
            mycustomstring.setString("A");
            assertEquals("A", mycustomstring.rotateCharacters(1, true));
        }

    //start/end with white space
    @Test
    public void testRotateCharacters5() {
        mycustomstring.setString(" 01234567890 ");
        assertEquals("3 01284567 90", mycustomstring.rotateCharacters(5, true));
    }


    //duplicate letters, divides evenly
    @Test
    public void testRotateCharacters6() {
        mycustomstring.setString("AAbbCCddEEff");
        assertEquals("AAbbCCddEEff", mycustomstring.rotateCharacters(2, false));
    }


    //length = n
    @Test
    public void testRotateCharacters7() {
        mycustomstring.setString("*$&%^");
        assertEquals("$&%^*", mycustomstring.rotateCharacters(5, false));
    }


    //n=1, string is unchanged
    @Test
    public void testRotateCharacters8() {
        mycustomstring.setString("H3y, l3t'5 put 50me d161ts in this 5tr1n6!11!!");
        assertEquals("H3y, l3t'5 put 50me d161ts in this 5tr1n6!11!!", mycustomstring.rotateCharacters(1, false));
    }


    //n > length
    @Test
    public void testRotateCharacters9() {
        mycustomstring.setString("H3y, l3t'5 put 50me d161ts in this 5tr1n6!11!!");
        assertEquals("3y, l3t'5 put 50me d161ts in this 5tr1n6!11!!H", mycustomstring.rotateCharacters(48, false));
    }

    //negative illegal argument
    @Test(expected = IllegalArgumentException.class)
    public void testRotateCharacters10() {
        mycustomstring.setString("H3y, l3t'5 put 50me d161ts in this 5tr1n6!11!!");
        mycustomstring.rotateCharacters(-1, false);
    }

    //illegal argument
    @Test(expected = IllegalArgumentException.class)
    public void testRotateCharacters11() {
        mycustomstring.setString("H3y, l3t'5 put 50me d161ts in this 5tr1n6!11!!");
        mycustomstring.rotateCharacters(0, true);
    }

    //null string
    @Test(expected = NullPointerException.class)
    public void testRotateCharacters12() {
        mycustomstring.rotateCharacters(1, true);
    }


    //given standard test
    @Test
    public void testConvertLettersToDigitsInSubstring1() {
        mycustomstring.setString("H3y, l3t'5 put 50me D161ts in this 5tr1n6!11!!");
        mycustomstring.convertLettersToDigitsInSubstring(18, 30);
        assertEquals("H3y, l3t'5 put 50m5 416120,19 9,14 20his 5tr1n6!11!!", mycustomstring.getString());
    }

    //given Test null pointer exception
    @Test(expected = NullPointerException.class)
    public void testConvertLettersToDigitsInSubstring2() {
        mycustomstring.convertLettersToDigitsInSubstring(200, 100);
    }

    //my index out of bounds exception; end > length
    @Test(expected = MyIndexOutOfBoundsException.class)
    public void testConvertLettersToDigitsInSubstring3() {
        mycustomstring.setString("H3y, l3t'5 put 50me D161ts in this 5tr1n6!11!!");
        mycustomstring.convertLettersToDigitsInSubstring(1, 100);
    }

    //my index out of bounds exception; start < 0
    @Test(expected = MyIndexOutOfBoundsException.class)
    public void testConvertLettersToDigitsInSubstring4() {
        mycustomstring.setString("H3y, l3t'5 put 50me D161ts in this 5tr1n6!11!!");
        mycustomstring.convertLettersToDigitsInSubstring(-1, 5);
    }

    //IllegalArgumentException start > end within bounds
    @Test(expected = IllegalArgumentException.class)
    public void testConvertLettersToDigitsInSubstring5() {
        mycustomstring.setString("H3y, l3t'5 put 50me D161ts in this 5tr1n6!11!!");
        mycustomstring.convertLettersToDigitsInSubstring(20, 10);
    }

    //no letters
    @Test
    public void testConvertLettersToDigitsInSubstring6() {
        mycustomstring.setString("!!!01234567890 !*! ");
        mycustomstring.convertLettersToDigitsInSubstring(2, 12);
        assertEquals("!!!01234567890 !*! ", mycustomstring.getString());
    }

    //convert string ends + whole uppercase alphabet
    @Test
    public void testConvertLettersToDigitsInSubstring7() {
        mycustomstring.setString("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        mycustomstring.convertLettersToDigitsInSubstring(0, 25);
        assertEquals("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26", mycustomstring.getString());
    }

    //whole lowercase alphabet + spaces at edges
    @Test
    public void testConvertLettersToDigitsInSubstring8() {
        mycustomstring.setString("  abc1,2,3,de 456fghijk lmnopqr stuvwxyz  ");
        mycustomstring.convertLettersToDigitsInSubstring(1, 39);
        assertEquals("  1,2,31,2,3,4,5 4566,7,8,9,10,11 12,13,14,15,16,17,18 19,20,21,22,23,24,25,26  ", mycustomstring.getString());
    }

    //letters not in converted section
    @Test
    public void testConvertLettersToDigitsInSubstring9() {
        mycustomstring.setString("abc 123 ABC");
        mycustomstring.convertLettersToDigitsInSubstring(3, 7);
        assertEquals("abc 123 ABC", mycustomstring.getString());
    }

    //same letters before/after converted section verifying convert range
    @Test
    public void testConvertLettersToDigitsInSubstring10() {
        mycustomstring.setString("aAaAaAaAaAaAaAaAaAaAaAaAaAaAaA");
        mycustomstring.convertLettersToDigitsInSubstring(5, 12);
        assertEquals("aAaAa1,1,1,1,1,1,1,1AaAaAaAaAaAaAaAaA", mycustomstring.getString());
    }

}
