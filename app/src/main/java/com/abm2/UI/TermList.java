package com.abm2.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.abm2.R;

import java.util.Calendar;

public class TermList extends AppCompatActivity {

    private EditText startDateText;
    private DatePickerDialog startDatePickerDialog;
    private ImageView startDateImg;

    private int date, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

        startDateText = findViewById(R.id.startDateText);
        startDateImg = findViewById(R.id.startDateImg);

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

    public void OnFloatingActionClick(View view) {

    }

    public void onStartDateImgClick(View view) {
        final Calendar cal = Calendar.getInstance();
        date = cal.get(Calendar.DATE);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(TermList.this, android.R.style.Theme_DeviceDefault_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        startDateText.setText(month+"-"+date+"-"+year);
                    }
                }, year, date, month);
        datePickerDialog.show();
    }
}