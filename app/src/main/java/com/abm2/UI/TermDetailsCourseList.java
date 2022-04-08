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
    //Declare fields for term data including layout
    private Term sentTerm;
    private int sentId;
    private String sentTitle;
    private Date sentStartDate;
    private Date sentEndDate;

    private String newTermTitle = null;
    private Date newTermStartDate = new Date();
    private Date newTermEndDate = new Date();

    private EditText editTermTitle;
    private EditText editTermStartDate;
    private EditText editTermEndDate;

    //Declare fields for new course including layout
    private EditText editNewCourseStartDate;
    private EditText editNewCourseEndDate;
    private Spinner statusSpinner;

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

        //Set new Course layout items
        editNewCourseStartDate = findViewById(R.id.editNewCourseStartDate);
        editNewCourseEndDate = findViewById(R.id.editNewCourseEndDate);
        newTermStartDate = sentStartDate;
        newTermEndDate = sentEndDate;

        //Set spinner information and populate
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this, R.array.status_array, android.R.layout.simple_spinner_item);
        statusSpinner = findViewById(R.id.statusSpinner);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        statusSpinner.setAdapter(statusAdapter);

        refreshRecycler();
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
                Toast toast = Toast.makeText(getApplication(), "Title field must not be blank", Toast.LENGTH_LONG);
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
                    }
                }, year, month, date);
        datePickerDialog.show();
    }

    public void onBtnAddNewCourseClick(View view) {

        refreshRecycler();
    }

    public void onTextCourseTitleClick(View view) {
        Intent intent = new Intent(TermDetailsCourseList.this, CourseDetailsAssessmentList.class);
        startActivity(intent);
    }

    public void refreshRecycler() {
        //Populate recycler view with Term items from DB
        RecyclerView recyclerView = findViewById(R.id.rvCourses);
        Repository repo = new Repository(getApplication());
        List<Course> allCourses = repo.selectCourseByTerm(sentId);
        final CourseAdapter adapter = new CourseAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setCourses(allCourses);
    }
}