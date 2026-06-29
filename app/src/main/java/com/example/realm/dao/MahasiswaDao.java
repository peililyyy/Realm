package com.example.realm.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.realm.model.Mahasiswa;

import java.util.List;

@Dao
public interface MahasiswaDao {

    @Insert
    void insert(Mahasiswa mahasiswa);

    @Update
    void update(Mahasiswa mahasiswa);

    @Delete
    void delete(Mahasiswa mahasiswa);

    @Query("SELECT * FROM mahasiswa ORDER BY id ASC")
    List<Mahasiswa> getAll();

    @Query("SELECT * FROM mahasiswa ORDER BY id DESC")
    List<Mahasiswa> getAllDesc();

    @Query("SELECT * FROM mahasiswa WHERE nama LIKE '%' || :keyword || '%' OR nim LIKE '%' || :keyword || '%'")
    List<Mahasiswa> search(String keyword);
}