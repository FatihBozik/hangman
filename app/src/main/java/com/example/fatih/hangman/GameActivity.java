package com.example.fatih.hangman;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

//TODO: ekran çevirince patlıyor

public class GameActivity extends ActionBarActivity {
    public  GameActivity()
    {
        this.words = new ArrayList<>();
        indicesOfBlankLetter = new ArrayList<>();
    }

    private String mWord;           //Tahmin edilecek kelime
    private char mLetter;           //Kelimeyi tutacak
    private int mGuessedLetters = 0;//Doğru tahmin edilmiş harf sayısı
    private int mFailedLetters = 0; //Yanlış tahmin edlimiş harf sayısı
    private int mPoints = 0;        //Doğru tahmin edilen kellime sayısını tutar
    private final Locale TURKEY = new Locale("tr", "TR");
    private char mPreviousLetter;
    public ArrayList<String> words;
    public ArrayList<Integer> indicesOfBlankLetter;
    private int hintCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        clearScreen();
        setRandomWord();

        printButtonHintCount(hintCount);
    }

    public void createDialog(int layoutId) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(false);
        View v = getLayoutInflater().inflate(layoutId, null);
        alertDialog.setView(v);
        alertDialog.show();

        TextView scoreTextView = (TextView) v.findViewById(R.id.textViewPoints);
        scoreTextView.setText(String.valueOf(mPoints));

        TextView wordTextView = (TextView) v.findViewById(R.id.word);
        wordTextView.setText(mWord);
    }


    public void printButtonHintCount(int count) {
        Button hintButton = (Button) findViewById(R.id.buttonHint);
        hintButton.setText("İp ucu: " + count);
    }


    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this).create();
        alertDialog.setTitle("Çıkış");
        alertDialog.setMessage("Oyundan çıkmak istiyor musunuz?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Evet",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        GameActivity.this.finish();
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

    public void removeTextView() {
        LinearLayout linearLayoutWord = (LinearLayout) findViewById(R.id.linearLayoutWord);
        linearLayoutWord.removeAllViewsInLayout();
    }

    private void createTextView(String word) {
        LinearLayout linearLayoutWord = (LinearLayout) findViewById(R.id.linearLayoutWord);

        for (int i = 0; i < word.length(); i++) {
            TextView newTextView = (TextView) getLayoutInflater().inflate(R.layout.textview, null);
            linearLayoutWord.addView(newTextView);
        }
    }

    public void selectLetter(View v) {
        int id = v.getId();
        TextView textView = (TextView) findViewById(id);
        textView.setVisibility(TextView.INVISIBLE);
        mLetter = textView.getText().charAt(0);


        boolean isThere = false;

        for (int i = 0; i < mWord.length(); i++) {
            char wordLetter = mWord.charAt(i);

            if (wordLetter == mLetter) {
                isThere = true;
                showLettersAtTextView(i, mLetter);

                mGuessedLetters++;
                mPoints += mLetter;
            }

        }

        if (!isThere) {
            letterFailed();
        }

        // Kelime başarıyla tahmin edildi
        if (mGuessedLetters == mWord.length()) {
            mPoints += (mLetter * 2);

            createDialog(R.layout.activity_save_over);
        }
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

        LinearLayout linear = (LinearLayout) v.getParent();
        EditText editTextUserName = (EditText) linear.getChildAt(1);

        String name = editTextUserName.getText().toString();

        if(!name.equals(""))
        {
            SharedPreferences.Editor editor = preferences.edit();
            String previousScore = preferences.getString("SCORES", "");
            editor.putString("SCORES", previousScore + "\n" + name + "\t " + mPoints + " Puan \n");
            editor.commit();
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
        }
        else
        {
            Toast.makeText(this, "Lütfen adınızı girin", Toast.LENGTH_SHORT).show();
        }
    }


    public void clearScreen() {
        // Doğru, yanlış tahmin edilen harflerin sayısını sıfırla
        mFailedLetters = mGuessedLetters = 0;
        mPreviousLetter = 0;
        hintCount = 2;
        printButtonHintCount(hintCount);

        // layoutlettersın çocuk TextViewlarını temizle
        LinearLayout linearLayoutWord = (LinearLayout) findViewById(R.id.linearLayoutWord);
        for (int i = 0; i < linearLayoutWord.getChildCount(); i++) {
            TextView currentTextView = (TextView) linearLayoutWord.getChildAt(i);
            currentTextView.setText("_");
        }

        LinearLayout layouts = (LinearLayout) findViewById(R.id.linearLayoutLetters);
        for (int i = 0; i < layouts.getChildCount(); i++) {
            LinearLayout linearLayout = (LinearLayout) layouts.getChildAt(i);

            for (int j = 0; j < linearLayout.getChildCount(); j++) {
                TextView textView = (TextView) linearLayout.getChildAt(j);
                textView.setVisibility(TextView.VISIBLE);
            }
        }

        //Resmi hangman_0 olarak değiştir.
        ImageView imageView = (ImageView) findViewById(R.id.hangmanImage);
        imageView.setImageResource(R.drawable.hangman_0);

    }

    public void pass(View v) {
        removeTextView();
        clearScreen();
        setRandomWord();
    }

    public void letterFailed() {
        mFailedLetters++;

        ImageView image = (ImageView) findViewById(R.id.hangmanImage);


        switch (mFailedLetters) {
            case 1:
                image.setImageResource(R.drawable.hangman_1);
                break;
            case 2:
                image.setImageResource(R.drawable.hangman_2);
                break;
            case 3:
                image.setImageResource(R.drawable.hangman_3);
                break;
            case 4:
                image.setImageResource(R.drawable.hangman_4);
                break;
            case 5:
                image.setImageResource(R.drawable.hangman_5);
                break;
            case 6:
                image.setImageResource(R.drawable.hangman_6);
                break;
            case 7:
                image.setImageResource(R.drawable.hangman_7);
                break;
            case 8:
                createDialog(R.layout.activity_game_over);
                break;
        }
    }

    public void loadWords() {

        try {
            final InputStream open = getAssets().open("WORDS.txt");
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(open, "ISO-8859-9"));
            this.words.clear();
            for (String s = bufferedReader.readLine(); s != null; s = bufferedReader.readLine()) {
                if (s.length() >= 3 && s.length() <= 10) {
                    this.words.add(s.toUpperCase(TURKEY));
                }
            }
            open.close();
        } catch (OutOfMemoryError outOfMemoryError) {
            Toast.makeText(this, "Cihazınızın belleği dolu. Lütfen diğer uygulamaları kapatıp oyunu tekrar başlatın", Toast.LENGTH_SHORT).show();
        } catch (IOException ex) {
            Toast.makeText(this, "Sözlük yüklenemedi. Lütfen oyunu kapatıp tekrar deneyin", Toast.LENGTH_SHORT).show();
        }
    }

    public void setRandomWord() {

        loadWords();

        int randomNumber = (int) (Math.random() * words.size());
        mWord = words.get(randomNumber);

        createTextView(mWord);
    }

    /**
     * Kullanıcı tarafından girilen harfi verilen pozisyona
     * yerleştir.
     *
     * @param position
     * @param guessedLetter
     */
    public void showLettersAtTextView(int position, char guessedLetter) {

        if (mPreviousLetter != 0 && mPreviousLetter != guessedLetter) {
            for (int i = 0; i < mWord.length(); i++) {
                if(mPreviousLetter == mWord.charAt(i))
                {
                    setBlack(i);
                }
            }
        }
        LinearLayout layoutLetter = (LinearLayout) findViewById(R.id.linearLayoutWord);

        TextView textView = (TextView) layoutLetter.getChildAt(position);
        textView.setText(Character.toString(guessedLetter));
        textView.setTextColor(getResources().getColor(R.color.blue));

        mPreviousLetter = guessedLetter;

    }

    public void setBlack(int a)
    {
        LinearLayout layoutLetter = (LinearLayout) findViewById(R.id.linearLayoutWord);
        TextView textView = (TextView) layoutLetter.getChildAt(a);
        textView.setTextColor(getResources().getColor(R.color.black));
    }


    public void traverseAndInvisibleLetter(char letter)
    {
        LinearLayout linearLayoutLetters = (LinearLayout) findViewById(R.id.linearLayoutLetters);

        for(int i = 0; i < linearLayoutLetters.getChildCount(); i++) {

            LinearLayout ly = (LinearLayout) linearLayoutLetters.getChildAt(i);

            for(int j = 0; j < ly.getChildCount(); j++) {
                TextView tx = (TextView) ly.getChildAt(j);

                if(tx.getText().toString().charAt(0) == letter)
                {
                    tx.setVisibility(View.INVISIBLE);
                    return;
                }
            }
        }
    }

    public void fillRandomly(String word) {
        int randomNumber = (int) (Math.random() * indicesOfBlankLetter.size());
        char letter = word.charAt(indicesOfBlankLetter.get(randomNumber));
        traverseAndInvisibleLetter(letter);

        for(int i = 0; i < mWord.length(); i++) {
            if(mWord.charAt(i) == letter) {
                showLettersAtTextView(i, letter);
                mGuessedLetters++;
            }
        }
        indicesOfBlankLetter.remove(randomNumber);
        printButtonHintCount(--hintCount);

        if(indicesOfBlankLetter.size() == 0) {
            createDialog(R.layout.activity_save_over);
        }
    }

    public boolean isThere(int indice) {
       boolean control = false;
       for(int i : indicesOfBlankLetter) {
            if(mWord.charAt(i) == mWord.charAt(indice)) {
                control = true;
                break;
            }
       }
       return control;
    }

    public void hint(View v) {
        LinearLayout word = (LinearLayout) findViewById(R.id.linearLayoutWord);

        if(hintCount == 2) {
            for(int i = 0; i < mWord.length(); i++) {
                TextView txt = (TextView) word.getChildAt(i);
                if(txt.getText().toString().charAt(0) == '_') {
                    if(!isThere(i)) {
                        indicesOfBlankLetter.add(new Integer(i));
                    }
                }
            }
        }

        if(hintCount <= 0) {
            return;
        }
        else {
            fillRandomly(mWord);
        }
    }
}


