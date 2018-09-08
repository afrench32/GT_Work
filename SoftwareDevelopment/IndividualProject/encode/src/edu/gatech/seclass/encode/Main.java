package edu.gatech.seclass.encode;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Character.isWhitespace;

public class Main {

    private static Charset charset = StandardCharsets.UTF_8;

    public static void main(String[] args) {

        int shift = -100;
        boolean reverseChars;
        boolean reverseWords;
        ArrayList<String> charsToRemove = new ArrayList<>();
        String filename;
        int fileLength;
        String fileString;

        // move arguments to arrayList
        ArrayList<String> argsAL = new ArrayList<>();
        if (args == null) {
            usage();
            return;
        }
        Collections.addAll(argsAL, args);

        // read file into string, get file length
        filename = argsAL.remove(argsAL.size() - 1);
        File file = new File(filename);
        if (!file.exists() || file.isDirectory()) {
            System.err.println("File Not Found");
            return;
        }
        fileLength = getFileLength(filename);
        fileString = getFileContent(filename);

        // set shift
        int cIndex = argsAL.lastIndexOf("-c");
        //   if there are no arguments at all set shift to file length. THIS IS RIGHT, DON'T FORGET OR CHANGE IT
        if (argsAL.size() == 0) {
            shift = fileLength;
        }
        //   if the c flag is last, error
        else if (cIndex >= argsAL.size() - 1) {
            usage();
            return;
        }
        //   if the c flag is there and in a legal position, set shift
        else if (cIndex >= 0 && cIndex < argsAL.size() - 1) {
            try {
                String shiftStr = argsAL.get(cIndex + 1);
                shift = Integer.parseInt(shiftStr);
                argsAL.remove(cIndex);
                argsAL.remove(shiftStr);
            }
            catch (NumberFormatException e) {
                usage();
                e.printStackTrace();
                return;
            }
        }
        //   if the c flag is not there but there are other arguments, set shift to 0
        else {
            shift = 0;
        }

        // remove extraneous c flags and related arguments
        boolean cStillThere = true;
        while (cStillThere) {
            if (argsAL.contains("-c")) {
                cIndex = argsAL.indexOf("-c");
                if (cIndex >= argsAL.size() - 1) {
                    usage();
                    return;
                }
                String tmp = argsAL.get(cIndex + 1);
                argsAL.remove(cIndex);
                argsAL.remove(tmp);
            }
            else {
                cStillThere = false;
            }
        }

        // set remove characters list
        int dIndex = argsAL.indexOf("-d");
        if (dIndex >= 0 && dIndex < argsAL.size() - 1) {
            String allRemChars = argsAL.get(dIndex + 1);
            for (int i = 0; i < allRemChars.length(); i++) {
                if (!charsToRemove.contains(String.valueOf(allRemChars.charAt(i)))) {
                    charsToRemove.add(String.valueOf(allRemChars.charAt(i)));
                }
            }
            argsAL.remove(dIndex);
            argsAL.remove(allRemChars);
        }
        else if (dIndex >= 0 && dIndex >= argsAL.size() - 1) {
            usage();
            return;
        }

        // set reverseChars flag
        reverseChars = argsAL.remove("-r");

        // set reverseWords flag
        reverseWords = argsAL.remove("-R");

        // exit if there are remaining (thus illegal) arguments
        if (argsAL.size() > 0) {
            usage();
            return;
        }

        // encode string
        String encoded = encode(fileString, shift, reverseChars, reverseWords, charsToRemove);

        // write string to file
        try {
            FileWriter fileWriter = new FileWriter(filename, false);
            fileWriter.write(encoded);
            fileWriter.flush();
            fileWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Encodes a string according to specifications
     * @param fileString, the string to be encoded (an entire file)
     * @param shift, int representing the amount to shift using a Caesar cypher
     * @param reverseChars, boolean representing whether or not to reverse characters in words
     * @param reverseWords, boolean representing whether or not to reverse the order of the words
     * @param charsToRemove, list of characters to remove
     * @return encoded form of fileString
     */
    private static String encode(String fileString, int shift, boolean reverseChars,
                                 boolean reverseWords, ArrayList<String> charsToRemove) {

        StringBuilder finalString = new StringBuilder();
        StringBuilder word = new StringBuilder();
        ArrayList<StringBuilder> fileAsList = new ArrayList<StringBuilder>();

        for (int i = 0; i < fileString.length(); i++) {
            char ch = fileString.charAt(i);
            if (!charsToRemove.contains(String.valueOf(ch).toLowerCase()) &&
                    !charsToRemove.contains(String.valueOf(ch).toUpperCase())) {
                if (!isWhitespace(ch)) {
                    // add letter to word, in reverse or in not reverse
                    if (!reverseChars) {
                        word.append(ch);
                    }
                    else {
                        StringBuilder temp = new StringBuilder();
                        temp.append(ch);
                        word = temp.append(word);
                    }
                }
                // if the character is whitespace
                else {
                    // add word to list of words and reset word
                    if (word.length() > 0) {
                        fileAsList.add(applyCypher(word, shift));
                        word = new StringBuilder();
                    }
                    // add whitespace to list of words
                    StringBuilder temp = new StringBuilder();
                    temp.append(ch);
                    fileAsList.add(temp);

                }
            }
        }
        if (word.length() > 0) {
            fileAsList.add(applyCypher(word, shift));
        }
        // add whitespace to list of words
        StringBuilder temp = new StringBuilder();
        fileAsList.add(temp);

        // add words to finalString, in normal or reverse order
        if (!reverseWords) {
            for (int i = 0; i < fileAsList.size(); i++) {
                finalString.append(fileAsList.get(i));
            }
        }
        else {
            for (int i = fileAsList.size() - 1; i >= 0; i--) {
                finalString.append(fileAsList.get(i));
            }
        }

        return finalString.toString();

    }

    /**
     * Applies caesar cypher with given shift to a word
     * @param word, word to encode
     * @param shift, shift to use in caesar cypher
     * @return retStr, the encoded word
     */
    private static StringBuilder applyCypher(StringBuilder word, int shift) {

        StringBuilder retStr = new StringBuilder();

        String lowerCharacters[]  = { "a", "b", "c", "d", "e", "f", "g", "h", "i",
                "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
                "w", "x", "y", "z" };
        String digits[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        Map<String, String> cypher = new HashMap<>();

        // build the cypher
        for (int i = 0; i < lowerCharacters.length; i++) {
            int shiftInd = (i + shift) % 26;
            if (shiftInd < 0) {
                shiftInd = 26 + shiftInd;
            }
            cypher.put(lowerCharacters[i % 26], lowerCharacters[shiftInd]);
            cypher.put(lowerCharacters[i % 26].toUpperCase(), lowerCharacters[shiftInd].toUpperCase());
        }
        for (int i = 0; i < digits.length; i++) {
            int shiftInd = (i + shift) % 10;
            if (shiftInd < 0) {
                shiftInd = 10 + shiftInd;
            }
            cypher.put(digits[i % 10], digits[shiftInd]);
        }

        // add cyphered character or just character to string
        for (int i = 0; i < word.length(); i++) {
            String ch = String.valueOf(word.charAt(i));
            retStr.append(cypher.getOrDefault(ch, ch));
        }

        return retStr;

    }

    /**
     * Prints usage statement
     */
    private static void usage() {
        System.err.println("Usage: Encode  [-c int] [-d string] [-r] [-R] <filename>");
    }

    /**
     * reads contents of a file into a string
     * @param filename, name of file to read
     * @return content, string representing the content of the file
     */
    private static String getFileContent(String filename) {
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(filename)), charset);
        } catch (IOException e) {
            usage();
            e.printStackTrace();
        }
        return content;
    }

    /**
     * gets length of given file in bytes
     * @param filename, name of file to read
     * @return fileLength, long giving length of file in bytes
     */
    private static int getFileLength(String filename) {

        long fileLength;
        File file;

        file = new File(filename);
        fileLength = file.length();

        return (int) fileLength;

    }
}
