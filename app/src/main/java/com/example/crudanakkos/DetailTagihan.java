package com.example.crudanakkos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.crudanakkos.R;

public class DetailTagihan extends AppCompatActivity {

    private TextView tvNama, tvTotalPembayaran, tvNoKos, tvTanggal, tvDesc;
    private ImageView ivImage;
    private Button btnKembali;

    String idTagihan;
    String username;
    String totalTagihan;
    String tanggal;
    String status;
    String nama;
    String noKos;
    String url;
    String desc;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tagihan);

        tvNama = findViewById(R.id.tvNama);
        tvTotalPembayaran = findViewById(R.id.tvTotalHarga);
        tvNoKos = findViewById(R.id.tvNoKos);
        tvTanggal = findViewById(R.id.tvTanggal);
        tvDesc = findViewById(R.id.tvDesc);
        ivImage = findViewById(R.id.ivImage);
        btnKembali = findViewById(R.id.btnKembali);

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TagihanActivity.class));
            }
        });

        if (getIntent().hasExtra("idTagihan") && getIntent().hasExtra("username")
                && getIntent().hasExtra("totalTagihan") && getIntent().hasExtra("tanggal")
                && getIntent().hasExtra("status") && getIntent().hasExtra("nama")
                && getIntent().hasExtra("noKos") && getIntent().hasExtra("url")
                && getIntent().hasExtra("desc")){

            idTagihan = getIntent().getStringExtra("idTagihan");
            username = getIntent().getStringExtra("username");
            totalTagihan = getIntent().getStringExtra("totalTagihan");
            tanggal = getIntent().getStringExtra("tanggal");
            status = getIntent().getStringExtra("status");
            nama = getIntent().getStringExtra("nama");
            noKos = getIntent().getStringExtra("noKos");
            url = getIntent().getStringExtra("url");
            desc = getIntent().getStringExtra("desc");

            Intent intent = getIntent();
            Glide.with(DetailTagihan.this).load(intent.getStringExtra("url")).into(ivImage);

            if (!(nama.equals("null"))){
                tvNama.setText("Nama : " + nama);
            }else{
                tvNama.setText("User : " + username);
            }

            tvTotalPembayaran.setText("Total Pembayaran : " + totalTagihan);
            tvTanggal.setText("Tanggal : " + tanggal);
            tvDesc.setText("Keterangan : " + desc);

        }



    }
}

