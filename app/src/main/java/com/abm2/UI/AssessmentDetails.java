package com.abm2.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.abm2.R;

public class AssessmentDetails extends AppCompatActivity {
    //Declare layout related fields
    //Variables for holding passed intent data
    private String sentTitle;
    private String sentEnd;
    private String sentType;
    private String sentCourse;
    //Fields for targetting TextView UI objects
    private TextView textTitle;
    private TextView textEnd;
    private TextView textType;
    private TextView textCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_details);

        //Set Assessment detailed information
        textTitle = findViewById(R.id.textAssessmentTitle);
        textEnd = findViewById(R.id.textAssessmentEnd);
        textType = findViewById(R.id.textAssessmentType);
        textCourse = findViewById(R.id.textAssessmentCourse);

        sentTitle = getIntent().getStringExtra("title");
        sentEnd = getIntent().getStringExtra("endDate");
        sentType = getIntent().getStringExtra("type");
        sentCourse = getIntent().getStringExtra("course");

        textTitle.setText(sentTitle);
        textEnd.setText(sentEnd);
        textType.setText(sentType);
        textCourse.setText(sentCourse);
    }
}