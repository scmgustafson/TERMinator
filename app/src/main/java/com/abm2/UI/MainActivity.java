package com.abm2.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.abm2.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onStartBtnClick(View view) {
        Intent intent = new Intent(MainActivity.this, TermList.class); //Create an intent to move from this activity to TermList activity
        startActivity(intent); //Pass intent into startActivity method
    }
}