package com.example.mysimpleinstagram;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Log.d("LoginActivity", "Login successful");

            final Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            usernameInput = findViewById(R.id.username_et);
            passwordInput = findViewById(R.id.password_et);
            loginBtn = findViewById(R.id.login_btn);

            loginBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    final String username = usernameInput.getText().toString();
                    final String password = passwordInput.getText().toString();

                    login(username, password);
                }
            });
        }
//        ParseUser.logOut();
//        ParseUser currentUser = ParseUser.getCurrentUser();
    }

//    ParseUser user = new ParseUser();
//    // Set core properties
//    user.setUsername("joestevens");
//    user.setPassword("secret123");
//    user.setEmail("email@example.com");
//    // Set custom properties
//    user.put("phone", "650-253-0000");
//    // Invoke signUpInBackground
//    user.signUpInBackground(new SignUpCallback() {
//        public void done(ParseException e) {
//            if (e == null) {
//                // Hooray! Let them use the app now.
//            } else {
//                // Sign up didn't succeed. Look at the ParseException
//                // to figure out what went wrong
//            }
//        }
//    });

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    Log.d("LoginActivity", "Login successful");

                    final Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("LoginActivity", "Login failure");
                    e.printStackTrace();
                }
            }
        });
    }
}
