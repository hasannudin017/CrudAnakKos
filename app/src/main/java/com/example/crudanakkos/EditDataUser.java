package com.example.crudanakkos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.crudanakkos.R;

import java.util.HashMap;

public class EditDataUser extends AppCompatActivity {

    private EditText etUsername, etNoHp, etPassword;
    private Button btnSave, btnBatal;
    private String username, noHP, passsword;

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data_user);

        etUsername = findViewById(R.id.etUsername);
        etNoHp = findViewById(R.id.etNoHp);
        etPassword = findViewById(R.id.etPassword);
        btnSave = findViewById(R.id.btnSave);
        btnBatal = findViewById(R.id.btnBatal);

        if (getIntent().hasExtra("username") && getIntent().hasExtra("noHp") && getIntent().hasExtra("password")){
            username = getIntent().getStringExtra("username");
            noHP = getIntent().getStringExtra("noHp");
            passsword = getIntent().getStringExtra("password");
        }

        etUsername.setText(username);
        etNoHp.setText(noHP);
        etPassword.setText(passsword);

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Admin.class));
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noHpBaru = etNoHp.getText().toString();
                String passwordBaru = etPassword.getText().toString();

                HashMap hashMap = new HashMap();
                hashMap.put("noHp", noHpBaru);
                hashMap.put("password", passwordBaru);

                database.child(username).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {

                        Toast.makeText(getApplicationContext(), "Update Berhasil", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Admin.class));

                    }
                });
            }
        });
    }
}