package com.example.fatih.hangman;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class GameOverActitvity extends ActionBarActivity {

    public int mPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_actitvity);

        int points = getIntent().getIntExtra("Points Identifier", 0);
        TextView scoreTextView = (TextView) findViewById(R.id.textViewPoints);
        scoreTextView.setText(String.valueOf(points));
        mPoints = points;

        String word = getIntent().getStringExtra("Word Identifier");
        TextView wordTextView = (TextView) findViewById(R.id.word);
        wordTextView.setText(word);
    }

    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(GameOverActitvity.this).create();
        alertDialog.setTitle("Çıkış");
        alertDialog.setMessage("Oyundan çıkmak istiyor musunuz?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Evet",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        GameOverActitvity.this.finish();
                        System.exit(0);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Hayır",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
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
