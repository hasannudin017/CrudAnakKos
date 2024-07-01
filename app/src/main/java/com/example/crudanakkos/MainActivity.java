package com.example.crudanakkos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.crudanakkos.TagihanActivity;
import com.example.crudanakkos.TagihanAdapter;
import com.example.crudanakkos.Tagihan;
import com.example.crudanakkos.Login;
import com.example.crudanakkos.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //sharedPreferences
    private SharedPreferences sharedPreferences;
    public static final String SHARED_PREF_NAME = "myPref";
    public static final String KEY_USER = "user";

    private ImageView ivKeluar;
    private RecyclerView rvTagihan;
    private DatabaseReference database;
    private TagihanAdapterUser adapter;
    private ArrayList<Tagihan> arrayList;

    private String user;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivKeluar =findViewById(R.id.ivKeluar);
        rvTagihan =findViewById(R.id.rvTagihan);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        user = sharedPreferences.getString(KEY_USER, null);

        ivKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
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
        database.child("users").child(user).child("Tagihan").addValueEventListener(new ValueEventListener() {
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

                adapter = new TagihanAdapterUser(arrayList, MainActivity.this);
                rvTagihan.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}