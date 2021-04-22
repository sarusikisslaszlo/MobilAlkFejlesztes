package com.example.healthcareservice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View view) {
        EditText username = findViewById(R.id.editTextUserName);
        EditText password = findViewById(R.id.editTextPassword);

        String userNameString = username.getText().toString();
        String passwordString = password.getText().toString();

        Log.i(LOG_TAG, "Bejelentkezett: " + userNameString + ", jelszó: " + passwordString);
    }
}