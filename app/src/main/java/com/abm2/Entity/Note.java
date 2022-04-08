package com.abm2.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note {
    //Note primary key
    @PrimaryKey (autoGenerate = true)
    private int noteId;
    //Note other fields
    private String text;
    private int courseId;

    public Note(String text, int courseId) {
        this.text = text;
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return text;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
