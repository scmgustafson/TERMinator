package com.abm2.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.abm2.R;

import java.util.Calendar;

public class TermDetailsCourseList extends AppCompatActivity {

    private EditText startDateText;
    private EditText endDateText;

    private int date, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details_course_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Enable top right menu

        startDateText = findViewById(R.id.startDateText);
        endDateText = findViewById(R.id.editTextEndDate);
    }

    public void onStartDateImgClick(View view) {
        final Calendar cal = Calendar.getInstance();
        date = cal.get(Calendar.DATE);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(TermDetailsCourseList.this, android.R.style.Theme_Holo_Light_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        month += 1;
                        startDateText.setText(month+"-"+date+"-"+year);
                    }
                }, year, date, month);
        datePickerDialog.show();
    }

    public void onEndDateImgClick(View view) {
        final Calendar cal = Calendar.getInstance();
        date = cal.get(Calendar.DATE);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(TermDetailsCourseList.this, android.R.style.Theme_Holo_Light_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        month += 1;
                        endDateText.setText(month+"-"+date+"-"+year);
                    }
                }, year, date, month);
        datePickerDialog.show();
    }

    public void onAddBtnClick(View view) {

    }

    public void onTextCourseTitleClick(View view) {
        Intent intent = new Intent(TermDetailsCourseList.this, CourseDetailsAssessmentList.class);
        startActivity(intent);
    }
}