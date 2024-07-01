package com.example.crudanakkos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.example.crudanakkos.DetailTagihan;
import com.example.crudanakkos.TagihanActivity;
import com.example.crudanakkos.Tagihan;
import com.example.crudanakkos.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DetailTagihanUser extends AppCompatActivity {

    private static final int PERMISSION_CAMERA_REQUEST_CODE = 1;
    private static final int PERMISSION_READ_EXTERNAL_STORAGE_REQUEST_CODE = 1;

    private TextView tvNama, tvTotalPembayaran, tvNoKos, tvTanggal, tvDesc;
    private EditText etNama, etNoKos;
    private ImageView ivImage;
    private Button btnKembali, btnBayar;

    String idTagihan;
    String username;
    String totalTagihan;
    String tanggal;
    String status;
    String nama;
    String noKos;
    String url;
    String desc;

    private DatabaseReference database;
    private StorageReference storage;

    private Uri uri;
    private String getNoKos, getNama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tagihan_user);

        tvNama = findViewById(R.id.tvNama);
        etNama = findViewById(R.id.etNama);
        etNoKos = findViewById(R.id.etNoKos);
        tvTotalPembayaran = findViewById(R.id.tvTotalHarga);
        tvNoKos = findViewById(R.id.tvNoKos);
        tvTanggal = findViewById(R.id.tvTanggal);
        tvDesc = findViewById(R.id.tvDesc);
        ivImage = findViewById(R.id.ivImage);
        btnKembali = findViewById(R.id.btnKembali);
        btnBayar = findViewById(R.id.btnBayar);

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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

            tvNama.setText("User : " + username);

            tvTotalPembayaran.setText("Total Pembayaran : " + totalTagihan);
            tvTanggal.setText("Tanggal : " + tanggal);
            tvDesc.setText("Keterangan : " + desc);
        }

        storage = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance().getReference();

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pilihGambar();
                getNama = etNama.getText().toString();
                getNoKos = etNoKos.getText().toString();
            }
        });

        btnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadGambar();
            }
        });

        cekPermission();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //menerima data dari galeri
        if (requestCode == 20 && resultCode == RESULT_OK && data != null){
            final Uri path = data.getData();
            Thread thread = new Thread(() -> {
                try {
                    InputStream stream = getContentResolver().openInputStream(path);
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    ivImage.post(() -> {
                        ivImage.setImageBitmap(bitmap);
                    });
                }catch (IOException e){
                    e.printStackTrace();
                }
            });
            thread.start();
        }

        //menerima data dari kamera
        if (requestCode == 10 && resultCode == RESULT_OK){
            final Bundle extras = data.getExtras();
            Thread thread = new Thread(() -> {
                Bitmap bitmap = (Bitmap) extras.get("data");
                ivImage.post(() -> {
                    ivImage.setImageBitmap(bitmap);
                });
            });
            thread.start();
        }
    }

//    private void uploadGambar() {
//        ivImage.setDrawingCacheEnabled(true);
//        ivImage.buildDrawingCache(true);
//        BitmapDrawable bitmapDrawable = (BitmapDrawable) ivImage.getDrawable();
//        Bitmap bitmap = bitmapDrawable.getBitmap();
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("Uploading...");
//        progressDialog.show();
//
//        //simpan gambar ke file base
//        StorageReference reference = storage.child("Upload/" + System.currentTimeMillis() + ".jpg");
//        UploadTask uploadTask = reference.putBytes(data);
//        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                progressDialog.dismiss();
//
//                Task<Uri> downloadUrlTask = taskSnapshot.getStorage().getDownloadUrl();
//                downloadUrlTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Uri> task) {
//                        if (task.isSuccessful()){
//                            Uri dowloadUrl = task.getResult();
//                            if (dowloadUrl != null){
//                                uri = dowloadUrl;
//
//                                //kita simpan ke database realtime
//                                simpanDataKeDataBaseRealtime();
//                            }
//                        }else{
//                            Toast.makeText(getApplicationContext(), "Upload Gagal", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                progressDialog.dismiss();
//                Toast.makeText(getApplicationContext(), "Upload Gagal", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
//                progressDialog.setMessage("Uploaded : " + (int) progress + "%");
//            }
//        });
//    }

    private void uploadGambar() {
        Log.d("Upload", "Upload dimulai");

        // Mendapatkan bitmap dari ImageView
        ivImage.setDrawingCacheEnabled(true);
        ivImage.buildDrawingCache(true);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) ivImage.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        // Mengubah bitmap menjadi byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        // Menampilkan dialog progress
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        // Mengunggah data ke Firebase Storage
        StorageReference reference = storage.child("Upload/" + System.currentTimeMillis() + ".jpg");
        UploadTask uploadTask = reference.putBytes(data);

        // Listener ketika upload berhasil
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("Upload", "Upload berhasil");

                        // Menutup progress dialog
                        progressDialog.dismiss();

                        // Mendapatkan URL download gambar
                        taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    uri = task.getResult();
                                    if (uri != null) {
                                        Log.d("Upload", "URL didapat: " + uri.toString());

                                        // Simpan URL ke Realtime Database
                                        simpanDataKeDataBaseRealtime();
                                    }
                                } else {
                                    Log.e("Upload", "Gagal mendapatkan URL: " + task.getException().getMessage());
                                    Toast.makeText(getApplicationContext(), "Upload Gagal: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                // Listener ketika upload gagal
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Upload", "Upload gagal: " + e.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Upload Gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                // Listener untuk mengupdate progress
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded: " + (int) progress + "%");
                    }
                });
    }

    private void simpanDataKeDataBaseRealtime() {
        // Simpan data ke "admin/Tagihan/username"
        database.child("admin").child("Tagihan").child(username).setValue(
                new Tagihan(idTagihan, username, totalTagihan, tanggal, "lunas", getNoKos, getNama, uri.toString(), "Pembayaran Kos Bulan ini" )
        ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Data Berhasil Di Simpan", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Gagal menyimpan data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Simpan data ke "users/username/Tagihan/idTagihan"
        database.child("users").child(username).child("Tagihan").child(idTagihan).setValue(
                new Tagihan(idTagihan, username, totalTagihan, tanggal, "lunas", getNoKos, getNama, uri.toString(), "Pembayaran Kos Bulan ini" )
        ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Data Berhasil Di Simpan", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Gagal menyimpan data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void cekPermission() {
        //cek izin kamera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            //jika izin belum diberikan, minta izin kamera
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA_REQUEST_CODE);
        }

        //cek izin baca penyimpanan external
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_READ_EXTERNAL_STORAGE_REQUEST_CODE);
        }
    }


    private void pilihGambar() {
        final CharSequence[] items = {"Ambil Foto", "Ambil Dari Galeri", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailTagihanUser.this);
        builder.setTitle("Pilih Gambar");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals("Ambil Foto")){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 10);
            } else if (items[item].equals("Ambil Dari Galeri")) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), 20);
            } else if (items[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}