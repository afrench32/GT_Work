package edu.gatech.seclass.sdpcryptogram;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import edu.gatech.seclass.sdpcryptogram.vo.User;

public class MainMenuActivity extends AppCompatActivity {

    private Button create;
    private Button solve;
    private Button view;
    private Button logout;
    private User user;
    private TextView welcome;
    private TextView encoded_phrase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Bundle bundle = getIntent().getExtras();

        user = (User) bundle.get("user");
        create = (Button) findViewById(R.id.create_button);
        solve = (Button) findViewById(R.id.solve_button);
        view = (Button) findViewById(R.id.view_button);
        logout = (Button) findViewById(R.id.logout_button);
        welcome = (TextView) findViewById(R.id.welcome_tv);
        encoded_phrase = (TextView) findViewById(R.id.encoded_phrase);

        welcome.setText("Welcome, " + user.getFirstName() + " " + user.getLastName() + "!");

    }

    /**
     * Move to CreateCryptogramActivity
     * @param view
     */
    public void handleCreateClick(View view) {

        final Context context=this;
        Intent intent = new Intent(context, CreateCryptogramActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }

    /**
     * Move to ChooseCryptogramActivity.
     * @param view
     */
    public void handleSolveClick(View view) {

        final Context context=this;
        Intent intent = new Intent(context, ChooseCryptogramActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }

    /**
     * Move to ViewMenuActivity
     * @param view
     */
    public void handleViewClick(View view) {

        final Context context=this;
        Intent intent = new Intent(context, ViewMenuActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }

    /**
     * Logout and return to LandingPageActivity
     * @param view
     */
    public void handleLogoutClick(View view) {

        final Context context=this;
        Intent intent = new Intent(context, LandingPageActivity.class);
        startActivity(intent);
    }
}
