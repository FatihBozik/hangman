package com.example.fatih.hangman;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class SaveOverActivity extends ActionBarActivity {

    private int mPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_over);

        String word = getIntent().getStringExtra("WORD IDENTIFIER");
        TextView tv = (TextView) findViewById(R.id.word);
        tv.setText(word);

        int points = getIntent().getIntExtra("Points Identifier", 0);
        TextView scoreTextView = (TextView) findViewById(R.id.textViewPoints);
        scoreTextView.setText(String.valueOf(points));
        mPoints = points;
    }

    public void returnMenu(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void rePlay(View v) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void saveScore(View v) {
        SharedPreferences preferences = getSharedPreferences("MYPREFERENCE", MODE_PRIVATE);
        EditText editTextUserName = (EditText) findViewById(R.id.username);
        String name = editTextUserName.getText().toString();

        SharedPreferences.Editor editor = preferences.edit();
        String previousScore = preferences.getString("SCORES", "");
        editor.putString("SCORES", previousScore + "\n" + name + "\t " + mPoints + " Puan \n");
        editor.commit();

        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }




}
