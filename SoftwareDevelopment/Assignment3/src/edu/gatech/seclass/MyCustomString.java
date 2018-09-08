package edu.gatech.seclass;

public class MyCustomString implements MyCustomStringInterface {

    /**
     * Default and only constructor. Does nothing
     * */
    public MyCustomString() {

        this.theString = "";

    }

    /**
     * Returns the current string.
     * If the string is null, or has not been set to a value, it should return null.
     *
     * @return Current string
     */
    public String getString() {

        return this.theString;

    }

    /**
     * Sets the value of the current string
     *
     * @param string The value to be set
     */
    public void setString(String string) {

        this.theString = string;

    }

    /**
     * Returns the number of numbers in the current string, where a number is defined as a
     * contiguous (i.e., uninterrupted) sequence of digits.
     *
     * If the current string is null or empty, the method should return 0.
     *
     * Examples:
     * - countNumbers would return 2 for string "My numbers are 11 and 9".
     *
     * @return Number of numbers in the current string
     */
    public int countNumbers(){

        if (this.theString.length() == 0) {

            return 0;

        }

        int count = 0;

        for (int i = 0; i < this.theString.length(); i++) {

            if (this.charIsDigit(this.theString.charAt(i))) {

                if (i > 0) {

                    if (!this.charIsDigit(this.theString.charAt(i - 1))) {

                        count ++;

                    }// if

                } // if

                else {

                    count ++;

                } // else

            } // if

        } // for

        return count;

    }

    /**
     * Rotates every substring of n characters in the current string and returns the resulting string.
     * If 'right' is true, each substring of n characters should be shifted one place right, with the n-th character
     * becoming the first character in the resulting substring. Otherwise, if 'right' is false, the characters should be
     * shifted one place to the left, with the first character becoming the n-th character in the resulting substring.
     * In case the length of the current string ia not a multiple of n, the last partial substring should also be shifted
     * in an analogous way.
     *
     * Examples:
     * - For n=2 and right=true, "hello 90, bye 2" would be converted to "ehll o09 ,yb e2".
     * - For n=4 and right=true, "1234567890" would be converted to "4123856709".
     * - For n=2 and right=false, "hello 90, bye 2" would be converted to "ehll o09 ,yb e2".
     * - For n=4 and right=false, "1234567890" would be converted to "2341678509".
     *
     * @param n Size of substring of characters to rotate
     * @param right Boolean that indicates whether characters rotate to the right (vs the left)
     * @return String with the original string's characters rotated in sets of n size
     * @throws NullPointerException     If the current string is null
     * @throws IllegalArgumentException If n <=0 (and the current string is not null)
     */
    public String rotateCharacters(int n, boolean right){

        if (this.theString.equals("")) {
            throw new NullPointerException();
        }
        else if (n <= 0) {
            throw new IllegalArgumentException();
        }

        StringBuilder retString = new StringBuilder();

        int numSubstrings = this.theString.length() / n;
        if (this.theString.length() % n > 0) numSubstrings ++;

        // for each substring
        for (int i = 0; i < numSubstrings; i++) {

            String tempString = "";
            String substring;
            int substringLength = n;

            if (i == numSubstrings - 1) {
                substring = this.theString.substring(n * i, this.theString.length());
                int mod = this.theString.length() % n;
                if (mod != 0) {
                    substringLength = mod;
                }
            }
            else substring = this.theString.substring(n*i, n*(i+1));

            if (right) {

                tempString = tempString + substring.charAt(substringLength - 1);

                for (int j = 0; j < substring.length() - 1; j++) {
                    tempString = tempString + substring.charAt(j);
                }

            }

            else {

                for (int j = 1; j < substring.length(); j++) {
                    tempString = tempString + substring.charAt(j);
                }

                tempString = tempString + substring.charAt(0);

            }

            retString.append(tempString);

        }

        return retString.toString();

    }

    /**
     * Replaces the individual letters in the current string, between startPosition and endPosition (both inclusive),
     * with the corresponding number for each letter's place in the English alphabet (i.e., a or A = 1 and z or Z = 26).
     * Contiguous, converted letters should be separated by commas. Non-alphatecic characters should be left unchanged.
     *
     * Examples:
     * - String "Dog" would be converted to "4,15,7"
     * - String "3 Cats." would be converted to "3 3,1,20,19."
     *
     * @param startPosition Position of the first character to consider
     * @param endPosition   Position of the last character to consider
     * @throws NullPointerException        If the current string is null
     * @throws MyIndexOutOfBoundsException If endPosition is greater than the length of the string or startPosition < 0
     *                                     (and the current string is not null)
     * @throws IllegalArgumentException    If startPosition > endPosition
     *                                     (and the current string is not null, and neither position is out of bounds)
     */
    public void convertLettersToDigitsInSubstring(int startPosition, int endPosition) {

        if (this.theString.equals("")) {
            throw new NullPointerException();
        }

        if (startPosition < 0 || endPosition > this.theString.length() - 1) {
            throw new MyIndexOutOfBoundsException();
        }

        if (startPosition > endPosition) {
            throw new IllegalArgumentException();
        }

        String before = this.theString.substring(0, startPosition);
        String middle = this.theString.substring(startPosition, endPosition + 1);
        String after = this.theString.substring(endPosition + 1, this.theString.length());

        String tempString = "";

        for (int i = 0; i < middle.length(); i++) {

            tempString += this.charAsAlpha(middle.charAt(i));

            if (i + 1 < middle.length()) {
                if (this.charIsAlpha(middle.charAt(i)) && this.charIsAlpha(middle.charAt(i + 1))) {
                    tempString = tempString + ",";
                }
            }
        }
        middle = tempString;

        this.setString(before + middle + after);

    }

    /**
     * Helper method to determine if a given character is a digit
     *
     * @param c character to test
     * @return boolean value indicating whether character is digit or not
     */
    private boolean charIsDigit(char c) {

        return c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' ||
                c == '8' || c == '9';

    }

    /**
     * Helper method to determine if a given character is a letter
     *
     * @param c character to test
     * @return boolean value indicating if character is letter
     */
    private boolean charIsAlpha(char c) {
        if (c == 'a' || c == 'A') return true;
        if (c == 'b' || c == 'B') return true;
        if (c == 'c' || c == 'C') return true;
        if (c == 'd' || c == 'D') return true;
        if (c == 'e' || c == 'E') return true;
        if (c == 'f' || c == 'F') return true;
        if (c == 'g' || c == 'G') return true;
        if (c == 'h' || c == 'H') return true;
        if (c == 'i' || c == 'I') return true;
        if (c == 'j' || c == 'J') return true;
        if (c == 'k' || c == 'K') return true;
        if (c == 'l' || c == 'L') return true;
        if (c == 'm' || c == 'M') return true;
        if (c == 'n' || c == 'N') return true;
        if (c == 'o' || c == 'O') return true;
        if (c == 'p' || c == 'P') return true;
        if (c == 'q' || c == 'Q') return true;
        if (c == 'r' || c == 'R') return true;
        if (c == 's' || c == 'S') return true;
        if (c == 't' || c == 'T') return true;
        if (c == 'u' || c == 'U') return true;
        if (c == 'v' || c == 'V') return true;
        if (c == 'w' || c == 'W') return true;
        if (c == 'x' || c == 'X') return true;
        if (c == 'y' || c == 'Y') return true;
        return (c == 'z' || c == 'Z');
    }

    /**
     * Helper method to convert letter to corresponding number
     *
     * @param c character to convert
     * @return number if character is a letter, simply the character otherwise
     */
    private String charAsAlpha(char c) {
        if (c == 'a' || c == 'A') return "1";
        else if (c == 'b' || c == 'B') return "2";
        else if (c == 'c' || c == 'C') return "3";
        else if (c == 'd' || c == 'D') return "4";
        else if (c == 'e' || c == 'E') return "5";
        else if (c == 'f' || c == 'F') return "6";
        else if (c == 'g' || c == 'G') return "7";
        else if (c == 'h' || c == 'H') return "8";
        else if (c == 'i' || c == 'I') return "9";
        else if (c == 'j' || c == 'J') return "10";
        else if (c == 'k' || c == 'K') return "11";
        else if (c == 'l' || c == 'L') return "12";
        else if (c == 'm' || c == 'M') return "13";
        else if (c == 'n' || c == 'N') return "14";
        else if (c == 'o' || c == 'O') return "15";
        else if (c == 'p' || c == 'P') return "16";
        else if (c == 'q' || c == 'Q') return "17";
        else if (c == 'r' || c == 'R') return "18";
        else if (c == 's' || c == 'S') return "19";
        else if (c == 't' || c == 'T') return "20";
        else if (c == 'u' || c == 'U') return "21";
        else if (c == 'v' || c == 'V') return "22";
        else if (c == 'w' || c == 'W') return "23";
        else if (c == 'x' || c == 'X') return "24";
        else if (c == 'y' || c == 'Y') return "25";
        else if (c == 'z' || c == 'Z') return "26";
        else {
            return "" + c;
        }
    }

    private String theString;

}
