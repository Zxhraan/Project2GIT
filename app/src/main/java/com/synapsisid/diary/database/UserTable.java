package com.synapsisid.diary.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class UserTable {

    public UserTable(int id, String nama, String tanggalLahir, String gender, String email, String password) {
        this.id = id;
        this.nama = nama;
        this.tanggalLahir = tanggalLahir;
        this.gender = gender;
        this.email = email;
        this.password = password;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private String nama;

    @ColumnInfo
    private String tanggalLahir;

    @ColumnInfo
    private String gender;

    @ColumnInfo
    private String email;

    @ColumnInfo
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(String tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
