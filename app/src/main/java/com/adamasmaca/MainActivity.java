package com.adamasmaca;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = (Button) findViewById(R.id.button_singleplayer);
        Button btn2 = (Button) findViewById(R.id.button_score);

        btn1.setTransformationMethod(null);
        btn2.setTransformationMethod(null);
    }

    public void startSinglePlayerGame(View v) {
        Intent singlePlayerIntent = new Intent(this, GameActivity.class);
        singlePlayerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(singlePlayerIntent);
    }

    public void openScores(View v) {
        Intent scoresIntent = new Intent(this, ScoresActivity.class);
        scoresIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(scoresIntent);
    }

    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Çıkış");
        alertDialog.setMessage("Oyundan çıkmak istiyor musunuz?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Evet",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
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

}
