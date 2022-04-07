package com.abm2.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.abm2.Entity.Course;

import java.util.List;

@Dao
public interface CourseDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Course course);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(Course course);

    @Delete
    void delete(Course course);

    @Query("SELECT * FROM courses")
    List<Course> selectAllCourses();

    @Query("SELECT * FROM courses WHERE courseId = :courseId")
    List<Course> selectCourseById(int courseId);

    @Query("SELECT * FROM courses WHERE termId = :termId")
    List<Course> selectCoursesByTerm(int termId);
}
