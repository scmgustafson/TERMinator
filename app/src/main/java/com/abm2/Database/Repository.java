package com.abm2.Database;

import android.app.Application;

import com.abm2.DAO.AssessmentDAO;
import com.abm2.DAO.CourseDAO;
import com.abm2.DAO.TermDAO;
import com.abm2.Entity.Assessment;
import com.abm2.Entity.Course;
import com.abm2.Entity.Term;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    //Declare all entity related fields
    private TermDAO termDAO;
    private CourseDAO courseDAO;
    private AssessmentDAO assessmentDAO;

    private List<Term> allTerms;
    private List<Course> allCourses;
    private List<Assessment> allAssessments;

    //Make new thread
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor= Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        TermDatabaseBuilder db = TermDatabaseBuilder.getDatabase(application);
        termDAO = db.termDAO();
        courseDAO = db.courseDAO();
        assessmentDAO = db.assessmentDAO();
    }

    //Define CRUD methods for Term entity
    public void insert(Term term) {
        databaseExecutor.execute(()->{
            termDAO.insert(term);
        });
        try {
            Thread.sleep(1000);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Term term) {
        databaseExecutor.execute(()->{
            termDAO.update(term);
        });
        try {
            Thread.sleep(1000);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Term term) {
        databaseExecutor.execute(()->{
            termDAO.delete(term);
        });
        try {
            Thread.sleep(1000);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public List<Term> selectAllTerms() {
        databaseExecutor.execute(()->{
            allTerms = termDAO.selectAllTerms();
        });
        try {
            Thread.sleep(1000);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return allTerms;
    }

    //Define CRUD methods for Course entity
    public void insert(Course course) {
        databaseExecutor.execute(()->{
            courseDAO.insert(course);
        });
        try {
            Thread.sleep(1000);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Course course) {
        databaseExecutor.execute(()->{
            courseDAO.update(course);
        });
        try {
            Thread.sleep(1000);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Course course) {
        databaseExecutor.execute(()->{
            courseDAO.delete(course);
        });
        try {
            Thread.sleep(1000);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public List<Course> selectAllCourses() {
        databaseExecutor.execute(()->{
            allCourses = courseDAO.selectAllCourses();
        });
        try {
            Thread.sleep(1000);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return allCourses;
    }

    public List<Course> selectCourseById(int courseId) {
        databaseExecutor.execute(()->{
            allCourses = courseDAO.selectCourseById(courseId);
        });
        try {
            Thread.sleep(1000);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return allCourses;
    }

    //Define CRUD methods for Assessment entity
    public void insert(Assessment assessment) {
        databaseExecutor.execute(()->{
            assessmentDAO.insert(assessment);
        });
        try {
            Thread.sleep(1000);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Assessment assessment) {
        databaseExecutor.execute(()->{
            assessmentDAO.update(assessment);
        });
        try {
            Thread.sleep(1000);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Assessment assessment) {
        databaseExecutor.execute(()->{
            assessmentDAO.delete(assessment);
        });
        try {
            Thread.sleep(1000);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public List<Assessment> selectAllAssessments() {
        databaseExecutor.execute(()->{
            allAssessments = assessmentDAO.selectAllAssessments();
        });
        try {
            Thread.sleep(1000);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return allAssessments;
    }
}
