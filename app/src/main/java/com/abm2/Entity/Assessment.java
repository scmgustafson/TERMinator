package com.abm2.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "assessments")
public class Assessment {
    //Declare assessment primary key
    @PrimaryKey(autoGenerate = true)
    private int assessmentId;
    //Declare other assessment fields
    private String title;
    private Date endDate;
    private String type;
    private int courseId;

    public Assessment(String title, Date endDate, String type, int courseId) {
        this.title = title;
        this.endDate = endDate;
        this.type = type;
        this.courseId = courseId;
    }

    public int getAssessmentId() {
        return assessmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
