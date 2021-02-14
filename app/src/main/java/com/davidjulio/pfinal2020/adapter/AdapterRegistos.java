package com.davidjulio.pfinal2020.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.davidjulio.pfinal2020.R;
import com.davidjulio.pfinal2020.model.Medicao;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterRegistos extends RecyclerView.Adapter<AdapterRegistos.MyViewRegistos> {

    List<Medicao> medicoes;
    Context context;

    public AdapterRegistos(List<Medicao> medicoes, Context context){
        this.medicoes = medicoes;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewRegistos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_registos, parent, false);
        return new MyViewRegistos(itemLista);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewRegistos holder, int position) {
        Medicao medicao = medicoes.get(position);
        String editavel = medicao.getEditavel();

        if(editavel.equals("S")){
            holder.ivAR.setImageResource(R.drawable.ic_registos);
            holder.tvInfo.setText("Manual");
            holder.linearLayoutInfo.setBackgroundColor(R.color.colorAccent);
        }else{
            holder.ivAR.setImageResource(R.drawable.ic_calculator);
            holder.tvInfo.setText("Calculada");
        }
        holder.tvGlicose.setText("Glicose: " + medicao.getMedicaoGlicose().toString());
        holder.tvDataHora.setText(medicao.getDataHora());
    }

    @Override
    public int getItemCount() {
        return medicoes.size();
    }

    public class MyViewRegistos extends RecyclerView.ViewHolder{

        TextView tvInfo, tvGlicose, tvDataHora;
        ImageView ivAR;
        LinearLayout linearLayoutInfo;

        public MyViewRegistos(@NonNull View itemView) {
            super(itemView);

            ivAR = itemView.findViewById(R.id.ivAR);

            tvInfo = itemView.findViewById(R.id.tvARinfo);
            tvGlicose = itemView.findViewById(R.id.tvARGlicose);
            tvDataHora = itemView.findViewById(R.id.tvARDataHora);

            linearLayoutInfo = itemView.findViewById(R.id.linearLayoutARInfo);

        }
    }
}
