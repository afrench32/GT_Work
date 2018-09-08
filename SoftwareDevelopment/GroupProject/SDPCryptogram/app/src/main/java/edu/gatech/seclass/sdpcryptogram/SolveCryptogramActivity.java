package edu.gatech.seclass.sdpcryptogram;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;

import edu.gatech.seclass.sdpcryptogram.database.CryptogramDBManager;
import edu.gatech.seclass.sdpcryptogram.vo.Cryptogram;
import edu.gatech.seclass.sdpcryptogram.vo.User;
import edu.gatech.seclass.sdpcryptogram.vo.Attempt;

// TODO: still lets you try to solve a puzzle that you have successfully solved
public class SolveCryptogramActivity extends AppCompatActivity {

    private TextView welcome_tv;
    private TextView puzzleName_tv;
    private TextView attempts_tv;
    private TextView encoded_tv;
    private TextView decoded_tv;
    private EditText et_a, et_b, et_c, et_d, et_e, et_f, et_g, et_h, et_i, et_j, et_k, et_l, et_m;
    private EditText et_n, et_o, et_p, et_q, et_r, et_s, et_t, et_u, et_v, et_w, et_x, et_y, et_z;
    private EditText listOfEditTexts[] = new EditText[26];

    private User user;
    private Cryptogram cryptogram;
    private Attempt attempt;
    private String puzzleNameToSolve;
    private String encoded;
    private Map<String, String> cypher = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve_cryptogram);

        Bundle bundle = getIntent().getExtras();
        user = (User) bundle.get("user");
        puzzleNameToSolve = bundle.getString("puzzle");

        CryptogramDBManager cryptogramDBManager = new CryptogramDBManager(this);
        cryptogram = cryptogramDBManager.getCrypto(puzzleNameToSolve);
        encoded = cryptogram.getEncoded();
        attempt = cryptogramDBManager.getAttempt(user.getUserName(), puzzleNameToSolve);
        if (attempt == null) {
            attempt = new Attempt(user.getUserName(), puzzleNameToSolve, 0,
                    Integer.parseInt(cryptogram.getAttemptsAllowed()), false, "");
        }
        cryptogramDBManager.close();

        welcome_tv = (TextView) findViewById(R.id.welcome_tv);
        puzzleName_tv = (TextView) findViewById(R.id.puzzle_name);
        attempts_tv = (TextView) findViewById(R.id.attempts_tv);
        encoded_tv = (TextView) findViewById(R.id.encoded_phrase);
        decoded_tv = (TextView) findViewById(R.id.decoded_phrase);

        welcome_tv.setText("Decode the cryptogram, " + user.getFirstName() + " " +
                user.getLastName() + "!");
        puzzleName_tv.setText("You are solving: " + puzzleNameToSolve);
        attempts_tv.setText("You have taken " + attempt.getAttemptsMade() + " attempts (out of " +
                attempt.getAttemptsAllowed() + ")");
        encoded_tv.setText(encoded);

        initializeCypher();
        setEditTexts();

        decoded_tv.setText(decode(encoded));

    }

    /**
     * Decodes the string based on the cypher currently entered
     * @param view
     */
    public void handleUseCypherClick(View view) {

        String lowerCharacters[]  = { "a", "b", "c", "d", "e", "f", "g", "h", "i",
                "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
                "w", "x", "y", "z" };
        boolean allGood = true;

        String enc = encoded.toLowerCase();

        // boolean array for each letter: is it in the encoded phrase?
        boolean[] charsinEncLower = new boolean[26];
        for (int i = 0; i < 26; i++) {
            String letter = lowerCharacters[i];
            if (enc.contains(letter)) {
                charsinEncLower[i] = true;
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
                listOfEditTexts[i].setText("Must be a single letter!");

            }
        }

        // yell at the player for entering letters he/she didn't need to
        for (int i = 0; i < 26; i++) {
            if (!charsinEncLower[i] && playerEnteredLetters[i].length() > 0) {
                allGood = false;
                listOfEditTexts[i].setError("No need to enter this letter!");
            }
        }

        // yell at the player for not entering letters he/she needed to
        for (int i = 0; i < 26; i++) {
            if (charsinEncLower[i] && !(playerEnteredLetters[i].length() > 0)) {
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
                if (charsinEncLower[i]) {
                    cypher.put(lowerCharacters[i], playerEnteredLetters[i]);
                    cypher.put(lowerCharacters[i].toUpperCase(), playerEnteredLetters[i].toUpperCase());
                }
            }
            decoded_tv.setText(decode(encoded));
        }

    }

    /**
     * Reset cypher to original encoded phrase
     * @param view
     */
    public void handleResetCypherClick(View view) {
        initializeCypher();
        setEditTexts();
        decoded_tv.setText(decode(encoded));
    }

    /**
     * Enter guess as a solution. Check against entry in database, give feedback and return to
     * MainMenuActivity
     * @param view
     */
    public void handleEnterSolutionClick(View view) {

        int newAttempts = Integer.parseInt(attempt.getAttemptsMade()) + 1;
        String decoded = decode(encoded);
        boolean newSolved = false;
        if (decoded.equals(cryptogram.getDecoded())) {
            System.err.println("SOLVED IT!");
            newSolved = true;
        }

        boolean completed = false;
        String currTime = "";
        if (newSolved || newAttempts == Integer.parseInt(attempt.getAttemptsAllowed())) {
            completed = true;
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 1);
            Date date = cal.getTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String dateString = format.format(date);
            System.err.println(dateString);
            currTime = dateString;
        }

        final Context context = this;
        final CryptogramDBManager cryptogramDBManager= new CryptogramDBManager(this);
        Attempt newAttempt = new Attempt(user.getUserName(), puzzleNameToSolve, newAttempts,
                Integer.parseInt(cryptogram.getAttemptsAllowed()), newSolved, currTime);
        if (Integer.parseInt(attempt.getAttemptsMade()) == 0) {
            cryptogramDBManager.insertAttempt(newAttempt);
        }
        else {
            cryptogramDBManager.updateAttempt(newAttempt);
        }
        cryptogramDBManager.close();

        // if correct, toast and back to main menu
        if (newSolved) {
            Toast.makeText(this, "You solved the puzzle!",
                    Toast.LENGTH_LONG).show();
            try{ Thread.sleep(1000); }catch(InterruptedException e){ }
            Intent intent = new Intent(context, MainMenuActivity.class);
            intent.putExtra("user",user);
            startActivity(intent);
        }
        // if incorrect and has more attempts, toast and stay (need to reset variables tv at top)
        if (!newSolved && !completed) {
            Toast.makeText(this, "Not correct, give it another try!",
                    Toast.LENGTH_LONG).show();
            attempt = newAttempt;
            attempts_tv.setText("You have taken " + attempt.getAttemptsMade() + " attempts (out of " +
                    attempt.getAttemptsAllowed() + ")");
            initializeCypher();
            setEditTexts();
            decoded_tv.setText(decode(encoded));
        }
        // if incorrect with no more attempts, toast and back to main menu
        if (!newSolved && completed) {
            Toast.makeText(this, "Sorry, you're out of attempts!",
                    Toast.LENGTH_LONG).show();
            try{ Thread.sleep(1000); }catch(InterruptedException e){ }
            Intent intent = new Intent(context, MainMenuActivity.class);
            intent.putExtra("user",user);
            startActivity(intent);
        }

    }

    /**
     * Return to MainMenuActivity
     * @param view
     */
    public void handleCancelClick(View view) {
        final Context context=this;
        Intent intent = new Intent(context, MainMenuActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }

    /**
     * Encodes the solution string using the current cypher
     * @param encoded, the clue string
     * @return decoded_phrase, the solution string
     */
    private String decode(String encoded){

        String decoded_phrase = "";
        int sol_len = encoded.length();
        for (int i = 0; i < sol_len; i++) {
            if (cypher.containsKey(String.valueOf(encoded.charAt(i)))) {
                decoded_phrase += cypher.get(String.valueOf(encoded.charAt(i)));
            }
            else {
                decoded_phrase += String.valueOf(encoded.charAt(i));
            }
        }
        return decoded_phrase;
    }

    /**
     * Set initial cypher to map encoded letters to themselves
     * TODO: this is ugly and not super efficient, but it works. change if there's time
     */
    private void initializeCypher() {

        cypher.clear();

        String sol = cryptogram.getDecoded();
        String enc = cryptogram.getEncoded();

        String lowerCharacters[]  = { "a", "b", "c", "d", "e", "f", "g", "h", "i",
                "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
                "w", "x", "y", "z" };

        ArrayList<String> charsInSolLower = new ArrayList<>();
        ArrayList<String> substituteCharsLower = new ArrayList<>();

        for (int i = 0; i < sol.length(); i++) {
            if (!charsInSolLower.contains(String.valueOf(sol.charAt((i))).toLowerCase()) &&
                    Arrays.asList(lowerCharacters).contains(String.valueOf(sol.charAt(i)).toLowerCase())) {

                charsInSolLower.add(String.valueOf(sol.charAt(i)).toLowerCase());
                substituteCharsLower.add(String.valueOf(enc.charAt(i)).toLowerCase());
            }
        }

        for (int i = 0; i < charsInSolLower.size(); i++) {
            cypher.put(substituteCharsLower.get(i), substituteCharsLower.get(i));
            cypher.put(substituteCharsLower.get(i).toUpperCase(),
                    substituteCharsLower.get(i).toUpperCase());
        }

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
