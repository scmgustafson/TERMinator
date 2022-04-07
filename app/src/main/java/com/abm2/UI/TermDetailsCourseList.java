package com.abm2.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.abm2.Database.DateConverter;
import com.abm2.Database.Repository;
import com.abm2.Entity.Course;
import com.abm2.Entity.Term;
import com.abm2.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TermDetailsCourseList extends AppCompatActivity {
    //Declare layout related fields
    private Term sentTerm;
    private int sentId;
    private String sentTitle;
    private Date sentStartDate;
    private Date sentEndDate;

    private TextView termTitle;
    private TextView termStart;
    private TextView termEnd;

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
        termTitle = findViewById(R.id.textTermTitle);
        termStart = findViewById(R.id.textTermStart);
        termEnd = findViewById(R.id.textTermEnd);
        startDateText = findViewById(R.id.startDateText);
        endDateText = findViewById(R.id.editTextEndDate);

        //Populate current term detailed information using intent info from TermAdapter
        sentId = getIntent().getIntExtra("id", -1);
        sentTitle = getIntent().getStringExtra("title"); //Retrieved from the intent extra in previous activity
        sentStartDate = DateConverter.toDate(getIntent().getLongExtra("startDateLong", -1));
        sentEndDate = DateConverter.toDate(getIntent().getLongExtra("endDateLong", -1));

        sentTerm = new Term(sentId, sentTitle, sentStartDate, sentEndDate);

        termTitle.setText(sentTitle);
        termStart.setText(sentStartDate.toString());
        termEnd.setText(sentEndDate.toString());
        //Populate recycler view with Term items from DB
        RecyclerView recyclerView = findViewById(R.id.rvCourses);
        Repository repo = new Repository(getApplication());
        List<Course> allCourses = repo.selectCourseByTerm(sentId);
        final CourseAdapter adapter = new CourseAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setCourses(allCourses);
    }
//
//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        // Save the user's current game state
//        savedInstanceState.putInt();
//        savedInstanceState.putInt(STATE_LEVEL, currentLevel);
//
//        // Always call the superclass so it can save the view hierarchy state
//        super.onSaveInstanceState(savedInstanceState);
//    }

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