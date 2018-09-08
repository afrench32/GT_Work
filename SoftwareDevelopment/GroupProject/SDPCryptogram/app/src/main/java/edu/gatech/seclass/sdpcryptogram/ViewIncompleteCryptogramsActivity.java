package edu.gatech.seclass.sdpcryptogram;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import edu.gatech.seclass.sdpcryptogram.database.CryptogramDBManager;
import edu.gatech.seclass.sdpcryptogram.vo.Attempt;
import edu.gatech.seclass.sdpcryptogram.vo.Cryptogram;
import edu.gatech.seclass.sdpcryptogram.vo.User;

public class ViewIncompleteCryptogramsActivity extends AppCompatActivity {

    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_incomplete_cryptograms);

        Bundle bundle=getIntent().getExtras();
        user=(User)bundle.get("user");

        TextView inCompleteCryptogram = (TextView)findViewById(R.id.incomplete_cryptograms_text);
        inCompleteCryptogram.setText(getAllInCompletedCryptogram());
    }

    /**
     * Return to ViewMenuActivity
     * @param view
     */
    public void handleCancelClick(View view) {
        final Context context=this;
        Intent intent = new Intent(context, ViewMenuActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }

    String getAllInCompletedCryptogram(){

        final CryptogramDBManager cryptogramDBManager= new CryptogramDBManager(this);
        List<Cryptogram> allCryptograms = cryptogramDBManager.getAllCryptograms();
        List<Attempt> allAttempts = cryptogramDBManager.getAllAttempts();
        cryptogramDBManager.close();

        ArrayList<Attempt> completedPuzzles = new ArrayList<Attempt>();
        ArrayList<Attempt> incompletePuzzles = new ArrayList<Attempt>();
        ArrayList<Attempt> allAttemptsByUser = new ArrayList<Attempt>();


        for(int i = 0; i < allAttempts.size(); i++) {
            Attempt currAttempt = allAttempts.get(i);
            if (currAttempt.getUsername().equals(user.getUserName())) {
                allAttemptsByUser.add(currAttempt);
                if (currAttempt.getSolved().equals("true") ||
                        Integer.parseInt(currAttempt.getAttemptsMade()) ==
                                Integer.parseInt(currAttempt.getAttemptsAllowed())) {
                    completedPuzzles.add(currAttempt);
                }
                else {
                    incompletePuzzles.add(currAttempt);
                }
            }
        }

        for (Cryptogram cryptogram:allCryptograms) {
            boolean hasTried = false;
            for (Attempt attempt:allAttemptsByUser) {
                if (cryptogram.getPuzzleName().equals(attempt.getPuzzleName())) {
                    hasTried = true;
                }
            }
            if (!hasTried) {
                incompletePuzzles.add(new Attempt(user.getUserName(), cryptogram.getPuzzleName(),
                        0, Integer.parseInt(cryptogram.getAttemptsAllowed()),
                        false, "" ));
            }
        }

        if (incompletePuzzles.size() == 0) {
            return "You've completed all the puzzles!";
        }


        StringBuilder completedAttemptString = new StringBuilder();

        int i = 1;
        for(Attempt attempt:incompletePuzzles) {
            completedAttemptString.append(i++).append(") ").append(attempt.getPuzzleName()).append(
                    "\n\t\tNumber of attempts: ").append(attempt.getAttemptsMade()).append("\n");
        }

        return completedAttemptString.toString();
    }
}
