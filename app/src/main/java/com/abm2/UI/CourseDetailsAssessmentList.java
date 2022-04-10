package com.abm2.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MenuItem;
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
import com.abm2.Entity.Note;
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
    private EditText courseTitle;
    private EditText courseStart;
    private EditText courseEnd;
    private Spinner courseStatus;
    private TextView courseName;
    private EditText courseEmail;
    private EditText coursePhone;
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
    //Set course alert fields
    private EditText editAlertStartDate;
    private EditText editAlertEndDate;
    private RadioButton rbCourseAlertStart;
    private RadioButton rbCourseAlertEnd;
    private Date alertCourseStartDate = new Date();
    private Date alertCourseEndDate = new Date();
    //Set fields for new Note
    private String newNoteText;
    private EditText editNewNote;
    //Set fields for new assessment
    private EditText editNewAssessmentTitle;
    private EditText editNewAssessmentEndDate;
    private RadioButton rbNewObjective;
    private RadioButton rbNewPerformance;
    private Date newAssessmentEndDate = new Date();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repo = new Repository(getApplication());
        //Setup action bar
        setContentView(R.layout.activity_course_details_assessment_list);

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

        //Set spinner information and populate
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this, R.array.status_array, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        courseStatus.setAdapter(statusAdapter);
        Resources res = getResources();

        //Compare sent status string to string array to get index position to set spinner
        ArrayList<String> statusStrings = new ArrayList<>();
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
        editCourseStartDate.setTime(DateConverter.toTimestamp(sentStart));
        editCourseEndDate.setTime((DateConverter.toTimestamp(sentEnd)));

        //Set Course alert layout items
        editAlertStartDate = findViewById(R.id.editCourseAlertStart);
        editAlertEndDate = findViewById(R.id.editCourseAlertEnd);
        editAlertStartDate.setText(String.valueOf(courseStart.getText()));
        editAlertEndDate.setText(String.valueOf(courseEnd.getText()));
        rbCourseAlertStart = findViewById(R.id.rbCourseStart);
        rbCourseAlertEnd = findViewById(R.id.rbCourseEnd);
        alertCourseStartDate.setTime(DateConverter.toTimestamp(sentStart));
        alertCourseEndDate.setTime(DateConverter.toTimestamp(sentEnd));

        //Set new note layout items
        editNewNote = findViewById(R.id.editNoteText);

        //Set new assessment layout items
        editNewAssessmentTitle = findViewById(R.id.editNewAssessmentTitle);
        editNewAssessmentEndDate = findViewById(R.id.editNewAssessmentEndDate);
        rbNewObjective = findViewById(R.id.rbNewObjective);
        rbNewPerformance = findViewById(R.id.rbNewPerformance);

        //Populate recycler view with Assessment items from DB
        refreshAssessmentRecyclerView();
        refreshNotesRecyclerView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshAssessmentRecyclerView();
        refreshNotesRecyclerView();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    public void onBtnEditCourseClick(View view) {
        Repository repo = new Repository(getApplication());
        //Get new ID for Course by getting last existing course and incrementing ID or starting at 1
        int courseId = sentCourse.getCourseId();
        int termId = sentCourse.getTermId();
        //Read in values of EditText fields for new object creation
        String newCourseTitle = "";
        String newCourseStatus = "";
        String newCourseIName = "";
        String newCourseIPhone = "";
        String newCourseIEmail = "";
        try {
            newCourseTitle = String.valueOf(courseTitle.getText());
            newCourseStatus = courseStatus.getSelectedItem().toString();
            newCourseIName = String.valueOf(courseName.getText());
            newCourseIPhone = String.valueOf(coursePhone.getText());
            newCourseIEmail = String.valueOf(courseEmail.getText());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //Check for blank fields
        if ((newCourseTitle.equals("") || newCourseTitle.equals(null)) || (newCourseIName.equals("") || newCourseIName.equals(null))
                || (newCourseIPhone.equals("") || newCourseIPhone.equals(null)) || (newCourseIEmail.equals("") || newCourseIEmail.equals(null))) {
            //Set Toast error message then show to user
            Toast toast = Toast.makeText(getApplication(), "All fields must not be blank", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
        else {
            //Check dates
            if (editCourseEndDate.before(editCourseStartDate) || editCourseEndDate.equals(editCourseStartDate)) {
                //Set Toast error message then show to user
                Toast toast = Toast.makeText(getApplication(), "Course end date must be after course start date", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
            else {
                Course newCourse = new Course(courseId, newCourseTitle, editCourseStartDate, editCourseEndDate, newCourseStatus, newCourseIName, newCourseIPhone, newCourseIEmail, termId);
                repo.update(newCourse);

                //Set Toast error message then show to user
                Toast toast = Toast.makeText(getApplication(), "Course saved!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();

                //Move to previous screen
                this.finish();
            }
        }
    }

    public void onBtnDeleteCourseClick(View view) {
        repo = new Repository(getApplication());
        repo.delete(sentCourse);

        //Set Toast error message then show to user
        Toast toast = Toast.makeText(getApplication(), "Deleting course...", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

        //Move to previous screen
        this.finish();
    }

    public void onCourseAlertStartDateClick(View view) {
        EDIT_COURSE_CAL.setTime(sentStart);
        date = EDIT_COURSE_CAL.get(Calendar.DATE);
        month = EDIT_COURSE_CAL.get(Calendar.MONTH);
        year = EDIT_COURSE_CAL.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(CourseDetailsAssessmentList.this, android.R.style.Theme_Holo_Light_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        editAlertStartDate.setText((month+1)+"-"+date+"-"+year);
                        EDIT_COURSE_CAL.set(year, month, date);
                        alertCourseStartDate.setTime(EDIT_COURSE_CAL.getTimeInMillis());
                    }
                }, year, month, date);
        datePickerDialog.show();
    }

    public void onCourseAlertEndDateClick(View view) {
        EDIT_COURSE_CAL.setTime(sentEnd);
        date = EDIT_COURSE_CAL.get(Calendar.DATE);
        month = EDIT_COURSE_CAL.get(Calendar.MONTH);
        year = EDIT_COURSE_CAL.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(CourseDetailsAssessmentList.this, android.R.style.Theme_Holo_Light_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        editAlertEndDate.setText((month+1)+"-"+date+"-"+year);
                        EDIT_COURSE_CAL.set(year, month, date);
                        alertCourseEndDate.setTime(EDIT_COURSE_CAL.getTimeInMillis());
                    }
                }, year, month, date);
        datePickerDialog.show();
    }

    public void onBtnSetCourseAlertClick(View view) {
        //Check with date to use for the alert
        if (rbCourseAlertStart.isChecked()) {
            Intent intent = new Intent(CourseDetailsAssessmentList.this, Receiver.class);
            intent.putExtra("msg", "Your course " + sentCourse.getTitle() + "'s start date is today at " + String.valueOf(editAlertStartDate.getText()) + "!");
            PendingIntent sender = PendingIntent.getBroadcast(CourseDetailsAssessmentList.this, MainActivity.numAlert++, intent, 0);
            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, DateConverter.toTimestamp(alertCourseStartDate), sender);

            //Display toast with confirmation message
            Toast toast = Toast.makeText(getApplication(), "An alert for " + sentCourse.getTitle() + " start date has been set!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
        else if (rbCourseAlertEnd.isChecked()) {
            Intent intent = new Intent(CourseDetailsAssessmentList.this, Receiver.class);
            intent.putExtra("msg", "Your course " + sentCourse.getTitle() + "'s end date is today at " + String.valueOf(editAlertEndDate.getText()) + "!");
            PendingIntent sender = PendingIntent.getBroadcast(CourseDetailsAssessmentList.this, MainActivity.numAlert++, intent, 0);
            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, DateConverter.toTimestamp(alertCourseEndDate), sender);

            //Display toast with confirmation message
            Toast toast = Toast.makeText(getApplication(), "An alert for " + sentCourse.getTitle() + " end date has been set!", Toast.LENGTH_LONG);
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

    public void onBtnAddNoteClick(View view) {
        repo = new Repository(getApplication());
        //Read in new note value
        newNoteText = null;
        int courseId = sentCourse.getCourseId();
        //Generate new note id
        List<Note> allNotes = repo.selectAllNotes();
        int newId = 1;
        if (allNotes.size() > 0) {
            newId = (allNotes.get(allNotes.size()-1).getNoteId()) + 1;
        }
        //Begin input validation
        try {
            newNoteText = String.valueOf(editNewNote.getText());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //Check for blank
        if (newNoteText.equals("") || newNoteText.equals(null)) {
            //Set Toast error message then show to user
            Toast toast = Toast.makeText(getApplication(), "Note text must not be blank", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
        else {
            //Create new note object and add to DB
            Note note = new Note(newId, newNoteText, courseId);
            Repository repo = new Repository(getApplication());
            repo.insert(note);

            //Set Toast error message then show to user
            Toast toast = Toast.makeText(getApplication(), "Note saved!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

            //Clear note field
            editNewNote.setText("");

            //Refresh note RV and focus on RV
            refreshNotesRecyclerView();
            RecyclerView recyclerView = findViewById(R.id.rvCourseNotes);
            recyclerView.requestFocus();
        }
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
        String newTitle = "";
        String newType = "";
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

        refreshAssessmentRecyclerView();
        RecyclerView recyclerView = findViewById(R.id.rvAssessments);
        recyclerView.requestFocus();
    }

    public void refreshNotesRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rvCourseNotes);
        Repository repo = new Repository(getApplication());
        List<Note> allNotes = repo.selectNotesByCourseId(sentCourse.getCourseId());
        final NoteAdapter adapter = new NoteAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setNotes(allNotes);
    }

    public void refreshAssessmentRecyclerView() {
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