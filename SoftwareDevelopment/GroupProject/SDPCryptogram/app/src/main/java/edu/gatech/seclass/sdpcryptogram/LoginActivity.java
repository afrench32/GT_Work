package edu.gatech.seclass.sdpcryptogram;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

import edu.gatech.seclass.sdpcryptogram.database.CryptogramDBManager;
import edu.gatech.seclass.sdpcryptogram.vo.User;

//TODO: open database onCreate, close databse onDestroy?
//TODO: onPause, onResume?
public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private Button login;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username_et);
        login = (Button) findViewById(R.id.login_button);
        cancel = (Button) findViewById(R.id.cancel_button);
    }

    /**
     * Attempt to login with the username. Go to MainMenuActivity if successful
     *
     * @param view
     */
    public void handleLoginClick(View view) {

        final Context context=this;

        final String userNameEntered = username.getText().toString();
        boolean usernameError = false;


        if (userNameEntered.indexOf(" ") >= 0 || userNameEntered.length() > 15 ||
                userNameEntered.length() < 1) {
            usernameError = true;
            username.setError("Username must be from 1-15 characters. No spaces!");
        }

        if (!usernameError) {

            final CryptogramDBManager cryptogramDBManager= new CryptogramDBManager(this);
            User user  = cryptogramDBManager.getUser(userNameEntered);
            cryptogramDBManager.close();

            if(user != null){

                //Todo Implement Log4j Logging.
                System.out.println("User Successfully Logged In with First Name :: "+user.getFirstName()+ " Last Name as ::"+user.getLastName());
                Intent intent = new Intent(context, MainMenuActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
            else {
                username.setError("No User Registered with User Name as :: "+userNameEntered);
            }
        }
    }

    /**
     * Return to LandingPageActivity
     * @param view
     */
    public void handleCancelClick(View view) {

        final Context context=this;
        Intent intent = new Intent(context, LandingPageActivity.class);
        startActivity(intent);

    }
}
