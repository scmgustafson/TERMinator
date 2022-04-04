package com.abm2.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.abm2.R;

import java.util.Calendar;

public class TermDetailsCourseList extends AppCompatActivity {

    private EditText startDateText;
    private EditText endDateText;
    private Spinner statusSpinner;

    //Initialize date fields
    final Calendar CAL = Calendar.getInstance();
    private int date, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details_course_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Enable top right menu

        //Set spinner information and populate
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this, R.array.status_array, android.R.layout.simple_spinner_item);
        statusSpinner = findViewById(R.id.statusSpinner);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        statusSpinner.setAdapter(statusAdapter);

        //Set other layout items
        startDateText = findViewById(R.id.startDateText);
        endDateText = findViewById(R.id.editTextEndDate);
    }

    public void onStartDateImgClick(View view) {
        date = CAL.get(Calendar.DATE);
        month = CAL.get(Calendar.MONTH);
        year = CAL.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(TermDetailsCourseList.this, android.R.style.Theme_Holo_Light_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        startDateText.setText((month+1)+"-"+date+"-"+year);
                        CAL.set(year, month, date);
                    }
                }, year, month, date);
        datePickerDialog.show();
    }

    public void onEndDateImgClick(View view) {
        date = CAL.get(Calendar.DATE);
        month = CAL.get(Calendar.MONTH);
        year = CAL.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(TermDetailsCourseList.this, android.R.style.Theme_Holo_Light_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        endDateText.setText((month+1)+"-"+date+"-"+year);
                        CAL.set(year, month, date);
                    }
                }, year, month, date);
        datePickerDialog.show();
    }

    public void onAddBtnClick(View view) {

    }

    public void onTextCourseTitleClick(View view) {
        Intent intent = new Intent(TermDetailsCourseList.this, CourseDetailsAssessmentList.class);
        startActivity(intent);
    }
}