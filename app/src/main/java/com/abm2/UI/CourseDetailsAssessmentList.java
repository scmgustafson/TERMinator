package com.abm2.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.abm2.Database.DateConverter;
import com.abm2.Database.Repository;
import com.abm2.Entity.Assessment;
import com.abm2.Entity.Course;
import com.abm2.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CourseDetailsAssessmentList extends AppCompatActivity {
    //Declare layout related fields
    //Variables for holding passed intent data
    private Course sentCourse;
    private int sentId;
    private int sentTerm;
    private String sentTitle;
    private Date sentStart;
    private Date sentEnd;
    private String sentStatus;
    private String sentName;
    private String sentEmail;
    private String sentPhone;
    private String sentNotes; //TODO IMPLEMENT LIST OF NOTES
    //Fields targeting TextView layout objects
    private TextView courseTerm;
    private TextView courseTitle;
    private TextView courseStart;
    private TextView courseEnd;
    private TextView courseStatus;
    private TextView courseName;
    private TextView courseEmail;
    private TextView coursePhone;
    private TextView courseNotes; //TODO IMPLEMENT LIST OF NOTES
    //Set date related fields
    private int date, month, year;
    final Calendar CAL = Calendar.getInstance();
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Enable top right menu

        //Set Course detailed information
        courseTerm = findViewById(R.id.textCourseTerm);
        courseTitle = findViewById(R.id.textCourseTitle);
        courseStart = findViewById(R.id.textCourseStart);
        courseEnd = findViewById(R.id.textCourseEnd);
        courseStatus = findViewById(R.id.textCourseStatus);
        courseName = findViewById(R.id.textCourseInstructorName);
        courseEmail = findViewById(R.id.textCourseInstructorEmail);
        coursePhone = findViewById(R.id.textCourseInstructorPhone);
        courseNotes = findViewById(R.id.textNote); //TODO IMPLEMENT RECYCLER VIEW FOR NOTES?

        sentId = getIntent().getIntExtra("id", -1);
        sentTerm = getIntent().getIntExtra("term", -1);
        sentTitle = getIntent().getStringExtra("title");
        sentStart = DateConverter.toDate(getIntent().getLongExtra("startDateLong", -1));
        sentEnd = DateConverter.toDate(getIntent().getLongExtra("endDateLong", -1));
        sentStatus = getIntent().getStringExtra("status");
        sentName = getIntent().getStringExtra("instructorName");
        sentPhone = getIntent().getStringExtra("instructorPhone");
        sentEmail = getIntent().getStringExtra("instructorEmail");
        sentNotes = getIntent().getStringExtra("notes"); //TODO IMPLEMENT RECYCLER VIEW FOR NOTES?

        sentCourse = new Course(sentId, sentTitle, sentStart, sentEnd, sentStatus, sentName, sentPhone, sentEmail, sentNotes, sentTerm);

        courseTerm.setText(String.valueOf(sentTerm));
        courseTitle.setText(sentTitle);
        courseStart.setText(sentStart.toString());
        courseEnd.setText(sentEnd.toString());
        courseStatus.setText(sentStatus);
        courseName.setText(sentName);
        coursePhone.setText(sentPhone);
        courseEmail.setText(sentEmail);
        courseNotes.setText(sentNotes);

        //Set new assessment layout items
        editNewAssessmentTitle = findViewById(R.id.editNewAssessmentTitle);
        editNewAssessmentEndDate = findViewById(R.id.editNewAssessmentEndDate);
        rbNewObjective = findViewById(R.id.rbNewObjective);
        rbNewPerformance = findViewById(R.id.rbNewPerformance);

        //Populate recycler view with Term items from DB
        RecyclerView recyclerView = findViewById(R.id.rvAssessments);
        Repository repo = new Repository(getApplication());
        List<Assessment> allAssessments = repo.selectAssessmentsByCourse(sentId);
        final AssessmentAdapter adapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setAssessments(allAssessments);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshRecyclerView();
    }

    public void onEditEndDateClick(View view) {
        date = CAL.get(Calendar.DATE);
        month = CAL.get(Calendar.MONTH);
        year = CAL.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(CourseDetailsAssessmentList.this, android.R.style.Theme_Holo_Light_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        editNewAssessmentEndDate.setText((month+1)+"-"+date+"-"+year);
                        CAL.set(year, month, date);
                        newAssessmentEndDate.setTime(CAL.getTimeInMillis());
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
            int newId = (allAssessments.get(allAssessments.size()-1).getAssessmentId()) + 1;
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

    public void onImgAssessmentRefreshClick(View view) {
        refreshRecyclerView();
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