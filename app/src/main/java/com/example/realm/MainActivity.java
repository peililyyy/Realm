package com.example.realm;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realm.adapter.MahasiswaAdapter;
import com.example.realm.database.DatabaseClient;
import com.example.realm.model.Mahasiswa;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    EditText etNim, etNama, etSearch;
    Button btnTambah, btnUpdate, btnHapus, btnSort, btnBack;
    RecyclerView rvMahasiswa;

    MahasiswaAdapter adapter;
    List<Mahasiswa> mahasiswaList = new ArrayList<>();
    Mahasiswa selectedMahasiswa;
    boolean isAscending = true;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNim = findViewById(R.id.etNim);
        etNama = findViewById(R.id.etNama);
        etSearch = findViewById(R.id.etSearch);
        btnTambah = findViewById(R.id.btnTambah);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnHapus = findViewById(R.id.btnHapus);
        btnSort = findViewById(R.id.btnSort);
        btnBack = findViewById(R.id.btnBack);
        rvMahasiswa = findViewById(R.id.rvMahasiswa);

        rvMahasiswa.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MahasiswaAdapter(mahasiswaList, mahasiswa -> {
            selectedMahasiswa = mahasiswa;
            etNim.setText(mahasiswa.getNim());
            etNama.setText(mahasiswa.getNama());
        });
        rvMahasiswa.setAdapter(adapter);

        btnTambah.setOnClickListener(v -> insertData());
        btnUpdate.setOnClickListener(v -> updateData());
        btnHapus.setOnClickListener(v -> deleteData());
        btnSort.setOnClickListener(v -> {
            isAscending = !isAscending;
            btnSort.setText(isAscending ? "SORT: ASC" : "SORT: DESC");
            loadData();
        });
        btnBack.setOnClickListener(v -> finish());

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchData(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        loadData();
    }

    private void loadData() {
        executorService.execute(() -> {
            List<Mahasiswa> list;
            if (isAscending) {
                list = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().mahasiswaDao().getAll();
            } else {
                list = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().mahasiswaDao().getAllDesc();
            }
            runOnUiThread(() -> {
                mahasiswaList = list;
                adapter.setList(mahasiswaList);
            });
        });
    }

    private void searchData(String keyword) {
        executorService.execute(() -> {
            List<Mahasiswa> list = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().mahasiswaDao().search(keyword);
            runOnUiThread(() -> {
                mahasiswaList = list;
                adapter.setList(mahasiswaList);
            });
        });
    }

    private void insertData() {
        String nim = etNim.getText().toString();
        String nama = etNama.getText().toString();

        if (nim.isEmpty() || nama.isEmpty()) {
            Toast.makeText(this, "NIM dan Nama tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        executorService.execute(() -> {
            Mahasiswa m = new Mahasiswa(nim, nama);
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().mahasiswaDao().insert(m);
            runOnUiThread(() -> {
                clearFields();
                loadData();
                Toast.makeText(MainActivity.this, "Data Mahasiswa berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
            });
        });
    }

    private void updateData() {
        if (selectedMahasiswa == null) {
            Toast.makeText(this, "Pilih data yang akan diupdate", Toast.LENGTH_SHORT).show();
            return;
        }

        String nim = etNim.getText().toString();
        String nama = etNama.getText().toString();

        executorService.execute(() -> {
            selectedMahasiswa.setNim(nim);
            selectedMahasiswa.setNama(nama);
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().mahasiswaDao().update(selectedMahasiswa);
            runOnUiThread(() -> {
                clearFields();
                loadData();
                Toast.makeText(MainActivity.this, "Data Mahasiswa berhasil diperbarui!", Toast.LENGTH_SHORT).show();
            });
        });
    }

    private void deleteData() {
        if (selectedMahasiswa == null) {
            Toast.makeText(this, "Pilih data yang akan dihapus", Toast.LENGTH_SHORT).show();
            return;
        }

        executorService.execute(() -> {
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().mahasiswaDao().delete(selectedMahasiswa);
            runOnUiThread(() -> {
                clearFields();
                loadData();
                Toast.makeText(MainActivity.this, "Data Mahasiswa berhasil dihapus!", Toast.LENGTH_SHORT).show();
            });
        });
    }

    private void clearFields() {
        etNim.setText("");
        etNama.setText("");
        selectedMahasiswa = null;
    }
}
