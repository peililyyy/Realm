package com.example.realm.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.realm.dao.MahasiswaDao;
import com.example.realm.model.Mahasiswa;

@Database(entities = {Mahasiswa.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract MahasiswaDao mahasiswaDao();

}