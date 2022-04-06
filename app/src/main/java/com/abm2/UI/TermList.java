package com.abm2.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.abm2.Database.Repository;
import com.abm2.Entity.Term;
import com.abm2.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TermList extends AppCompatActivity {

    private EditText startDateText;
    private EditText endDateText;

    //Prepare date fields
    final Calendar CAL = Calendar.getInstance();
    private int date, month, year;
    String dateFormat = "MM/dd/yy";
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
    Date startDate = new Date();
    Date endDate = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

        startDateText = findViewById(R.id.startDateText);
        endDateText = findViewById(R.id.editTextEndDate);

        //Populate recycler view with Term items from DB
        RecyclerView recyclerView = findViewById(R.id.rvTerms);
        Repository repo = new Repository(getApplication());
        List<Term> allTerms = repo.selectAllTerms();
        final TermAdapter adapter = new TermAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setTerms(allTerms);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        //Adds items to menu if present
        getMenuInflater().inflate(R.menu.menu_termlist, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onStartDateImgClick(View view) {
        date = CAL.get(Calendar.DATE);
        month = CAL.get(Calendar.MONTH);
        year = CAL.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(TermList.this, android.R.style.Theme_Holo_Light_Dialog,
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(TermList.this, android.R.style.Theme_Holo_Light_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        endDateText.setText((month+1)+"-"+date+"-"+year);
                        CAL.set(year, month, date);
                    }
                }, year, month, date);
        //TODO set minimum for end date to startdate
//        datePickerDialog.getDatePicker().setMinDate(whatever start date is);
        datePickerDialog.show();
    }

    public void onAddBtnClick(View view) {

    }

    public void onTermTitleTextClick(View view) {
        Intent intent = new Intent(TermList.this, TermDetailsCourseList.class);
        startActivity(intent);
    }
}