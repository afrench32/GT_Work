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

import static org.junit.Assert.*;

/*
DO NOT ALTER THIS CLASS.  Use it as an example for MyMainTest.java
 */

public class MainTest {

    private ByteArrayOutputStream outStream;
    private ByteArrayOutputStream errStream;
    private PrintStream outOrig;
    private PrintStream errOrig;
    private Charset charset = StandardCharsets.UTF_8;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        outStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outStream);
        errStream = new ByteArrayOutputStream();
        PrintStream err = new PrintStream(errStream);
        outOrig = System.out;
        errOrig = System.err;
        System.setOut(out);
        System.setErr(err);
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(outOrig);
        System.setErr(errOrig);
    }

    // Some utilities

    private File createTmpFile() throws IOException {
        File tmpfile = temporaryFolder.newFile();
        tmpfile.deleteOnExit();
        return tmpfile;
    }

    private File createInputFile1() throws Exception {
        File file1 =  createTmpFile();
        FileWriter fileWriter = new FileWriter(file1);

        fileWriter.write("abcxyz");

        fileWriter.close();
        return file1;
    }

    private File createInputFile2() throws Exception {
        File file2 =  createTmpFile();
        FileWriter fileWriter = new FileWriter(file2);

        fileWriter.write("Howdy Billy,\n" +
                "I am going to take cs6300 and cs6400 next semester.\n" +
                "Did you take cs 6300 last semester? I want to\n" +
                "take 2 courses so that I will graduate Asap!");

        fileWriter.close();
        return file2;
    }





    private String getFileContent(String filename) {
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(filename)), charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    // test cases

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 1 from assignment directions
    @Test
    public void mainTest1() throws Exception {
        File inputFile1 = createInputFile1();

        String args[] = {inputFile1.getPath()};
        Main.main(args);

        String expected1 = "ghidef";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 2 from assignment directions
    @Test
    public void mainTest2() throws Exception {
        File inputFile2 = createInputFile2();

        String args[] = {"-r", inputFile2.getPath()};
        Main.main(args);

        String expected2 = "ydwoH ,ylliB\n" +
                "I ma gniog ot ekat 0036sc dna 0046sc txen .retsemes\n" +
                "diD uoy ekat sc 0036 tsal ?retsemes I tnaw ot\n" +
                "ekat 2 sesruoc os taht I lliw etaudarg !pasA";

        String actual2 = getFileContent(inputFile2.getPath());

        assertEquals("The files differ!", expected2, actual2);
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 3 from assignment directions
    @Test
    public void mainTest3() throws Exception {
        File inputFile2 = createInputFile2();

        String args[] = {"-d", "aeiou", "-c", "3", inputFile2.getPath()};
        Main.main(args);

        String expected3 = "Kzgb Eoob,\n"
                + "L p jqj w wn fv6300 qg fv6400 qaw vpvwu.\n"
                + "Gg b wn fv 6300 ovw vpvwu? L zqw w\n"
                + "wn 2 fuvv v wkw L zoo jugw Dvs!";

        String actual3 = getFileContent(inputFile2.getPath());

        assertEquals("The files differ!", expected3, actual3);
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 4 from assignment directions
    @Test
    public void mainTest4() throws Exception {
        File inputFile2 = createInputFile2();

        String args[] = {"-c", "2", inputFile2.getPath()};
        Main.main(args);

        String expected4 = "Jqyfa Dknna,\n"
                + "K co iqkpi vq vcmg eu6300 cpf eu6400 pgzv ugoguvgt.\n"
                + "Fkf aqw vcmg eu 6300 ncuv ugoguvgt? K ycpv vq\n"
                + "vcmg 2 eqwtugu uq vjcv K yknn itcfwcvg Cucr!";

        String actual4 = getFileContent(inputFile2.getPath());

        assertEquals("The files differ!", expected4, actual4);
    }


    // Purpose: To provide an example of a test case format
    // Frame #: Instructor error example
    @Test
    public void mainTest5() {
        String args[] = null; //invalid argument
        Main.main(args);
        assertEquals("Usage: Encode  [-c int] [-d string] [-r] <filename>", errStream.toString().trim());
    }

}