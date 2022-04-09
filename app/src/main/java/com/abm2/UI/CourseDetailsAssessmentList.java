package com.abm2.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abm2.Database.DateConverter;
import com.abm2.Database.Repository;
import com.abm2.Entity.Assessment;
import com.abm2.Entity.Course;
import com.abm2.Entity.Term;
import com.abm2.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CourseDetailsAssessmentList extends AppCompatActivity {
    //Variables for holding passed intent data
    private Course sentCourse;
    private int sentId;
    private int sentTermId;
    private String sentTitle;
    private Date sentStart;
    private Date sentEnd;
    private String sentStatus;
    private String sentName;
    private String sentEmail;
    private String sentPhone;
    //Declare fields for Course details layout
    private TextView courseTerm;
    private TextView courseTitle;
    private TextView courseStart;
    private TextView courseEnd;
    private Spinner courseStatus;
    private TextView courseName;
    private TextView courseEmail;
    private TextView coursePhone;
    //Declare fields for editing Course object
    private Date editCourseStartDate = new Date();
    private Date editCourseEndDate = new Date();
    //Set date related fields
    private int date, month, year;
    final Calendar SENT_COURSE_CAL = Calendar.getInstance();
    final Calendar EDIT_COURSE_CAL = Calendar.getInstance();
    final Calendar NEW_ASS_CAL = Calendar.getInstance();
    //Set DB related fields
    Repository repo;
    //Set fields for new assessment
    private EditText editNewAssessmentTitle;
    private EditText editNewAssessmentEndDate;
    private RadioButton rbNewObjective;
    private RadioButton rbNewPerformance;
    private Date newAssessmentEndDate = new Date();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details_assessment_list);
        Repository repo = new Repository(getApplication());

        //Set Course detailed information
        courseTitle = findViewById(R.id.editCourseTitle);
        courseStart = findViewById(R.id.editCourseStartDate);
        courseEnd = findViewById(R.id.editCourseEndDate);
        courseStatus = findViewById(R.id.editCourseStatusSpinner);
        courseName = findViewById(R.id.editCourseInstructorName);
        courseEmail = findViewById(R.id.editCourseInstructorEmail);
        coursePhone = findViewById(R.id.editCourseInstructorPhone);

        sentId = getIntent().getIntExtra("id", -1);
        sentTermId = getIntent().getIntExtra("term", -1);
        sentTitle = getIntent().getStringExtra("title");
        sentStart = DateConverter.toDate(getIntent().getLongExtra("startDateLong", -1));
        sentEnd = DateConverter.toDate(getIntent().getLongExtra("endDateLong", -1));
        sentStatus = getIntent().getStringExtra("status");
        sentName = getIntent().getStringExtra("instructorName");
        sentPhone = getIntent().getStringExtra("instructorPhone");
        sentEmail = getIntent().getStringExtra("instructorEmail");

        sentCourse = new Course(sentId, sentTitle, sentStart, sentEnd, sentStatus, sentName, sentPhone, sentEmail, sentTermId);

        String sentTermTitle = null;
        List<Term> terms = repo.selectAllTerms();
        for (Term term : terms) {
            if (term.getTermId() == sentCourse.getTermId()) {
                sentTermTitle = term.getTitle();
            }
        }

        //Set spinner information and populate
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this, R.array.status_array, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        courseStatus.setAdapter(statusAdapter);
        Resources res = getResources();
        //Compare sent status string to string array to get index position to set spinner
        ArrayList<String> statusStrings = new ArrayList<String>();
        for (String string : res.getStringArray(R.array.status_array)) {
            statusStrings.add(string);
        }
        int pos = 0;
        for (String string : statusStrings) {
            if (sentStatus.equals(string)) {
                break;
            }
            else {
                pos += 1;
            }
        }
        courseStatus.setSelection(pos);

        //Set other detailed course information
        courseTitle.setText(sentTitle);
        courseName.setText(sentName);
        coursePhone.setText(sentPhone);
        courseEmail.setText(sentEmail);

        //Set course date information
        SENT_COURSE_CAL.setTime(sentStart);
        date = SENT_COURSE_CAL.get(Calendar.DATE);
        month = SENT_COURSE_CAL.get(Calendar.MONTH);
        year = SENT_COURSE_CAL.get(Calendar.YEAR);
        courseStart.setText((month+1)+"-"+date+"-"+year);
        SENT_COURSE_CAL.setTime(sentEnd);
        date = SENT_COURSE_CAL.get(Calendar.DATE);
        month = SENT_COURSE_CAL.get(Calendar.MONTH);
        year = SENT_COURSE_CAL.get(Calendar.YEAR);
        courseEnd.setText((month+1)+"-"+date+"-"+year);

        //Set new assessment layout items
        editNewAssessmentTitle = findViewById(R.id.editNewAssessmentTitle);
        editNewAssessmentEndDate = findViewById(R.id.editNewAssessmentEndDate);
        rbNewObjective = findViewById(R.id.rbNewObjective);
        rbNewPerformance = findViewById(R.id.rbNewPerformance);

        //Populate recycler view with Term items from DB
        refreshRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshRecyclerView();
    }

    public void onCourseStartDateClick(View view) {
        EDIT_COURSE_CAL.setTime(sentStart);
        date = EDIT_COURSE_CAL.get(Calendar.DATE);
        month = EDIT_COURSE_CAL.get(Calendar.MONTH);
        year = EDIT_COURSE_CAL.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(CourseDetailsAssessmentList.this, android.R.style.Theme_Holo_Light_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        courseStart.setText((month+1)+"-"+date+"-"+year);
                        EDIT_COURSE_CAL.set(year, month, date);
                        editCourseStartDate.setTime(EDIT_COURSE_CAL.getTimeInMillis());
                    }
                }, year, month, date);
        datePickerDialog.show();
    }

    public void onCourseEndDateClick(View view) {
        EDIT_COURSE_CAL.setTime(sentEnd);
        date = EDIT_COURSE_CAL.get(Calendar.DATE);
        month = EDIT_COURSE_CAL.get(Calendar.MONTH);
        year = EDIT_COURSE_CAL.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(CourseDetailsAssessmentList.this, android.R.style.Theme_Holo_Light_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        courseEnd.setText((month+1)+"-"+date+"-"+year);
                        EDIT_COURSE_CAL.set(year, month, date);
                        editCourseEndDate.setTime(EDIT_COURSE_CAL.getTimeInMillis());
                    }
                }, year, month, date);
        datePickerDialog.show();
    }

    public void onAssessmentEndDateClick(View view) {
        date = NEW_ASS_CAL.get(Calendar.DATE);
        month = NEW_ASS_CAL.get(Calendar.MONTH);
        year = NEW_ASS_CAL.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(CourseDetailsAssessmentList.this, android.R.style.Theme_Holo_Light_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        editNewAssessmentEndDate.setText((month+1)+"-"+date+"-"+year);
                        NEW_ASS_CAL.set(year, month, date);
                        newAssessmentEndDate.setTime(NEW_ASS_CAL.getTimeInMillis());
                    }
                }, year, month, date);
        datePickerDialog.show();
    }

    public void onBtnAddNewAssessmentClick(View view) {
        repo = new Repository(getApplication());
        String newTitle = null;
        String newType = null;
        int newCourseId = sentCourse.getCourseId();
        try {
            newTitle = String.valueOf(editNewAssessmentTitle.getText());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //Check for blank fields
        if (newTitle.equals("") || newTitle.equals(null)) {
            //Set Toast error message then show to user
            Toast toast = Toast.makeText(getApplication(), "New assessment title field must not be blank", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

        }
        else {
            if (rbNewObjective.isChecked()) {
                newType = "Objective";
            }
            else if (rbNewPerformance.isChecked()){
                newType = "Performance";
            }
            //Get new ID for assessment
            List<Assessment> allAssessments = repo.selectAllAssessments();
            int newId = 1;
            if (allAssessments.size() > 0) {
                newId = (allAssessments.get(allAssessments.size()-1).getAssessmentId()) + 1;
            }
            Assessment newAssessment = new Assessment(newId, newTitle, newAssessmentEndDate, newType, sentCourse.getCourseId());
            repo.insert(newAssessment);

            //Set Toast error message then show to user
            Toast toast = Toast.makeText(getApplication(), "Assessment saved!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }

        refreshRecyclerView();
    }

    public void onTextAssessmentTitleClick(View view) {
        Intent intent = new Intent(CourseDetailsAssessmentList.this, AssessmentDetails.class);
        startActivity(intent);
    }

    public void refreshRecyclerView() {
        //Populate recycler view with Term items from DB
        RecyclerView recyclerView = findViewById(R.id.rvAssessments);
        Repository repo = new Repository(getApplication());
        List<Assessment> allAssessments = repo.selectAssessmentsByCourse(sentId);
        final AssessmentAdapter adapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setAssessments(allAssessments);
    }
}