package com.synapsisid.diary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.synapsisid.diary.database.AppDatabase;
import com.synapsisid.diary.database.DatabaseDao;
import com.synapsisid.diary.database.DiaryTable;
import com.synapsisid.diary.databinding.ActivityDiaryBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DiaryActivity extends AppCompatActivity {

    public static String DIARY_ID_KEY = "DIARY_ID";

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMMM-dd HH:mm");

    private ActivityDiaryBinding binding;
    private boolean isCreateNew = true;
    private DatabaseDao dao;

    private DiaryTable currentData;

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("my_pref", Context.MODE_PRIVATE);
        id = sharedPreferences.getInt("USER_ID",-1);

        dao = AppDatabase.getInstance(this).databaseDao();

        receiveArguments();
        listener();

    }

    private void deleteData(){

        new AlertDialog.Builder(this).setMessage("Apakah anda yakin untuk menghapus diary ini?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dao.deleteDiary(currentData.getId());
                        DiaryActivity.this.finish();
                    }
                }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        }).show();

    }

    private void getData(int id){
        currentData = dao.getDiary(id);

        if(currentData==null){
            Toast.makeText(this, "Diary ini sudah terhapus", Toast.LENGTH_LONG).show();
            this.finish();
        }else{
            binding.etTitle.setText(currentData.getJudul());
            binding.etContent.setText(currentData.getContent());

            String time = "Created at "+currentData.getTimeCreated();
            time += "\nLast updated at "+currentData.getLastUpdate();


            binding.switchPrivate.setChecked(currentData.isPrivate());
        }

    }

    private boolean validate(){
        boolean isValidated = true;

        if(binding.etTitle.getText().toString().isEmpty())
            isValidated = false;

        if(binding.etContent.getText().toString().isEmpty())
            isValidated = false;

        return isValidated;
    }

    private void receiveArguments(){
        Integer diaryId = getIntent().getIntExtra(DIARY_ID_KEY,-1);

        if(diaryId > 0){
            isCreateNew = false;
            getData(diaryId);
        }
    }

    private void listener(){

        binding.switchPrivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!isCreateNew){
                    dao.updateDiary(
                            new DiaryTable(
                                    currentData.getId(),
                                    binding.etTitle.getText().toString(),
                                    binding.etContent.getText().toString(),
                                    currentData.getTimeCreated(),
                                    currentData.getLastUpdate(),
                                    b,
                                    id
                            )
                    );
                    Toast.makeText(DiaryActivity.this,"Berhasil mengubah mode private",Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(DiaryActivity.this).setMessage("Data yang belum tersimpan akan hilang, lanjut keluar?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DiaryActivity.this.finish();
                            }
                        }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                }).show();

            }
        });

        binding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.m_delete:
                        if(isCreateNew == false){
                            deleteData();

                        }
                }

                return true;
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    if (isCreateNew) {
                        dao.saveDiary(
                                new DiaryTable(
                                        0,
                                        binding.etTitle.getText().toString(),
                                        binding.etContent.getText().toString(),
                                        sdf.format(Calendar.getInstance().getTime()),
                                        sdf.format(Calendar.getInstance().getTime()),
                                        binding.switchPrivate.isChecked(),
                                        id
                                )
                        );
                        Toast.makeText(DiaryActivity.this, "Berhasil membuat diary baru", Toast.LENGTH_LONG).show();
                    } else {
                        dao.updateDiary(
                                new DiaryTable(
                                        currentData.getId(),
                                        binding.etTitle.getText().toString(),
                                        binding.etContent.getText().toString(),
                                        currentData.getTimeCreated(),
                                        sdf.format(Calendar.getInstance().getTime()),
                                        binding.switchPrivate.isChecked(),
                                        id
                                )
                        );
                        Toast.makeText(DiaryActivity.this, "Data berhasil disimpan", Toast.LENGTH_LONG).show();
                    }

                    // Tambahkan kode untuk berpindah ke HomeActivity
                    Intent intent = new Intent(DiaryActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish(); // Menutup aktivitas saat ini agar tidak dapat kembali ke halaman ini.
                } else {
                    Toast.makeText(DiaryActivity.this, "Field tidak boleh kosong", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}