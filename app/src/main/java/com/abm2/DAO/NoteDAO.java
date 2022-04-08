package com.abm2.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.abm2.Entity.Course;
import com.abm2.Entity.Note;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface NoteDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Note note);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("SELECT * FROM notes")
    List<Note> selectAllNotes();

    @Query("SELECT * FROM notes WHERE courseId = :courseId")
    List<Note> selectNotesByCourseId(int courseId);
}
