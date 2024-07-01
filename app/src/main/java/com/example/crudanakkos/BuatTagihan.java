package com.example.crudanakkos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.crudanakkos.Tagihan;
import com.example.crudanakkos.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class BuatTagihan extends AppCompatActivity {

    private Spinner spUser;
    private EditText etTotalTagihan, etTanggal;
    private Button btnBuat, btnBatal;

    private DatabaseReference database;
    private ArrayList<String>arrUser;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_tagihan);

        spUser = findViewById(R.id.spUser);
        etTotalTagihan = findViewById(R.id.etTotalTagihan);
        etTanggal = findViewById(R.id.etTanggal);
        btnBuat = findViewById(R.id.btnBuat);
        btnBatal = findViewById(R.id.btnBatal);

        database = FirebaseDatabase.getInstance().getReference();

        arrUser = new ArrayList<>();

        tampilkanDataSpinner();

        //get Tanggal Hari ini
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMM yyyy");
        String Tanggal = dateFormat.format(calendar.getTime());
        etTanggal.setText(Tanggal);

        spUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                user = spUser.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TagihanActivity.class));
            }
        });

        btnBuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String totalTagihan = etTotalTagihan.getText().toString();

                //buat id
                DatabaseReference reference = database.push();
                String id = reference.getKey();

                //simpan di admin
                database.child("admin").child("Tagihan").child(user).setValue(
                        new Tagihan(id, user, totalTagihan, Tanggal, "belum lunas", "null", "null", "null", "Pembayaran Kos Bulan ini" )
                ).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Data Berhasil Di Simpan", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), TagihanActivity.class));
                    }
                });

                //simpan di user
                database.child("users").child(user).child("Tagihan").child(id).setValue(
                        new Tagihan(id, user, totalTagihan, Tanggal, "belum lunas", "null", "null", "null", "Pembayaran Kos Bulan ini" )
                ).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Data Berhasil Di Simpan", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), TagihanActivity.class));
                    }
                });
            }
        });

    }

    private void tampilkanDataSpinner() {
        database.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrUser.clear();

                for (DataSnapshot item : snapshot.getChildren()){
                    arrUser.add(item.child("username").getValue(String.class));
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(BuatTagihan.this, android.R.layout.simple_spinner_dropdown_item, arrUser);
                spUser.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}