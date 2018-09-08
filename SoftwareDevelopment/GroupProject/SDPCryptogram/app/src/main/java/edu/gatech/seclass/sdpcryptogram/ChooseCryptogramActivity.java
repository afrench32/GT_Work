package edu.gatech.seclass.sdpcryptogram;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import edu.gatech.seclass.sdpcryptogram.database.CryptogramDBManager;
import edu.gatech.seclass.sdpcryptogram.vo.Cryptogram;
import edu.gatech.seclass.sdpcryptogram.vo.User;
import edu.gatech.seclass.sdpcryptogram.vo.Attempt;

public class ChooseCryptogramActivity extends AppCompatActivity {

    private EditText puzzleName;
    private User user;
    private TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_cryptogram);

        Bundle bundle = getIntent().getExtras();
        //TODO: examine this... we're getting a string, how is it cast as a user? working ok so far
        user = (User) bundle.get("user");
        puzzleName = (EditText) findViewById(R.id.puzzle_name);
        welcome = (TextView) findViewById(R.id.welcome_tv);

        welcome.setText("Enter a cryptogram to solve, " + user.getFirstName() + " " + user.getLastName() + "!");

    }

    /**
     * Go to SolveCryptogramActivity, solving puzzleName
     * @param view
     */
    public void handleSolveClick(View view) {

        final Context context = this;

        final CryptogramDBManager cryptogramDBManager = new CryptogramDBManager(this);

        final String puzzleNameSelected = puzzleName.getText().toString();
        String userName = user.getUserName();

        boolean puzzleExistsError = false;
        boolean userCreatedError = false;
        boolean attemptExists = true;

        Cryptogram cryptogram = cryptogramDBManager.getCrypto(puzzleNameSelected);

        if (cryptogram == null) {
            puzzleExistsError = true;
            puzzleName.setError("Puzzle does not exist!");
        }

        if (!puzzleExistsError) {
            System.err.println(cryptogram.getCreatedBy() + ":" + user.getUserName());
            if (cryptogram.getCreatedBy().equals(user.getUserName())) {
                userCreatedError = true;
                puzzleName.setError("Puzzle created by you!");
            }
        }

        if (!puzzleExistsError && !userCreatedError) {
            Attempt attempt = cryptogramDBManager.getAttempt(
                    user.getUserName(), puzzleNameSelected);
            if (attempt == null) {
                System.err.println("NO SUCH ATTEMPT");
                Intent intent = new Intent(context, SolveCryptogramActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("puzzle", cryptogram.getPuzzleName());
                startActivity(intent);
            }
            else {
                System.err.println(attempt.getAttemptsMade() + "made, " + attempt.getAttemptsAllowed() + " allowed");
                if (Boolean.parseBoolean(attempt.getSolved()) ||
                        Integer.parseInt(attempt.getAttemptsMade()) >=
                                Integer.parseInt(attempt.getAttemptsAllowed())) {
                    puzzleName.setError("You've finished this puzzle!");
                }
                else {
                    Intent intent = new Intent(context, SolveCryptogramActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("puzzle", cryptogram.getPuzzleName());
                    startActivity(intent);
                }
            }
        }
    }

    /**
     * Go back to MainMenuActivity
     * @param view
     */
    public void handleCancelClick(View view) {

        final Context context=this;
        Intent intent = new Intent(context, MainMenuActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);

    }
}
