package com.abm2.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.abm2.Database.Repository;
import com.abm2.Entity.Assessment;
import com.abm2.Entity.Course;
import com.abm2.R;

import java.util.Calendar;
import java.util.List;

public class CourseDetailsAssessmentList extends AppCompatActivity {

    private EditText startDateText;
    private EditText endDateText;

    private int date, month, year;

    final Calendar CAL = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details_assessment_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Enable top right menu

        startDateText = findViewById(R.id.startDateText);
        endDateText = findViewById(R.id.editTextEndDate);

        //Populate recycler view with Term items from DB
        RecyclerView recyclerView = findViewById(R.id.rvAssessments);
        Repository repo = new Repository(getApplication());
        List<Assessment> allAssessments = repo.selectAllAssessments();
        final AssessmentAdapter adapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setAssessments(allAssessments);
    }

    public void onEndDateImgClick(View view) {
        date = CAL.get(Calendar.DATE);
        month = CAL.get(Calendar.MONTH);
        year = CAL.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(CourseDetailsAssessmentList.this, android.R.style.Theme_Holo_Light_Dialog,
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

    public void onTextAssessmentTitleClick(View view) {
        Intent intent = new Intent(CourseDetailsAssessmentList.this, AssessmentDetails.class);
        startActivity(intent);
    }
}