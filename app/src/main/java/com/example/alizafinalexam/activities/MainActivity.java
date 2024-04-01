package com.example.alizafinalexam.activities;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.alizafinalexam.R;
import com.example.alizafinalexam.lib.Utils;
import com.example.alizafinalexam.models.RandomNumber;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.WindowCompat;


import com.example.alizafinalexam.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;




public class MainActivity extends AppCompatActivity {
    private RandomNumber mRandomNumber;
    private ArrayList<Integer> mNumberHistory;
    private final String mKeyGame = "Game";
    private EditText from;
    private EditText to;
    private TextView resultsText;



    //when the app stops, either save the game if autoSave is on, otherwise delete the game
    @Override
    protected void onStop(){
        super.onStop();
        saveOrDeleteInSharedPrefs();
    }

    private void saveOrDeleteInSharedPrefs(){
        SharedPreferences defaultSharedPreferences = getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = defaultSharedPreferences.edit();
        editor.putInt(mKeyGame, mRandomNumber.getCurrentRandomNumber());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRandomNumber = new RandomNumber();
        initializeHistoryList(savedInstanceState, mKeyGame);

        resultsText = findViewById(R.id.resultsText);

        ExtendedFloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){

                from = findViewById(R.id.from);
                to = findViewById(R.id.to);
                if ( from.length() == 0 || to.length() == 0){
                    String error = getString(R.string.both_error);
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                }
                else {
                    int intFrom = Integer.parseInt(from.getText().toString());
                    int intTo = Integer.parseInt(to.getText().toString());
                    mRandomNumber.setFromTo(intFrom, intTo);
                    int result = mRandomNumber.getCurrentRandomNumber();
                    mNumberHistory.add(result);
                    String num = Integer.toString(result);
                    resultsText.setText(num);
                }


            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(mKeyGame, mNumberHistory);
    }


    private void initializeHistoryList(Bundle savedInstanceState , String key)
    {
        if(savedInstanceState != null){
            mNumberHistory = savedInstanceState.getIntegerArrayList(key);
        }
        else{
            String history = getDefaultSharedPreferences(this).getString(key, null);
            mNumberHistory = history == null ? new ArrayList<>(): Utils.getNumberListFromJSONString(history);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.showHistory) {
            Utils.showInfoDialog (MainActivity.this,
                    getString(R.string.history), mNumberHistory.toString());
        }

        if (id == R.id.clearHistory){
            mNumberHistory.clear();
        }

        //need to fix this 
        if (id == R.id.about){
            View view = findViewById(android.R.id.content);
            String greeting = getString(R.string.about_text);
            Snackbar.make(view, greeting, Snackbar.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

}