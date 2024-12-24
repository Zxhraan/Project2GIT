package com.synapsisid.diary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.synapsisid.diary.database.AppDatabase;
import com.synapsisid.diary.database.DiaryTable;
import com.synapsisid.diary.database.UserTable;
import com.synapsisid.diary.databinding.ActivityHomeBinding;


import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private DiaryListAdapter adapter;
    private ArrayList<DiaryTable> listDiary = new ArrayList<>();
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("my_pref", Context.MODE_PRIVATE);
        id = sharedPreferences.getInt("USER_ID", -1);

        prepareRc();
        listener();
        getData();

        LocalBroadcastManager.getInstance(this).registerReceiver(bc, new IntentFilter("LOGOUT"));
    }



    private void getData() {
        List<DiaryTable> listDiaryDb = AppDatabase.getInstance(this)
                .databaseDao().getListPublicDiary(id);

        listDiary.clear();
        if (listDiaryDb != null) {
            listDiary.addAll(listDiaryDb);
        }
        adapter.setNewData(listDiary);
        adapter.notifyDataSetChanged();

        List<UserTable> user = AppDatabase.getInstance(this)
                .databaseDao().getProfileData(id);

    }

    private void prepareRc() {
        adapter = new DiaryListAdapter(this);
        binding.rcDiary.setLayoutManager(new LinearLayoutManager(this));
        binding.rcDiary.setAdapter(adapter);
    }

    private void searchDiary(String keyword) {
        List<DiaryTable> filteredList = new ArrayList<>();
        for (DiaryTable diary : listDiary) {
            if (diary.getJudul().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(diary);
            }
        }
        adapter.setNewData(filteredList);
        adapter.notifyDataSetChanged(); // Perbaikan: Menyegarkan tampilan RecyclerView
    }

    private void listener() {
        binding.tilSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchDiary(newText);
                return false;
            }
        });


        binding.swipeLayout.setOnRefreshListener(() -> {
            binding.tilSearch.setQuery("", false);
            getData();
            binding.swipeLayout.setRefreshing(false);
        });



        binding.btnCreateNew.setOnClickListener(view ->
                startActivity(new Intent(HomeActivity.this, DiaryActivity.class)));
    }

    BroadcastReceiver bc = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            HomeActivity.this.finish();
        }
    };
}
