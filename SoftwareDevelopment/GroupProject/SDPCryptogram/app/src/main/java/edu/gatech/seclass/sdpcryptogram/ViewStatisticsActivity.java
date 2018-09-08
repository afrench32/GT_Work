package edu.gatech.seclass.sdpcryptogram;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.gatech.seclass.sdpcryptogram.database.CryptogramDBManager;
import edu.gatech.seclass.sdpcryptogram.vo.Attempt;
import edu.gatech.seclass.sdpcryptogram.vo.Cryptogram;
import edu.gatech.seclass.sdpcryptogram.vo.User;

public class ViewStatisticsActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_statistics);

        Bundle bundle=getIntent().getExtras();
        user = (User) bundle.get("user");

        TextView statisticsText = (TextView)findViewById(R.id.statistics_text);
        statisticsText.setText(getAllStatistics());
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

    String getAllStatistics (){

        final CryptogramDBManager cryptogramDBManager= new CryptogramDBManager(this);
        List<Cryptogram> allCryptograms = cryptogramDBManager.getAllCryptograms();
        List<Attempt> allAttempts = cryptogramDBManager.getAllAttempts();
        cryptogramDBManager.close();

        if (allCryptograms.size() == 0) {
            return "There are no cryptograms! Why don't you make one?";
        }

        StringBuilder completedAttemptString = new StringBuilder();

        int i = 1;

        for (Cryptogram cryptogram:allCryptograms) {

            String puzzleName = cryptogram.getPuzzleName();
            String dateCreated = cryptogram.getDateCreated();

            completedAttemptString.append(i++).append(") ").append(puzzleName);
            completedAttemptString.append("\n\t\tCreated on: ").append(dateCreated);

            ArrayList<Attempt> solvedThisPuzzle = new ArrayList<Attempt>();
            for (Attempt attempt:allAttempts) {
                if (attempt.getPuzzleName().equals(puzzleName) && attempt.getSolved().equals("true")) {
                    solvedThisPuzzle.add(attempt);
                }
            }

            int numberCompleted = solvedThisPuzzle.size();
            completedAttemptString.append("\n\t\tNumber solved: ").append(numberCompleted);

            ArrayList<String> dateList = new ArrayList<String>();
            for (Attempt attempt:solvedThisPuzzle) {
                dateList.add(attempt.getDateCompleted());
            }
            Collections.sort(dateList);

            completedAttemptString.append("\n\t\tFirst three to solve: ");

            for (int j = 0; j < Math.min(3, dateList.size()); j++) {
                System.err.println("HERE!");
                for (Attempt attempt:solvedThisPuzzle) {
                    if (attempt.getDateCompleted().equals(dateList.get(j))) {
                        completedAttemptString.append(attempt.getUsername()).append(", ");
                    }
                }
            }

            completedAttemptString.append("\n");
        }

        return completedAttemptString.toString();

    }
}
