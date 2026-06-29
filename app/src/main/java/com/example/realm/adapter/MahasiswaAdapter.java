package com.example.realm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realm.R;
import com.example.realm.model.Mahasiswa;

import java.util.List;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.ViewHolder> {

    private List<Mahasiswa> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Mahasiswa mahasiswa);
    }

    public MahasiswaAdapter(List<Mahasiswa> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    public void setList(List<Mahasiswa> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mahasiswa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mahasiswa mahasiswa = list.get(position);
        holder.tvNama.setText(mahasiswa.getNama());
        holder.tvNim.setText("NIM: " + mahasiswa.getNim());
        holder.tvId.setText("ID: " + mahasiswa.getId());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(mahasiswa);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvNim, tvId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvNim = itemView.findViewById(R.id.tvNim);
            tvId = itemView.findViewById(R.id.tvId);
        }
    }
}
