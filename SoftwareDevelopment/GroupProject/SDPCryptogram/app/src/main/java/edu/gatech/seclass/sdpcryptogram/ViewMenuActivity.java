package edu.gatech.seclass.sdpcryptogram;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.gatech.seclass.sdpcryptogram.vo.User;

public class ViewMenuActivity extends AppCompatActivity {

    private Button incomplete;
    private Button complete;
    private Button statistics;
    private Button back;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_menu);

        Bundle bundle=getIntent().getExtras();
        user = (User) bundle.get("user");

        incomplete = (Button) findViewById(R.id.incomplete_button);
        complete = (Button) findViewById(R.id.complete_button);
        statistics = (Button) findViewById(R.id.statistics_button);
        back = (Button) findViewById(R.id.back_button);

    }

    /**
     * Move to ViewIncompleteCryptogramsActivity
     * @param view
     */
    public void handleViewIncompleteClick(View view) {
        final Context context=this;
        Intent intent = new Intent(context, ViewIncompleteCryptogramsActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }

    /**
     * Move to ViewCompleteCryptogramsActivity
     * @param view
     */
    public void handleViewCompleteClick(View view) {
        final Context context=this;
        Intent intent = new Intent(context, ViewCompleteCryptogramsActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }

    /**
     * Move to ViewStatisticsActivity
     * @param view
     */
    public void handleViewStatisticsClick(View view) {
        final Context context=this;
        Intent intent = new Intent(context, ViewStatisticsActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }

    /**
     * Move to MainMenuActivity
     * @param view
     */
    public void handleMainMenuClick(View view) {
        final Context context=this;
        Intent intent = new Intent(context, MainMenuActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }
}
