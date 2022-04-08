package com.abm2.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.abm2.Database.Repository;
import com.abm2.Entity.Assessment;
import com.abm2.Entity.Term;
import com.abm2.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TermList extends AppCompatActivity {
    //Set fields for layout items
    private EditText editNewTermTitle;
    private EditText editNewTermStartDate;
    private EditText editNewTermEndDate;

    //Prepare date fields
    final Calendar CAL = Calendar.getInstance();
    private int date, month, year;

    //Prepare fields for new Term creation
    private String newTitle;
    private Date newStartDate = new Date();
    private Date newEndDate = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

        //Set layout items
        editNewTermTitle = findViewById(R.id.editNewTermTitle);
        editNewTermStartDate = findViewById(R.id.editNewTermStartDate);
        editNewTermEndDate = findViewById(R.id.editNewTermEndDate);

        //Populate recycler view with Term items from DB
        refreshRecycler();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshRecycler();
    }

    public void onStartDateClick(View view) {
        date = CAL.get(Calendar.DATE);
        month = CAL.get(Calendar.MONTH);
        year = CAL.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(TermList.this, android.R.style.Theme_Holo_Light_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        editNewTermStartDate.setText((month+1)+"-"+date+"-"+year);
                        CAL.set(year, month, date);
                        newStartDate.setTime(CAL.getTimeInMillis());
                    }
                }, year, month, date);
        datePickerDialog.show();
    }

    public void onEndDateClick(View view) {
        date = CAL.get(Calendar.DATE);
        month = CAL.get(Calendar.MONTH);
        year = CAL.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(TermList.this, android.R.style.Theme_Holo_Light_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        editNewTermEndDate.setText((month+1)+"-"+date+"-"+year);
                        CAL.set(year, month, date);
                        newEndDate.setTime(CAL.getTimeInMillis());
                    }
                }, year, month, date);
        datePickerDialog.show();
    }

    public void onBtnAddTermClick(View view) {
        Repository repo = new Repository(getApplication());
        //Get new ID for term by getting last term and incrementing ID or starting at 1
        int newId = 1;
        List<Term> allTerms = repo.selectAllTerms();
        if (allTerms.size() > 0) {
            newId = (allTerms.get(allTerms.size()-1).getTermId()) + 1;
        }
        newTitle = null;
        try {
            newTitle = String.valueOf(editNewTermTitle.getText());
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
            if (newEndDate.before(newStartDate) || newEndDate.equals(newStartDate)) {
                //Set Toast error message then show to user
                Toast toast = Toast.makeText(getApplication(), "Term end date must be after term start date", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
            else {
                Term newTerm = new Term(newId, newTitle, newStartDate, newEndDate);
                repo.insert(newTerm);

                //Set Toast error message then show to user
                Toast toast = Toast.makeText(getApplication(), "Term saved!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        }
        refreshRecycler();
    }

    public void onTermTitleTextClick(View view) {
        Intent intent = new Intent(TermList.this, TermDetailsCourseList.class);
        startActivity(intent);
    }

    public void refreshRecycler() {
        //Populate recycler view with Term items from DB
        RecyclerView recyclerView = findViewById(R.id.rvTerms);
        Repository repo = new Repository(getApplication());
        List<Term> allTerms = repo.selectAllTerms();
        final TermAdapter adapter = new TermAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setTerms(allTerms);
    }
}