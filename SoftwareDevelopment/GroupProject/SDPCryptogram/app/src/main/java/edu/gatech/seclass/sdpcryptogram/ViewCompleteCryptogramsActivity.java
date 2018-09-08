package edu.gatech.seclass.sdpcryptogram;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.seclass.sdpcryptogram.database.CryptogramDBManager;
import edu.gatech.seclass.sdpcryptogram.vo.Attempt;
import edu.gatech.seclass.sdpcryptogram.vo.User;

public class ViewCompleteCryptogramsActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_complete_cryptograms);

        Bundle bundle=getIntent().getExtras();
        user=(User)bundle.get("user");

        TextView completeCryptogramText = (TextView)findViewById(R.id.complete_cryptograms_text);
        completeCryptogramText.setText(getAllCompletedCryptogram());
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


    String getAllCompletedCryptogram(){

        final CryptogramDBManager cryptogramDBManager= new CryptogramDBManager(this);
        List<Attempt> allAttempts = cryptogramDBManager.getAllAttempts();
        cryptogramDBManager.close();

        StringBuilder completedAttemptString = new StringBuilder();

        ArrayList<Attempt> completedPuzzles = new ArrayList<Attempt>();

        for(int i = 0; i < allAttempts.size(); i++) {
            Attempt currAttempt = allAttempts.get(i);
            if (currAttempt.getUsername().equals(user.getUserName()) &&
                    (currAttempt.getSolved().equals("true") ||
                            Integer.parseInt(currAttempt.getAttemptsMade()) ==
                                    Integer.parseInt(currAttempt.getAttemptsAllowed()))) {
                completedPuzzles.add(currAttempt);

            }
        }

        if (completedPuzzles.size() == 0) {
            return "You've completed no puzzles!";
        }

        int i = 1;
        for(Attempt attempt:completedPuzzles){
            completedAttemptString.append(i++).append(") ").append(attempt.getPuzzleName()).append(
                    "\n\t\tSolved: ").append(attempt.getSolved()).append(
                            "\n\t\tDate Completed: ").append(attempt.getDateCompleted()).append("\n");
        }

        return completedAttemptString.toString();
    }
}
