package edu.gatech.seclass.encode;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class MyMainTest {

    private Main main;
    private Charset charset = StandardCharsets.UTF_8;
    private ByteArrayOutputStream errStream;
    private PrintStream errOrig;

    @Before
    public void setUp() {

        main = new Main();

        errStream = new ByteArrayOutputStream();
        PrintStream err = new PrintStream(errStream);
        errOrig = System.err;
        System.setErr(err);
    }

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @After
    public void tearDown() {

        main = null;
        System.setErr(errOrig);
    }

    // UTILITIES

    // Create temporary file for a test
    private File createTmpFile() throws IOException {
        File tmpfile = temporaryFolder.newFile();
        tmpfile.deleteOnExit();
        return tmpfile;
    }

    // Create file, one word, no special characters
    // abc
    private File createInputFile1() throws Exception {
        File file1 =  createTmpFile();
        FileWriter fileWriter = new FileWriter(file1);

        fileWriter.write("abc");

        fileWriter.close();
        return file1;
    }

    // Create file, one word, with special characters
    // abc,xyz!
    private File createInputFile2() throws Exception {
        File file2 =  createTmpFile();
        FileWriter fileWriter = new FileWriter(file2);

        fileWriter.write("abc,xyz!");

        fileWriter.close();
        return file2;
    }

    // Create file, multiple words, no special characters
    // I am robot
    private File createInputFile3() throws Exception {
        File file3 =  createTmpFile();
        FileWriter fileWriter = new FileWriter(file3);

        fileWriter.write("I am robot");

        fileWriter.close();
        return file3;
    }

    // Create file, multiple words, with special characters
    // Hello, world!
    private File createInputFile4() throws Exception {
        File file4 =  createTmpFile();
        FileWriter fileWriter = new FileWriter(file4);

        fileWriter.write("Hello, world!");

        fileWriter.close();
        return file4;
    }

    // Create file, empty
    private File createInputFile5() throws Exception {
        File file5 = createTmpFile();
        FileWriter fileWriter = new FileWriter(file5);

        fileWriter.write("");

        fileWriter.close();
        return file5;
    }

    // Get contents of a file
    private String getFileContent(String filename) {
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(filename)), charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }


    // TEST CASES

    // Purpose: test default settings with empty file
    // Frame #: 2
    @Test
    public void encodeTest1() throws Exception{

        File inputFile2 = createInputFile5();

        String args[] = {inputFile2.getPath()};

        Main.main(args);

        String expected = "";

        String actual = getFileContent(inputFile2.getPath());

        assertEquals("The files differ!", expected, actual);

    }

    // Purpose: Test cipher with 0 shift
    // Frame #: 4
    @Test
    public void encodeTest2() throws Exception{

        File inputFile3 = createInputFile4();

        String args[] = {"-c", "0", inputFile3.getPath()};

        Main.main(args);

        String expected = "Hello, world!";

        String actual = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected, actual);

    }

    // Purpose: Test cipher with 26 (effectively 0) shift
    // Frame #: 5
    @Test
    public void encodeTest3() throws Exception{

        File inputFile3 = createInputFile4();

        String args[] = {"-c", "26", inputFile3.getPath()};

        Main.main(args);

        String expected = "Hello, world!";

        String actual = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected, actual);

    }

    // Purpose: Test removing no characters
    // Frame #: 6
    @Test
    public void encodeTest4() throws Exception{

        File inputFile3 = createInputFile4();

        String args[] = {"-d", "\"\"", "-c", "3", inputFile3.getPath()};

        Main.main(args);

        String expected = "Khoor, zruog!";

        String actual = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    // Purpose: Testing removing only one character
    // Frame #: 7
    @Test
    public void encodeTest5() throws Exception{
        File inputFile3 = createInputFile4();

        String args[] = {"-d", "e", "-c", "3", inputFile3.getPath()};

        Main.main(args);

        String expected = "Koor, zruog!";

        String actual = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    // Purpose: Testing removing characters that don't exist
    // Frame #: 8
    @Test
    public void encodeTest6() throws Exception{
        File inputFile3 = createInputFile4();

        String args[] = {"-d", "asf", "-c", "3", inputFile3.getPath()};

        Main.main(args);

        String expected = "Khoor, zruog!";

        String actual = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    // Purpose: Test removing some characters that exist, some that don't
    // Frame #: 9
    @Test
    public void encodeTest7() throws Exception {
        File inputFile3 = createInputFile4();

        String args[] = {"-d", "ls", "-c", "3", inputFile3.getPath()};

        Main.main(args);

        String expected = "Khr, zrug!";

        String actual = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    // Purpose: one word, special characters, reversed, c in [1,25], remove letters and special characters
    // Frame #: 10
    @Test
    public void encodeTest8() throws  Exception{
        File inputFile3 = createInputFile2();

        String args[] = {"-d", "!a", "-c", "3", "-r", inputFile3.getPath()};

        Main.main(args);

        String expected = "cba,fe";

        String actual = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    // Purpose: One word, special characters, c in [1,25], reversed, remove letters NOT special characters,
    // Frame #: 11
    @Test
    public void encodeTest9() throws  Exception {
        File inputFile3 = createInputFile2();

        String args[] = {"-d", "abxy", "-c", "3", "-r", inputFile3.getPath()};

        Main.main(args);

        String expected = "!c,f";

        String actual = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    // Purpose: One word, special characters, c in [1,25], reversed, remove special characters NOT letters,
    // Frame #: 12
    @Test
    public void encodeTest10() throws  Exception {
        File inputFile3 = createInputFile2();

        String args[] = {"-d", ",!", "-c", "3", "-r", inputFile3.getPath()};

        Main.main(args);

        String expected = "cbafed";

        String actual = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    // Purpose: One word, special characters, c in [1,25], NOT reversed, letters and special characters
    // Frame #: 14
    @Test
    public void encodeTest11() throws  Exception {
        File inputFile3 = createInputFile2();

        String args[] = {"-d", "a!", "-c", "3", inputFile3.getPath()};

        Main.main(args);

        String expected = "ef,abc";

        String actual = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    // Purpose: One word, special characters, c in [1,25], NOT reversed, remove letters NOT special characters,
    // Frame #: 15
    @Test
    public void encodeTest12() throws  Exception {
        File inputFile3 = createInputFile2();

        String args[] = {"-d", "abc", "-c", "3", inputFile3.getPath()};

        Main.main(args);

        String expected = ",abc!";

        String actual = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    // Purpose: One word, special characters, c in [1,25], not reversed, remove special characters NOT letters,
    // Frame #: 16
    @Test
    public void encodeTest13() throws  Exception {
        File inputFile3 = createInputFile2();

        String args[] = {"-d", ",!", "-c", "3", inputFile3.getPath()};

        Main.main(args);

        String expected = "defabc";

        String actual = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected, actual);
    }


    // Purpose: Multiple words, special characters, c in [1,25], NOT reversed, remove letters ONLY
    // Frame #: 47
    @Test
    public void encodeTest14() throws  Exception {
        File inputFile3 = createInputFile4();

        String args[] = {"-d", "hr", "-c", "3", inputFile3.getPath()};

        Main.main(args);

        String expected = "hoor, zrog!";

        String actual = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    // Purpose: Multiple words, special characters, c in [1,25], NOT reversed, remove special characters ONLY
    // Frame #: 48
    @Test
    public void encodeTest15() throws  Exception {
        File inputFile3 = createInputFile4();

        String args[] = {"-d", ",!", "-c", "3", inputFile3.getPath()};

        Main.main(args);

        String expected = "Khoor zruog";

        String actual = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    // NEW TEST CASES

    // Purpose: file does not exist
    // Frame #: 1
    @Test
    public void encodeTest16() {

        String args[] = {"nonexistentfile.txt"};

        Main.main(args);

        String expected = "File Not Found";

        assertEquals(expected, errStream.toString().trim());
    }

    // Purpose: cypher integer negative
    // Frame #: 3
    @Test
    public void encodeTest17() throws Exception {
        File inputFile3 = createInputFile4();

        String args[] = {"-c", "-1", inputFile3.getPath()};

        Main.main(args);

        String expected = "Gdkkn, vnqkc!";

        String actual = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    // Purpose: one word, special characters, c between 1 and 25, reversed, no letters to remove
    // Frame #: 13
    @Test
    public void encodeTest18() throws  Exception {

        File inputFile3 = createInputFile2();

        String args[] = {"-r", "-c", "3", inputFile3.getPath()};

        Main.main(args);

        String expected = "!cba,fed";

        String actual = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected, actual);

    }

    // Purpose: one word, special characters, c between 1 and 25, NOT reversed, no letters to remove
    // Frame #: 17
    @Test
    public void encodeTest19() throws  Exception {

        File inputFile3 = createInputFile2();

        String args[] = {"-c", "3", inputFile3.getPath()};

        Main.main(args);

        String expected = "def,abc!";

        String actual = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected, actual);

    }

    // Purpose: one word, special characters, c > 26, reversed, remove letters and special characters
    // Frame #: 18
    @Test
    public void encodeTest20() throws  Exception {

        File inputFile3 = createInputFile2();

        String args[] = {"-r", "-d", "z!", "-c", "29", inputFile3.getPath()};

        Main.main(args);

        String expected = "ba,fed";

        String actual = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected, actual);

    }

    // Purpose: one word, special characters, c > 26, reversed, remove letters NOT special characters
    // Frame #: 19
    @Test
    public void encodeTest21() throws  Exception {

        File inputFile3 = createInputFile2();

        String args[] = {"-r", "-d", "z", "-c", "29", inputFile3.getPath()};

        Main.main(args);

        String expected = "!ba,fed";

        String actual = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected, actual);

    }

    // Purpose: one word, special characters, c > 26, reversed, remove special characters NOT letters
    // Frame #: 20
    @Test
    public void encodeTest22() throws  Exception {

        File inputFile3 = createInputFile2();

        String args[] = {"-r", "-d", "!,", "-c", "29", inputFile3.getPath()};

        Main.main(args);

        String expected = "cbafed";

        String actual = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected, actual);

    }

    // Purpose: one word, special characters, c > 26, reversed, remove no characters
    // Frame #: 21
    @Test
    public void encodeTest23() throws  Exception {

        File inputFile3 = createInputFile2();

        String args[] = {"-r", "-c", "29", inputFile3.getPath()};

        Main.main(args);

        String expected = "!cba,fed";

        String actual = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected, actual);

    }

    // Purpose: one word, special characters, c > 26, NOT reversed, remove letters AND special characters
    // Frame #: 22
    @Test
    public void encodeTest24() throws  Exception {

        File inputFile3 = createInputFile2();

        String args[] = {"-d", "!z", "-c", "29", inputFile3.getPath()};

        Main.main(args);

        String expected = "def,ab";

        String actual = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected, actual);

    }

    // Purpose: one word, special characters, c > 26, NOT reversed, remove letters NOT special characters
    // Frame #: 23
    @Test
    public void encodeTest25() throws  Exception {

        File inputFile3 = createInputFile2();

        String args[] = {"-d", "z", "-c", "29", inputFile3.getPath()};

        Main.main(args);

        String expected = "def,ab!";

        String actual = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected, actual);

    }

    // Purpose: one word, special characters, c > 26, NOT reversed, remove special characters NOT letters
    // Frame #: 24
    @Test
    public void encodeTest26() throws  Exception {

        File inputFile3 = createInputFile2();

        String args[] = {"-d", "!,", "-c", "29", inputFile3.getPath()};

        Main.main(args);

        String expected = "defabc";

        String actual = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected, actual);

    }

    // Purpose: one word, special characters, c > 26, NOT reversed, remove nothing
    // Frame #: 25
    @Test
    public void encodeTest27() throws  Exception {

        File inputFile3 = createInputFile2();

        String args[] = {"-c", "29", inputFile3.getPath()};

        Main.main(args);

        String expected = "def,abc!";

        String actual = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected, actual);

    }

    // Purpose: one word, NO special characters, c between 1 and 23, reversed, remove letters AND special characters
    // Frame #: 26
    @Test
    public void encodeTest28() throws  Exception {

        File inputFile3 = createInputFile1();

        String args[] = {"-r", "-d", "c!", "-c", "3", inputFile3.getPath()};

        Main.main(args);

        String expected = "ed";

        String actual = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected, actual);

    }

    // Purpose: one word, NO special characters, c between 1 and 23, reversed, remove letters NOT special characters
    // Frame #: 27
    @Test
    public void encodeTest29() throws  Exception {

        File inputFile3 = createInputFile1();

        String args[] = {"-r", "-d", "c", "-c", "3", inputFile3.getPath()};

        Main.main(args);

        String expected = "ed";

        String actual = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected, actual);

    }

    // Purpose: one word, NO special characters, c between 1 and 23, reversed, remove special characters NOT letters
    // Frame #: 28
    @Test
    public void encodeTest30() throws  Exception {

        File inputFile3 = createInputFile1();

        String args[] = {"-r", "-d", "!", "-c", "3", inputFile3.getPath()};

        Main.main(args);

        String expected = "fed";

        String actual = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected, actual);

    }

}
