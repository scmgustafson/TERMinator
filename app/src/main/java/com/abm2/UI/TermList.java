package com.abm2.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.abm2.R;

public class TermList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        //Adds items to menu if present
        getMenuInflater().inflate(R.menu.menu_termlist, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void OnFloatingActionClick(View view) {
        Intent intent = new Intent(TermList.this, AddTerm.class); //Create an intent to move from this activity to TermList activity
        startActivity(intent); //Pass intent into startActivity method
    }
}