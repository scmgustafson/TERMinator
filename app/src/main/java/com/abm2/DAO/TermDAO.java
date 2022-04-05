package com.abm2.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.abm2.Entity.Term;

import java.util.List;

@Dao
public interface TermDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Term term);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(Term term);

    @Delete
    void delete(Term term);

    @Query("SELECT * FROM terms")
    List<Term> selectAllTerms();
}
