package edu.gatech.seclass.sdpcryptogram;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.gatech.seclass.sdpcryptogram.database.CryptogramDBManager;
import edu.gatech.seclass.sdpcryptogram.vo.User;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText username;
    private EditText email;
    private Button createAccountButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        firstName = (EditText) findViewById(R.id.first_name_et);
        lastName = (EditText) findViewById(R.id.last_name_et);
        username = (EditText) findViewById(R.id.username_et);
        email = (EditText) findViewById(R.id.email_et);
        createAccountButton = (Button) findViewById(R.id.create_account);
        cancelButton = (Button) findViewById(R.id.cancel_button);
    }

    /**
     * Attempt to create the account. If successful, login and move to MainMenuActivity
     * @param view
     */
    public void handleCreateAccountClick(View view) {

        final Context context = this;

        final String userNameEntered = username.getText().toString();
        final String firstNameEntered = firstName.getText().toString();
        final String lastNameEntered = lastName.getText().toString();
        final String emailEntered = email.getText().toString();

        boolean usernameError = false;
        boolean firstNameError = false;
        boolean lastNameError = false;
        boolean emailError = false;

        if (userNameEntered.indexOf(" ") >= 0 || userNameEntered.length() > 15 ||
                userNameEntered.length() < 1) {
            usernameError = true;
            username.setError("Username must be from 1-15 characters. No spaces!");
        }

        if (firstNameEntered.indexOf(" ") >= 0 || firstNameEntered.length() > 15 ||
                firstNameEntered.length() < 1) {
            firstNameError = true;
            firstName.setError("Username must be from 1-15 characters. No spaces!");
        }

        if (lastNameEntered.indexOf(" ") >= 0 || lastNameEntered.length() > 15 ||
                lastNameEntered.length() < 1) {
            lastNameError = true;
            lastName.setError("Username must be from 1-15 characters. No spaces!");
        }

        if (!isValidEmail(emailEntered)) {
            emailError = true;
            email.setError("Email address must be valid!");
        }

        if(!usernameError && !firstNameError && !lastNameError && !emailError) {

            User newUser = new User(firstNameEntered, lastNameEntered, userNameEntered,
                    emailEntered);

            final CryptogramDBManager cryptogramDBManager= new CryptogramDBManager(this);
            Boolean success = cryptogramDBManager.insertUser(newUser);

            if (success) {

                //Todo Implement Log4j Logging.
                System.out.println("User Successfully Created  with First Name: " +
                        newUser.getFirstName() + " Last Name:" + newUser.getLastName());
                Intent intent = new Intent(context, MainMenuActivity.class);
                intent.putExtra("user", newUser);
                startActivity(intent);
            }

            else {
                username.setError("Username " + userNameEntered + " already exists!");
            }

            //TODO: does it get here if a new activity starts?
            cryptogramDBManager.close();
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

    /**
     * Email address validator
     * From https://www.geeksforgeeks.org/check-email-address-valid-not-java/
     * @param email
     * @return true if a valid email address, false if not
     */
    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}
