package com.example.crudanakkos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.crudanakkos.Admin;
import com.example.crudanakkos.Tagihan;
import com.example.crudanakkos.R;

import java.util.ArrayList;

public class TagihanActivity extends AppCompatActivity {

    private TextView tvBack;
    private RecyclerView rvTagihan;
    private FloatingActionButton fab;

    private DatabaseReference database;
    private TagihanAdapter adapter;
    private ArrayList<Tagihan> arrayList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagihan);

        tvBack = findViewById(R.id.tvBack);
        rvTagihan = findViewById(R.id.rvTagihan);
        fab = findViewById(R.id.fab);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(getApplicationContext(), Admin.class);
                startActivity(back);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent buat = new Intent(getApplicationContext(), BuatTagihan.class);
                startActivity(buat);
            }
        });

        database = FirebaseDatabase.getInstance().getReference();

        rvTagihan.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvTagihan.setLayoutManager(layoutManager);
        rvTagihan.setItemAnimator(new DefaultItemAnimator());

        tampilData();
    }

    private void tampilData() {
        database.child("admin").child("Tagihan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList= new ArrayList<>();

                for (DataSnapshot item : snapshot.getChildren()){
                    Tagihan tagihan = new Tagihan();
                    tagihan.setIdTagihan(item.child("idTagihan").getValue(String.class));
                    tagihan.setUsername(item.child("username").getValue(String.class));
                    tagihan.setTotalTagihan(item.child("totalTagihan").getValue(String.class));
                    tagihan.setTanggal(item.child("tanggal").getValue(String.class));
                    tagihan.setStatus(item.child("status").getValue(String.class));
                    tagihan.setNama(item.child("nama").getValue(String.class));
                    tagihan.setNoKos(item.child("noKos").getValue(String.class));
                    tagihan.setUrl(item.child("url").getValue(String.class));
                    tagihan.setDesc(item.child("desc").getValue(String.class));
                    arrayList.add(tagihan);
                }

                adapter = new TagihanAdapter(arrayList, TagihanActivity.this);
                rvTagihan.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}