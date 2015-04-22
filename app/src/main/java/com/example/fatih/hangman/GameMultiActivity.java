package com.example.fatih.hangman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

//TODO: Kelimenin anlamı da yazılacak.
//TODO: Direk tahmin hakkı verilecek.
//TODO: ekran çevirince patlıyor
//TODO: geri tuşuna basınca patlıyor

public class GameMultiActivity extends ActionBarActivity {

    private String mWord;           //Tahmin edilecek kelime
    private char mLetter;           //Kelimeyi tutacak
    private int mGuessedLetters = 0;//Doğru tahmin edilmiş harf sayısı
    private int mFailedLetters = 0; //Yanlış tahmin edlimiş harf sayısı
    private int mPoints = 0;        //Doğru tahmin edilen kellime sayısını tutar
    private final Locale TURKEY = new Locale("tr", "TR");
    private char mPreviousLetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_game);
        clearScreen();
        String temp = getIntent().getStringExtra("Word Identifier");
        mWord = temp.toUpperCase(TURKEY);
        createTextView(mWord);
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

            if(wordLetter == mLetter) {
                isThere = true;
                showLettersAtTextView(i, mLetter);


                mGuessedLetters++;
                mPoints = mPoints + 2;
            }
        }

        if (!isThere) {
            letterFailed();
        }

        // Kelime başarıyla tahmin edildi
        if(mGuessedLetters == mWord.length()) {
            mPoints = mPoints + 5;
            Intent multiplayerActivity = new Intent(this, MultiplayerActivity.class);
            multiplayerActivity.putExtra("Points Identifier", mPoints);
            multiplayerActivity.putExtra("Word Identifier", mWord);
            startActivity(multiplayerActivity);
        }
    }

    public void clearScreen() {
            // Doğru, yanlış tahmin edilen harflerin sayısını sıfırla
            mFailedLetters = mGuessedLetters = 0;

            // layoutlettersın çocuk TextViewlarını temizle
            LinearLayout linearLayoutWord = (LinearLayout) findViewById(R.id.linearLayoutWord);
            for (int i = 0; i < linearLayoutWord.getChildCount(); i++) {
                TextView currentTextView = (TextView) linearLayoutWord.getChildAt(i);
                currentTextView.setText("_");
            }

            LinearLayout layouts = (LinearLayout) findViewById(R.id.linearLayoutLetters);
            for(int i = 0; i < layouts.getChildCount(); i++) {
                LinearLayout linearLayout = (LinearLayout) layouts.getChildAt(i);

                for(int j = 0; j < linearLayout.getChildCount(); j++) {
                    TextView textView = (TextView) linearLayout.getChildAt(j);
                    textView.setVisibility(TextView.VISIBLE);
                }
            }


            //Resmi hangman_0 olarak değiştir.
            ImageView imageView = (ImageView) findViewById(R.id.hangmanImage);
            imageView.setImageResource(R.drawable.hangman_0);

    }

    public void letterFailed() {

        mFailedLetters++;

        ImageView image = (ImageView) findViewById(R.id.hangmanImage);

        switch (mFailedLetters)
        {
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
                Intent multiplayerActivity = new Intent(this, MultiplayerActivity.class);
                multiplayerActivity.putExtra("Points Identifier", mPoints);
                multiplayerActivity.putExtra("Word Identifier", mWord);
                startActivity(multiplayerActivity);
                break;
        }


    }


    /**
     * Kullanıcı tarafından girilen harfi verilen pozisyona
     * yerleştir.
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
}


