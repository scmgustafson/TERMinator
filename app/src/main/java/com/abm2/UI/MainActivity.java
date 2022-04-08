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

    public void onBtnStartClick(View view) {
        Intent intent = new Intent(MainActivity.this, TermList.class); //Create an intent to move from this activity to TermList activity
        startActivity(intent); //Pass intent into startActivity method

        //Setup DB with Sample Data
        Repository repo = new Repository(getApplication());
//        Term term = new Term(1, "Summer Term", new Date(), new Date());
//        Course course = new Course(1, "Physics", new Date(), new Date(), "In Progress", "Teacher", "Email", "1112223333", "Notes", 1);
        Assessment assessment = new Assessment(1, "Physics Final", new Date(), "Objective", 1);
//        repo.insert(term);
//        repo.insert(course);
        repo.insert(assessment);
//        Term term2 = new Term(2, "Fall Term", new Date(), new Date());
//        Course course2 = new Course(2, "Math", new Date(), new Date(), "In Progress", "Teacher", "Email", "1112223333", "Notes", 2);
//        Assessment assessment2 = new Assessment(2, "Math Midterm", new Date(), "Performance", 2);
//        repo.insert(term2);
//        repo.insert(course2);
//        repo.insert(assessment2);
    }
}