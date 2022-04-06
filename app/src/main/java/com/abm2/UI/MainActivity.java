package com.abm2.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.abm2.Database.Repository;
import com.abm2.Entity.Assessment;
import com.abm2.Entity.Course;
import com.abm2.Entity.Term;
import com.abm2.R;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onTermBtnClick(View view) {
        Intent intent = new Intent(MainActivity.this, TermList.class); //Create an intent to move from this activity to TermList activity
        startActivity(intent); //Pass intent into startActivity method

        //Setup DB with Sample Data
//        Repository repo = new Repository(getApplication());
//        Term term = new Term("Summer Term", new Date(), new Date());
//        Course course = new Course("Physics", new Date(), new Date(), "In Progress", "Teacher", "Email", "1112223333", "Notes", 1);
//        Assessment assessment = new Assessment("Physics Final", new Date(), "Objective", 1);
//        repo.insert(term);
//        repo.insert(course);
//        repo.insert(assessment);
    }
}