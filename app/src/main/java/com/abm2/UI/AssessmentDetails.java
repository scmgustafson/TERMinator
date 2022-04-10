package com.abm2.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MenuItem;
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

public class AssessmentDetails extends AppCompatActivity {
    //Declare layout related fields
    //Variables for holding passed intent data
    private Assessment sentAssessment;
    private int sentId;
    private String sentTitle;
    private Long sentEndLong;
    private String sentType;
    private String sentCourseId;
    //Fields for targetting detail Alert UI objects
    private TextView textTitle;
    private TextView textEnd;
    private TextView textCourse;
    private RadioButton rbObjective;
    private RadioButton rbPerformance;
    //Fields for targeting Set Alert objects
    private EditText editAlertStartDate;
    private EditText editAlertEndDate;
    private RadioButton rbStart;
    private RadioButton rbEnd;
    private Date alertStartDate = new Date();
    private Date alertEndDate = new Date();
    //Set Date related information
    private Date endDate;
    final Calendar CAL = Calendar.getInstance();
    final Calendar ALERT_CAL = Calendar.getInstance();
    //DB related fields
    Repository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repo = new Repository(getApplication());
        //Setup action bar
        setContentView(R.layout.activity_assessment_details);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Set Assessment detailed information
        textTitle = findViewById(R.id.textAssessmentTitle);
        textEnd = findViewById(R.id.textAssessmentEnd);
        rbObjective = findViewById(R.id.rbDetailsObjective);
        rbPerformance = findViewById(R.id.rbDetailsPerformance);

        sentId = getIntent().getIntExtra("id", -1);
        sentTitle = getIntent().getStringExtra("title");
        sentEndLong = getIntent().getLongExtra("endDate", -1);
        endDate = DateConverter.toDate(sentEndLong);
        sentType = getIntent().getStringExtra("type");
        sentCourseId = getIntent().getStringExtra("course");
        CAL.setTime(endDate);
        ALERT_CAL.setTime(endDate);

        //Create Assessment object out of sent data
        sentAssessment = new Assessment(sentId, sentTitle, endDate, sentType, Integer.valueOf(sentCourseId));

        //Fields for targeting Set Alert UI objects
        alertStartDate = DateConverter.toDate(sentEndLong);
        alertEndDate = DateConverter.toDate(sentEndLong);
        editAlertStartDate = findViewById(R.id.editAssessmentAlertStart);
        editAlertEndDate = findViewById(R.id.editAssessmentAlertEndDate);
        rbStart = findViewById(R.id.rbStartAlert);
        rbEnd = findViewById(R.id.rbEndAlert);
        editAlertStartDate.setText((CAL.get(Calendar.MONTH)+1)+"-"+(CAL.get(Calendar.DATE))+"-"+(CAL.get(Calendar.YEAR)));
        editAlertEndDate.setText((CAL.get(Calendar.MONTH)+1)+"-"+(CAL.get(Calendar.DATE))+"-"+(CAL.get(Calendar.YEAR)));
        //Get course title based on course ID
        textTitle.setText(sentTitle);
        textEnd.setText((CAL.get(Calendar.MONTH)+1)+"-"+(CAL.get(Calendar.DATE))+"-"+(CAL.get(Calendar.YEAR)));
        if (sentType.toLowerCase().equals("objective")) {
            rbObjective.setChecked(true);
        }
        else {
            rbPerformance.setChecked(true);
        }
    }

    //Override standard Android back function to do a this.finish()
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    //Override standard actionbar back function to do a this.finish()
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
                Toast toast = Toast.makeText(getApplication(), "Assessment saved!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();

                //Move to previous screen
                this.finish();
            }
        }
    }

    public void onBtnDeleteAssessmentClick(View view) {
        repo = new Repository(getApplication());
        repo.delete(sentAssessment);

        //Display toast with confirmation message
        Toast toast = Toast.makeText(getApplication(), "Deleting assessment...", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

        //Move to previous screen
        this.finish();
    }

    public void onAlertStartDateClick(View view) {
        int date = ALERT_CAL.get(Calendar.DATE);
        int month = ALERT_CAL.get(Calendar.MONTH);
        int year = ALERT_CAL.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AssessmentDetails.this, android.R.style.Theme_Holo_Light_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        editAlertStartDate.setText((month+1)+"-"+date+"-"+year);
                        ALERT_CAL.set(year, month, date);
                        alertStartDate.setTime(ALERT_CAL.getTimeInMillis());
                    }
                }, year, month, date);
        datePickerDialog.show();
    }

    public void onAlertEndDateClick(View view) {
        int date = ALERT_CAL.get(Calendar.DATE);
        int month = ALERT_CAL.get(Calendar.MONTH);
        int year = ALERT_CAL.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AssessmentDetails.this, android.R.style.Theme_Holo_Light_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        editAlertEndDate.setText((month+1)+"-"+date+"-"+year);
                        ALERT_CAL.set(year, month, date);
                        alertEndDate.setTime(ALERT_CAL.getTimeInMillis());
                    }
                }, year, month, date);
        datePickerDialog.show();
    }

    public void onBtnSetAlertClick(View view) {
        //Check with date to use for the alert
        if (rbStart.isChecked()) {
            Intent intent = new Intent(AssessmentDetails.this, Receiver.class);
            intent.putExtra("msg", "Your assessment " + sentAssessment.getTitle() + "'s start date is today at " + String.valueOf(editAlertStartDate.getText()) + "!");
            PendingIntent sender = PendingIntent.getBroadcast(AssessmentDetails.this, MainActivity.numAlert++, intent, 0);
            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, DateConverter.toTimestamp(alertStartDate), sender);

            //Display toast with confirmation message
            Toast toast = Toast.makeText(getApplication(), "An alert for " + sentAssessment.getTitle() + " start date has been set!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
        else if (rbEnd.isChecked()) {
            Intent intent = new Intent(AssessmentDetails.this, Receiver.class);
            intent.putExtra("msg", "Your assessment " + sentAssessment.getTitle() + "'s end date is today at " + String.valueOf(editAlertEndDate.getText()) + "!");
            PendingIntent sender = PendingIntent.getBroadcast(AssessmentDetails.this, MainActivity.numAlert++, intent, 0);
            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, DateConverter.toTimestamp(alertEndDate), sender);

            //Display toast with confirmation message
            Toast toast = Toast.makeText(getApplication(), "An alert for " + sentAssessment.getTitle() + " end date has been set!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
        else {
            //Display toast with confirmation message
            Toast toast = Toast.makeText(getApplication(), "Start or end date must be checked", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }
}