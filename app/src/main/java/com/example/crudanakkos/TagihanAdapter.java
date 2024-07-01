package com.example.crudanakkos;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.crudanakkos.Tagihan;
import com.example.crudanakkos.R;

import java.util.List;

public class TagihanAdapter extends RecyclerView.Adapter<TagihanAdapter.MyViewHolder> {

    List<Tagihan> mlist;
    Context context;

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public TagihanAdapter(List<Tagihan> mlist, Context context){
        this.mlist = mlist;
        this.context = context;
    }

    @NonNull
    @Override
    public TagihanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_tagihan_admin, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagihanAdapter.MyViewHolder holder, int position) {

        final Tagihan item = mlist.get(position);

        if (item != null){
            holder.tvNama.setText("Nama : " + item.getNama());
            holder.tvNoKos.setText("No Kos : " + item.getNoKos());
            holder.tvStatus.setText("Status : " + item.getStatus());
            holder.tvTanggal.setText(item.getTanggal());

            holder.btnHapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //hapus di admin
                    database.child("admin").child("Tagihan").child(item.getUsername()).setValue(null);

                    //hapus di user
                    database.child("users").child(item.getUsername()).child("Tagihan").child(item.getIdTagihan()).setValue(null);

                }
            });

            holder.btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String idTagihan = item.getIdTagihan();
                    String username = item.getUsername();
                    String totalTagihan = item.getTotalTagihan();
                    String tanggal = item.getTanggal();
                    String status = item.getStatus();
                    String nama = item.getNama();
                    String noKos = item.getNoKos();
                    String url = item.getUrl();
                    String desc = item.getDesc();

                    Intent detail = new Intent(context, DetailTagihan.class);
                    detail.putExtra("idTagihan", idTagihan);
                    detail.putExtra("username", username);
                    detail.putExtra("totalTagihan", totalTagihan);
                    detail.putExtra("tanggal", tanggal);
                    detail.putExtra("status", status);
                    detail.putExtra("nama", nama);
                    detail.putExtra("noKos", noKos);
                    detail.putExtra("url", url);
                    detail.putExtra("desc", desc);
                    context.startActivity(detail);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNama, tvNoKos, tvStatus, tvTanggal;
        private Button btnDetail, btnHapus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNama = itemView.findViewById(R.id.tvNama);
            tvNoKos = itemView.findViewById(R.id.tvNoKos);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            btnDetail = itemView.findViewById(R.id.btnDetail);
            btnHapus = itemView.findViewById(R.id.btnHapus);
        }
    }
}