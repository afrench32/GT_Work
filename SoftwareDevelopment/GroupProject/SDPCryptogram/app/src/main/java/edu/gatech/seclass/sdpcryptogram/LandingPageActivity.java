package edu.gatech.seclass.sdpcryptogram;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LandingPageActivity extends AppCompatActivity {

    private Button login;
    private Button createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        login = (Button) findViewById(R.id.login_button);
        createAccount = (Button) findViewById(R.id.ca_button);

    }

    /**
     * Go to LoginActivity
     * @param view
     */
    public void handleLoginClick(View view) {
        final Context context=this;
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Go to CreateAccountActivity
     * @param view
     */
    public void handleCreateAccountClick(View view) {
        final Context context=this;
        Intent intent = new Intent(context, CreateAccountActivity.class);
        startActivity(intent);
    }
}
