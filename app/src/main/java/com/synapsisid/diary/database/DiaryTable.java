package com.synapsisid.diary.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DiaryTable {

    public DiaryTable(int id, String judul, String content, String lastUpdate, String timeCreated,boolean isPrivate, int userId) {
        this.id = id;
        this.judul = judul;
        this.content = content;
        this.lastUpdate = lastUpdate;
        this.timeCreated = timeCreated;
        this.isPrivate = isPrivate;
        this.userId = userId;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private String judul;

    @ColumnInfo
    private String content;

    @ColumnInfo
    private String lastUpdate;

    @ColumnInfo
    private String timeCreated;

    @ColumnInfo
    private boolean isPrivate;

    @ColumnInfo
    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String time_created) {
        this.timeCreated = time_created;
    }

}
