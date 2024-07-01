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
import com.example.crudanakkos.DetailTagihan;
import com.example.crudanakkos.Tagihan;
import com.example.crudanakkos.R;

import java.util.List;

public class TagihanAdapterUser extends RecyclerView.Adapter<TagihanAdapterUser.MyViewHolder> {

    List<Tagihan> mlist;
    Context context;

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public TagihanAdapterUser(List<Tagihan> mlist, Context context){
        this.mlist = mlist;
        this.context = context;
    }

    @NonNull
    @Override
    public TagihanAdapterUser.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_tagihan_user, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagihanAdapterUser.MyViewHolder holder, int position) {

        final Tagihan item = mlist.get(position);

        if (item != null){
            holder.tvPembayaran.setText("Total Pembayaran : Rp. " + item.getTotalTagihan());
            holder.tvDesc.setText("Keterangan : " + item.getDesc());
            holder.tvStatus.setText("Status : " + item.getStatus());
            holder.tvTanggal.setText(item.getTanggal());

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

                    Intent detail = new Intent(context, DetailTagihanUser.class);
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

        private TextView tvPembayaran, tvDesc, tvStatus, tvTanggal;
        private Button btnDetail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPembayaran = itemView.findViewById(R.id.tvTotalPembayaran);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            btnDetail = itemView.findViewById(R.id.btnDetail);
        }
    }
}