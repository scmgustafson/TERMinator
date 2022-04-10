package com.abm2.UI;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.abm2.Database.Repository;
import com.abm2.Entity.Assessment;
import com.abm2.Entity.Note;
import com.abm2.R;

public class NoteDetails extends AppCompatActivity {
    //Declare layout items
    private EditText editNoteDetailsText;
    //Declare note related fields
    private Note sentNote;
    private int sentNoteId;
    private String sentText;
    private int sentCourseId;

    //Declare DB items
    Repository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        //Set layout fields
        editNoteDetailsText = findViewById(R.id.editNoteDetailsText);
        //Populate Note fields and create sentNote objects
        sentNoteId = getIntent().getIntExtra("noteId", -1);
        sentText = getIntent().getStringExtra("text");
        sentCourseId = getIntent().getIntExtra("courseId", -1);
        sentNote = new Note(sentNoteId, sentText, sentCourseId);

        editNoteDetailsText.setText(sentText);
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

    public void onBtnSaveNoteClick(View view) {
        repo = new Repository(getApplication());
        if (sentNote.getNoteId() > 0) {
            String newText = null;
            try {
                newText = String.valueOf(editNoteDetailsText.getText());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            //Check for blank fields
            if (newText.equals("") || newText.equals(null)) {
                //Set Toast error message then show to user
                Toast toast = Toast.makeText(getApplication(), "Note text field must not be blank", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
            else {

                Note newNote = new Note(sentNoteId, newText, sentCourseId);
                repo.update(newNote);

                //Set Toast error message then show to user
                Toast toast = Toast.makeText(getApplication(), "Note saved!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();

                //Move to previous screen
                this.finish();
            }
        }
    }

    public void onBtnDeleteNoteClick(View view) {
        repo = new Repository(getApplication());
        repo.delete(sentNote);

        //Display toast with confirmation message
        Toast toast = Toast.makeText(getApplication(), "Deleting note...", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

        //Move to previous screen
        this.finish();
    }

    public void onBtnShareNoteClick(View view) {

    }
}
