package edu.gatech.seclass.sdpcryptogram;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

import edu.gatech.seclass.sdpcryptogram.database.CryptogramDBManager;
import edu.gatech.seclass.sdpcryptogram.vo.Cryptogram;
import edu.gatech.seclass.sdpcryptogram.vo.User;

// TODO confirmation message
public class CreateCryptogramActivity extends AppCompatActivity {

    private TextView welcome;
    private EditText puzzleName;
    private EditText solution;
    private TextView encoded_phrase;
    private EditText allowedAttempts;
    private EditText et_a, et_b, et_c, et_d, et_e, et_f, et_g, et_h, et_i, et_j, et_k, et_l, et_m;
    private EditText et_n, et_o, et_p, et_q, et_r, et_s, et_t, et_u, et_v, et_w, et_x, et_y, et_z;
    private EditText listOfEditTexts[] = new EditText[26];

    private User user;
    private Map<String, String> cypher = new HashMap<String, String>();
    private Random rgen = new Random(219253192);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_cryptogram);

        Bundle bundle = getIntent().getExtras();
        //TODO: this may give a null pointer?
        user = (User) bundle.get("user");

        welcome = findViewById(R.id.welcome_tv);
        puzzleName = findViewById(R.id.puzzle_name_et);
        solution = findViewById(R.id.solution_et);
        encoded_phrase = findViewById(R.id.encoded_phrase);
        allowedAttempts = findViewById(R.id.allowed_attempts_et);

        welcome.setText("Build your cryptogram, " + user.getFirstName() + " " + user.getLastName() + "!");

        randomizeCypher();
        setEditTexts();

        encoded_phrase.setText(encode(solution.getText().toString()));

    }

    public void handleEncodeSolutionClick(View view) {

        encoded_phrase.setText(encode(solution.getText().toString()));
        setEditTexts();

    }

    public void handleCustomizeCypherClick(View view) {

        String lowerCharacters[]  = { "a", "b", "c", "d", "e", "f", "g", "h", "i",
                "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
                "w", "x", "y", "z" };
        boolean allGood = true;

        String sol = solution.getText().toString().toLowerCase();
        System.err.println(sol);

        // boolean array for each letter: is it in the solution?
        boolean[] charsInSolLower = new boolean[26];
        for (int i = 0; i < 26; i++) {
            String letter = lowerCharacters[i];
            if (sol.contains(letter)) {
                charsInSolLower[i] = true;
            }
        }

        // get all letters that the player entered
        String[] playerEnteredLetters = new String[26];
        for (int i = 0; i < 26; i++) {
            playerEnteredLetters[i] = listOfEditTexts[i].getText().toString().toLowerCase();
        }

        // yell at the player if any entries have 2+ characters or aren't letters
        for (int i = 0; i < 26; i++) {
            if (playerEnteredLetters[i].length() > 1 || !(Arrays.asList(lowerCharacters).contains(
                    playerEnteredLetters[i]) || playerEnteredLetters[i].length() == 0)) {
                allGood = false;
                listOfEditTexts[i].setError("Must be a single letter");
            }
        }

        // yell at the player for entering letters he/she didn't need to
        for (int i = 0; i < 26; i++) {
            if (!charsInSolLower[i] && playerEnteredLetters[i].length() > 0) {
                allGood = false;
                listOfEditTexts[i].setError("No need to enter this letter!");
            }
        }

        // yell at the player for not entering letters he/she needed to
        for (int i = 0; i < 26; i++) {
            if (charsInSolLower[i] && !(playerEnteredLetters[i].length() > 0)) {
                allGood = false;
                listOfEditTexts[i].setError("Must enter this letter!");
            }
        }

        // yell at the player for repeat characters in cypher
        boolean duplicated = false;
        for (int i = 0; i < 26; i++) {
            for (int j = 0; j < 26; j++) {
                if (i != j && playerEnteredLetters[i].equals(playerEnteredLetters[j])
                        && playerEnteredLetters[i].length() > 0) {
                    allGood = false;
                    listOfEditTexts[i].setError("Duplicate with substitute for " +
                            lowerCharacters[j]);
                    listOfEditTexts[j].setError("Duplicate with substitute for " +
                            lowerCharacters[i]);
                }
            }
        }

        // update cypher if all is well
        if (allGood) {
            System.err.println("All good!");
            cypher.clear();
            for (int i = 0; i < 26; i++) {
                if (charsInSolLower[i]) {
                    cypher.put(lowerCharacters[i], playerEnteredLetters[i]);
                    cypher.put(lowerCharacters[i].toUpperCase(), playerEnteredLetters[i].toUpperCase());
                }
            }
            encoded_phrase.setText(encode(solution.getText().toString()));
        }

    }

    /**
     * Randomize the cypher
     * @param view
     */
    public void handleRandomizeCypherClick(View view) {

        randomizeCypher();
        setEditTexts();
        encoded_phrase.setText(encode(solution.getText().toString()));

    }

    /**
     * Attempt to save the cryptogram to the database. If successful return to MainMenuActivity
     * @param view
     */
    public void handleSaveClick (View view) {

        final Context context = this;

        final CryptogramDBManager cryptogramDBManager= new CryptogramDBManager(this);

        final String puzzleNameEntered = puzzleName.getText().toString();
        final String decoded = solution.getText().toString();
        final String allowedAttemptsStr = allowedAttempts.getText().toString();
        final String encoded;
        Boolean success = false;

        encoded = encode(decoded);

        final String createdBy = user.getUserName();

        boolean puzzleNameError = false;
        boolean solutionError = false;
        boolean allowedAttemptsError = false;

        if (puzzleNameEntered.length() < 1 || puzzleNameEntered.length() > 15 ||
                puzzleNameEntered.contains(" ")) {
            puzzleNameError = true;
            puzzleName.setError("Puzzle name must be from 1-15 characters. No spaces!");
        }
        if (decoded.length() < 1) {
            solutionError = true;
            solution.setError("Solution must have at least one character!");
        }
        if (!isPositiveInteger(allowedAttemptsStr)) {
            allowedAttemptsError = true;
            allowedAttempts.setError("Must be positive integer!");
        }

        if (!puzzleNameError && !allowedAttemptsError && !solutionError) {

            String currTime;
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 1);
            Date date = cal.getTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String dateString = format.format(date);
            System.err.println(dateString);
            currTime = dateString;

            Cryptogram cryptogram = new Cryptogram(encoded, decoded, createdBy,
                   puzzleNameEntered, allowedAttemptsStr, currTime);
            success = cryptogramDBManager.insertCryptogram(cryptogram);

            if (success) {

                System.out.println(user.getUserName() + " successfully created cryptogram " +
                        puzzleNameEntered + "\n\tsolution: " + decoded + "\n\tencoded: " + encoded);

                Intent intent = new Intent(context, MainMenuActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
            else {
                puzzleName.setError("Puzzle name already exists!");
            }

            //TODO: does it get here if a new activity starts?
            cryptogramDBManager.close();
        }
    }

    /**
     * Return to MainMenuActivity
     * @param view
     */
    public void handleCancelClick(View view) {

        final Context context=this;
        Intent intent = new Intent(context, MainMenuActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    /**
     * Randomizes the cypher
     */
    private void randomizeCypher() {

        cypher.clear();

        String sol = solution.getText().toString();

        String lowerCharacters[]  = { "a", "b", "c", "d", "e", "f", "g", "h", "i",
                "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
                "w", "x", "y", "z" };

        ArrayList<String> charsInSolLower = new ArrayList<>();
        ArrayList<String> substituteCharsLower = new ArrayList<>();

        for (int i = 0; i < sol.length(); i++) {
            if (!charsInSolLower.contains(String.valueOf(sol.charAt(i)).toLowerCase()) &&
                    Arrays.asList(lowerCharacters).contains(String.valueOf(sol.charAt(i)).toLowerCase())) {

                charsInSolLower.add(String.valueOf(sol.charAt(i)).toLowerCase());

                boolean goodChar = false;
                while (!goodChar) {
                    int randPos = rgen.nextInt(26);
                    String letter = lowerCharacters[randPos];
                    if (!substituteCharsLower.contains(letter)) {
                        substituteCharsLower.add(letter);
                        goodChar = true;
                    }
                }


            }
        }

        for (int i = 0; i < charsInSolLower.size(); i++) {
            cypher.put(charsInSolLower.get(i), substituteCharsLower.get(i));
            cypher.put(charsInSolLower.get(i).toUpperCase(),
                    substituteCharsLower.get(i).toUpperCase());
        }
    }

    /**
     * Encodes the solution string using the current cypher
     * @param decoded, the solution string
     * @return encoded string
     */
    private String encode(String decoded){

        String encoded_phrase = "";
        int sol_len = decoded.length();
        for (int i = 0; i < sol_len; i++) {
            if (cypher.containsKey(String.valueOf(decoded.charAt(i)))) {
                encoded_phrase += cypher.get(String.valueOf(decoded.charAt(i)));
            }
            else {
                encoded_phrase += String.valueOf(decoded.charAt(i));
            }
        }
        return encoded_phrase;
    }

    /**
     * Determine if string s is positive integer
     * @param s, string
     * @return boolean representing positive integer-ness of s
     */
    private static boolean isPositiveInteger(String s) {

        boolean isValidInteger = false;
        try {
            int si = Integer.parseInt(s);
            if (si > 0) {
                isValidInteger = true;
            }
        }
        catch (NumberFormatException ex) {
            // s is not an integer
        }
        return isValidInteger;
    }

    /**
     * initializes and sets text for EditText objects
     * TODO: move initialization to onCreate or new method? inefficient
     */
    private void setEditTexts() {

        String lowerCharacters[]  = { "a", "b", "c", "d", "e", "f", "g", "h", "i",
                "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
                "w", "x", "y", "z" };

        et_a = findViewById(R.id.et_a);
        et_b = findViewById(R.id.et_b);
        et_c = findViewById(R.id.et_c);
        et_d = findViewById(R.id.et_d);
        et_e = findViewById(R.id.et_e);
        et_f = findViewById(R.id.et_f);
        et_g = findViewById(R.id.et_g);
        et_h = findViewById(R.id.et_h);
        et_i = findViewById(R.id.et_i);
        et_j = findViewById(R.id.et_j);
        et_k = findViewById(R.id.et_k);
        et_l = findViewById(R.id.et_l);
        et_m = findViewById(R.id.et_m);
        et_n = findViewById(R.id.et_n);
        et_o = findViewById(R.id.et_o);
        et_p = findViewById(R.id.et_p);
        et_q = findViewById(R.id.et_q);
        et_r = findViewById(R.id.et_r);
        et_s = findViewById(R.id.et_s);
        et_t = findViewById(R.id.et_t);
        et_u = findViewById(R.id.et_u);
        et_v = findViewById(R.id.et_v);
        et_w = findViewById(R.id.et_w);
        et_x = findViewById(R.id.et_x);
        et_y = findViewById(R.id.et_y);
        et_z = findViewById(R.id.et_z);

        EditText[] templist = {et_a, et_b, et_c, et_d, et_e, et_f, et_g, et_h, et_i, et_j
                , et_k, et_l, et_m, et_n, et_o, et_p, et_q, et_r, et_s, et_t, et_u, et_v, et_w
                , et_x, et_y, et_z};
        for (int i = 0; i < 26; i++) {
            listOfEditTexts[i] = templist[i];
        }

        for (int i = 0; i < 26; i++) {
            listOfEditTexts[i].setText(cypher.get(lowerCharacters[i]));
        }
    }

}
