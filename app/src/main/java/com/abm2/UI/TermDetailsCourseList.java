package com.abm2.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.abm2.Database.DateConverter;
import com.abm2.Database.Repository;
import com.abm2.Entity.Course;
import com.abm2.Entity.Term;
import com.abm2.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TermDetailsCourseList extends AppCompatActivity {
    //Declare fields for edit term details layout
    private String newTermTitle = null;
    private Date newTermStartDate = new Date();
    private Date newTermEndDate = new Date();

    private EditText editTermTitle;
    private EditText editTermStartDate;
    private EditText editTermEndDate;

    //Declare fields for term object creation
    private Term sentTerm;
    private int sentId;
    private String sentTitle;
    private Date sentStartDate;
    private Date sentEndDate;

    //Declare fields for new course layout\
    private EditText editNewCourseTitle;
    private EditText editNewCourseStartDate;
    private EditText editNewCourseEndDate;
    private Spinner statusSpinner;
    private EditText editNewCourseIName;
    private EditText editNewCourseIEmail;
    private EditText editNewCourseIPhone;

    //Declare fields for new course object creation
    private int newCourseId;
    private String newCourseTitle;
    private Date newCourseStartDate = new Date();
    private Date newCourseEndDate = new Date();
    private String newCourseStatus;
    private String newCourseIName;
    private String newCourseIPhone;
    private String newCourseIEmail;
    private int newCourseTermId;

    //Initialize date fields
    final Calendar SENT_INFO_CAL = Calendar.getInstance();
    final Calendar NEW_COURSE_CAL = Calendar.getInstance();
    final Calendar EDIT_TERM_CAL = Calendar.getInstance();
    private int date, month, year;

    //Set DB related fields
    Repository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details_course_list);

        //Set Term data layout items
        editTermTitle = findViewById(R.id.editTermTitle);
        editTermStartDate = findViewById(R.id.editTermStartDate);
        editTermEndDate = findViewById(R.id.editTermEndDate);

        //Populate current term detailed information using intent info from TermAdapter
        sentId = getIntent().getIntExtra("id", -1);
        sentTitle = getIntent().getStringExtra("title"); //Retrieved from the intent extra in previous activity
        sentStartDate = DateConverter.toDate(getIntent().getLongExtra("startDateLong", -1));
        sentEndDate = DateConverter.toDate(getIntent().getLongExtra("endDateLong", -1));

        sentTerm = new Term(sentId, sentTitle, sentStartDate, sentEndDate);

        editTermTitle.setText(sentTitle);
        //Current term date information and formatting
        SENT_INFO_CAL.setTime(sentStartDate);
        date = SENT_INFO_CAL.get(Calendar.DATE);
        month = SENT_INFO_CAL.get(Calendar.MONTH);
        year = SENT_INFO_CAL.get(Calendar.YEAR);
        editTermStartDate.setText((month+1)+"-"+date+"-"+year);
        SENT_INFO_CAL.setTime(sentEndDate);
        date = SENT_INFO_CAL.get(Calendar.DATE);
        month = SENT_INFO_CAL.get(Calendar.MONTH);
        year = SENT_INFO_CAL.get(Calendar.YEAR);
        editTermEndDate.setText((month+1)+"-"+date+"-"+year);
        newTermStartDate = sentStartDate;
        newTermEndDate = sentEndDate;

        //Set new Course layout items
        editNewCourseTitle = findViewById(R.id.editNewCourseTitle);
        editNewCourseStartDate = findViewById(R.id.editNewCourseStartDate);
        editNewCourseEndDate = findViewById(R.id.editNewCourseEndDate);
        editNewCourseIName = findViewById(R.id.editNewCourseIName);
        editNewCourseIPhone = findViewById(R.id.editNewCourseIPhone);
        editNewCourseIEmail = findViewById(R.id.editNewCourseIEmail);

        //Set spinner information and populate
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this, R.array.status_array, android.R.layout.simple_spinner_item);
        statusSpinner = findViewById(R.id.spinnerCourseStatus);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        statusSpinner.setAdapter(statusAdapter);

        //Refresh Course RV
        refreshCourseRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshCourseRecyclerView();
    }

    public void onTermStartDateClick(View view) {
        EDIT_TERM_CAL.setTime(sentStartDate);
        date = EDIT_TERM_CAL.get(Calendar.DATE);
        month = EDIT_TERM_CAL.get(Calendar.MONTH);
        year = EDIT_TERM_CAL.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(TermDetailsCourseList.this, android.R.style.Theme_Holo_Light_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        editTermStartDate.setText((month+1)+"-"+date+"-"+year);
                        EDIT_TERM_CAL.set(year, month, date);
                        newTermStartDate.setTime(EDIT_TERM_CAL.getTimeInMillis());
                    }
                }, year, month, date);
        datePickerDialog.show();
    }

    public void onTermEndDateClick(View view) {
        EDIT_TERM_CAL.setTime(sentEndDate);
        date = EDIT_TERM_CAL.get(Calendar.DATE);
        month = EDIT_TERM_CAL.get(Calendar.MONTH);
        year = EDIT_TERM_CAL.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(TermDetailsCourseList.this, android.R.style.Theme_Holo_Light_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        editTermEndDate.setText((month+1)+"-"+date+"-"+year);
                        EDIT_TERM_CAL.set(year, month, date);
                        newTermEndDate.setTime(EDIT_TERM_CAL.getTimeInMillis());
                    }
                }, year, month, date);
        datePickerDialog.show();
    }

    public void onBtnSaveTermClick(View view) {
        repo = new Repository(getApplication());
        if (sentTerm.getTermId() > 0) {
            try {
                newTermTitle = String.valueOf(editTermTitle.getText());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            //Check for blank fields
            if (newTermTitle.equals("") || newTermTitle.equals(null)) {
                //Set Toast error message then show to user
                Toast toast = Toast.makeText(getApplication(), "All fields must not be blank", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
            else {
                if (newTermEndDate.before(newTermStartDate) || newTermEndDate.equals(newTermStartDate)) {
                    //Set Toast error message then show to user
                    Toast toast = Toast.makeText(getApplication(), "Term end date must be after term start date", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
                else {
                    Term newTerm = new Term(sentTerm.getTermId(), newTermTitle, newTermStartDate, newTermEndDate);
                    repo.update(newTerm);

                    //Set Toast error message then show to user
                    Toast toast = Toast.makeText(getApplication(), "Term saved!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();

                    //Move to previous screen
                    this.finish();
                }
            }
        }
    }

    public void onBtnDeleteTermClick(View view) {
        repo = new Repository(getApplication());
        List<Course> currentTermCourses = repo.selectCourseByTerm(sentTerm.getTermId());
        //Check to see if a Term has existing courses, if so then display error message otherwise delete the term
        if (currentTermCourses.size() > 0) {
            Toast toast = Toast.makeText(getApplication(), "Term cannot be deleted because it has existing courses. Please delete all courses for this term first.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
        else if (currentTermCourses.size() == 0){
            repo.delete(sentTerm);

            //Set Toast error message then show to user
            Toast toast = Toast.makeText(getApplication(), "Deleting term...", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

            //Move to previous screen
            this.finish();
        }
    }

    public void onCourseStartDateClick(View view) {
        date = NEW_COURSE_CAL.get(Calendar.DATE);
        month = NEW_COURSE_CAL.get(Calendar.MONTH);
        year = NEW_COURSE_CAL.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(TermDetailsCourseList.this, android.R.style.Theme_Holo_Light_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        editNewCourseStartDate.setText((month+1)+"-"+date+"-"+year);
                        NEW_COURSE_CAL.set(year, month, date);
                        newCourseStartDate.setTime(NEW_COURSE_CAL.getTimeInMillis());
                    }
                }, year, month, date);
        datePickerDialog.show();
    }

    public void onCourseEndDateClick(View view) {
        date = NEW_COURSE_CAL.get(Calendar.DATE);
        month = NEW_COURSE_CAL.get(Calendar.MONTH);
        year = NEW_COURSE_CAL.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(TermDetailsCourseList.this, android.R.style.Theme_Holo_Light_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        editNewCourseEndDate.setText((month+1)+"-"+date+"-"+year);
                        NEW_COURSE_CAL.set(year, month, date);
                        newCourseEndDate.setTime(NEW_COURSE_CAL.getTimeInMillis());
                    }
                }, year, month, date);
        datePickerDialog.show();
    }

    public void onBtnAddNewCourseClick(View view) {
        Repository repo = new Repository(getApplication());
        //Get new ID for Course by getting last existing course and incrementing ID or starting at 1
        newCourseId = 1;
        List<Course> allCourses = repo.selectAllCourses();
        if (allCourses.size() > 0) {
            newCourseId = (allCourses.get(allCourses.size()-1).getTermId()) + 1;
        }
        newCourseTitle = null;
        newCourseIName = null;
        newCourseIPhone = null;
        newCourseIEmail = null;
        //Read in values of EditText fields for new object creation
        try {
            newCourseTitle = String.valueOf(editNewCourseTitle.getText());
            newCourseStatus = statusSpinner.getSelectedItem().toString();
            newCourseIName = String.valueOf(editNewCourseIName.getText());
            newCourseIPhone = String.valueOf(editNewCourseIPhone.getText());
            newCourseIEmail = String.valueOf(editNewCourseIEmail.getText());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //Check for blank fields
        if ((newCourseTitle.equals("") || newCourseTitle.equals(null)) || (newCourseIName.equals("") || newCourseIName.equals(null))
        || (newCourseIPhone.equals("") || newCourseIPhone.equals(null)) || (newCourseIEmail.equals("") || newCourseIEmail.equals(null))) {
            //Set Toast error message then show to user
            Toast toast = Toast.makeText(getApplication(), "Title field must not be blank", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
        else {
            //Check dates
            if (newCourseEndDate.before(newCourseStartDate) || newCourseEndDate.equals(newCourseStartDate) ||
                    ((sentTerm.getStartDate().after(newCourseStartDate)) || (sentTerm.getStartDate().after(newCourseEndDate))) ||
                    ((sentTerm.getEndDate().before(newCourseStartDate)) || (sentTerm.getEndDate().before(newCourseEndDate)))) {
                //Set Toast error message then show to user
                Toast toast = Toast.makeText(getApplication(), "Course end date must be after course start date and must be within term dates", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
            else {
                Course newCourse = new Course(newCourseId, newCourseTitle, newCourseStartDate, newCourseEndDate, newCourseStatus, newCourseIName, newCourseIPhone, newCourseIEmail, sentTerm.getTermId());
                repo.insert(newCourse);

                //Set Toast error message then show to user
                Toast toast = Toast.makeText(getApplication(), "Course saved!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        }
        refreshCourseRecyclerView();
    }

    public void onTextCourseTitleClick(View view) {
        Intent intent = new Intent(TermDetailsCourseList.this, CourseDetailsAssessmentList.class);
        startActivity(intent);
    }

    public void refreshCourseRecyclerView() {
        //Populate recycler view with Course items from DB
        RecyclerView recyclerView = findViewById(R.id.rvCourses);
        Repository repo = new Repository(getApplication());
        List<Course> allCourses = repo.selectCourseByTerm(sentId);
        final CourseAdapter adapter = new CourseAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setCourses(allCourses);
    }
}