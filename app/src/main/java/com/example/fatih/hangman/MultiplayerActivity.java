package com.example.fatih.hangman;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;



public class MultiplayerActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);
    }

    public void play(View v) {
        EditText editText = (EditText) findViewById(R.id.editTextWord);
        String word = editText.getText().toString();
        editText.setText("");

        Intent gameMultiIntent = new Intent(this, GameMultiActivity.class);
        gameMultiIntent.putExtra("Word Identifier", word);
        startActivity(gameMultiIntent);

    }


}
