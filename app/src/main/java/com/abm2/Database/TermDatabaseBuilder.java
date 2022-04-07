package com.abm2.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.abm2.DAO.AssessmentDAO;
import com.abm2.DAO.CourseDAO;
import com.abm2.DAO.TermDAO;
import com.abm2.Entity.Assessment;
import com.abm2.Entity.Course;
import com.abm2.Entity.Term;

@Database(entities={Term.class, Course.class, Assessment.class}, version=5, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class TermDatabaseBuilder extends RoomDatabase {
    public abstract TermDAO termDAO();
    public abstract CourseDAO courseDAO();
    public abstract AssessmentDAO assessmentDAO();

    private static volatile TermDatabaseBuilder INSTANCE;

    static TermDatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TermDatabaseBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TermDatabaseBuilder.class, "myTermDatabase.db").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}