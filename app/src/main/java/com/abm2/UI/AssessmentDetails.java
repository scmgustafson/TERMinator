package com.abm2.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
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

public class AssessmentDetails extends AppCompatActivity {
    //Declare layout related fields
    //Variables for holding passed intent data
    private Assessment sentAssessment;
    private int sentId;
    private String sentTitle;
    private Long sentEndLong;
    private String sentType;
    private String sentCourse;
    //Fields for targetting TextView UI objects
    private TextView textTitle;
    private TextView textEnd;
    private TextView textCourse;
    private RadioButton rbObjective;
    private RadioButton rbPerformance;
    //Set Date related information
    private Date endDate;
    final Calendar CAL = Calendar.getInstance();
    //DB related fields
    Repository repo;

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

        sentId = getIntent().getIntExtra("id", -1);
        sentTitle = getIntent().getStringExtra("title");
        sentEndLong = getIntent().getLongExtra("endDate", -1);
        endDate = DateConverter.toDate(sentEndLong);
        sentType = getIntent().getStringExtra("type");
        sentCourse = getIntent().getStringExtra("course");

        //Create Assessment object out of sent data
        sentAssessment = new Assessment(sentId, sentTitle, endDate, sentType, Integer.valueOf(sentCourse));

        CAL.setTime(endDate);

        textTitle.setText(sentTitle);
        textEnd.setText((CAL.get(Calendar.MONTH)+1)+"-"+(CAL.get(Calendar.DATE))+"-"+(CAL.get(Calendar.YEAR)));
        textCourse.setText(sentCourse);
        if (sentType.toLowerCase().equals("objective")) {
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
                        endDate.setTime(CAL.getTimeInMillis());
                    }
                }, year, month, date);
        datePickerDialog.show();
    }

    public void onBtnSaveAssessmentDetailsClick(View view) {
        repo = new Repository(getApplication());
        if (sentAssessment.getAssessmentId() > 0) {
            String newTitle = null;
            try {
                newTitle = String.valueOf(textTitle.getText());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            //Check for blank fields
            if (newTitle.equals("") || newTitle.equals(null)) {
                //Set Toast error message then show to user
                Toast toast = Toast.makeText(getApplication(), "Title field must not be blank", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();

            }
            else {
                String newType = null;
                if (rbObjective.isChecked()) {
                    newType = "Objective";
                }
                else if (rbPerformance.isChecked()){
                    newType = "Performance";
                }

                Assessment newAssessment = new Assessment(sentAssessment.getAssessmentId(), newTitle, endDate, newType, sentAssessment.getCourseId());
                repo.update(newAssessment);

                //Set Toast error message then show to user
                Toast toast = Toast.makeText(getApplication(), "Assessment saved", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        }
    }

    public void onBtnDeleteAssessmentClick(View view) {
        repo = new Repository(getApplication());
        repo.delete(sentAssessment);

        Toast toast = Toast.makeText(getApplication(), "Deleting assessment...", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

        Intent intent = new Intent(AssessmentDetails.this, CourseDetailsAssessmentList.class);
        Course currentCourse = repo.selectCourseById(sentAssessment.getCourseId()).get(0);
        intent.putExtra("term", String.valueOf(currentCourse.getTermId())); //TODO IMPLEMENT RETURN OF COURSE NAME FROM ID VIA QUERY?
        intent.putExtra("title", currentCourse.getTitle());
        intent.putExtra("startDate", currentCourse.getStartDate().toString());
        intent.putExtra("endDate", currentCourse.getEndDate().toString());
        intent.putExtra("status", currentCourse.getStatus());
        intent.putExtra("instructorName", currentCourse.getInstructorName());
        intent.putExtra("instructorEmail", currentCourse.getInstructorEmail());
        intent.putExtra("instructorPhone", currentCourse.getInstructorPhone());
        intent.putExtra("notes", currentCourse.getNotes()); //TODO IMPLEMENT LIST OF NOTES
        startActivity(intent);
    }
}