package com.adamasmaca;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;


public class ScoresActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        SharedPreferences preferences = getSharedPreferences("MYPREFERENCE", MODE_PRIVATE);
        String score = preferences.getString("SCORES", "Puan kaydedilmedi");

        TextView textViewScores = (TextView) findViewById(R.id.textViewScores);
        textViewScores.setText(score);
    }



}
