package com.abm2.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;

import com.abm2.Database.DateConverter;
import com.abm2.R;

import java.util.Calendar;
import java.util.Date;

public class AssessmentDetails extends AppCompatActivity {
    //Declare layout related fields
    //Variables for holding passed intent data
    private String sentTitle;
    private Long sentEndLong;
    private Date sentEndDate;
    private String sentType;
    private String sentCourse;
    //Fields for targetting TextView UI objects
    private TextView textTitle;
    private TextView textEnd;
    private TextView textCourse;
    private RadioButton rbObjective;
    private RadioButton rbPerformance;
    //Set Date related information
    final Calendar CAL = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_details);
        //Set Assessment detailed information
        textTitle = findViewById(R.id.textAssessmentTitle);
        textEnd = findViewById(R.id.textAssessmentEnd);
        textCourse = findViewById(R.id.textAssessmentCourse);
        rbObjective = findViewById(R.id.rbDetailsObjective);
        rbPerformance = findViewById(R.id.rbDetailsPerformance);

        sentTitle = getIntent().getStringExtra("title");
        sentEndLong = getIntent().getLongExtra("endDate", -1);
        sentEndDate = DateConverter.toDate(sentEndLong);
        sentType = getIntent().getStringExtra("type");
        sentCourse = getIntent().getStringExtra("course");

        CAL.setTime(sentEndDate);

        textTitle.setText(sentTitle);
        textEnd.setText((CAL.get(Calendar.MONTH)+1)+"-"+(CAL.get(Calendar.DATE))+"-"+(CAL.get(Calendar.YEAR)));
        textCourse.setText(sentCourse);
        if (sentType.equals("Objective")) {
            rbObjective.setChecked(true);
        }
        else {
            rbPerformance.setChecked(true);
        }
    }

    public void onTextAssessmentEndClick(View view) {
        int date = CAL.get(Calendar.DATE);
        int month = CAL.get(Calendar.MONTH);
        int year = CAL.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AssessmentDetails.this, android.R.style.Theme_Holo_Light_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        textEnd.setText((month+1)+"-"+date+"-"+year);
                        CAL.set(year, month, date);
                    }
                }, year, month, date);
        datePickerDialog.show();
    }
}