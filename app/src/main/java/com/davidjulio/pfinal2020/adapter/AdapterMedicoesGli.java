package com.davidjulio.pfinal2020.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.davidjulio.pfinal2020.R;
import com.davidjulio.pfinal2020.model.Medicao;

import java.text.SimpleDateFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterMedicoesGli extends RecyclerView.Adapter<AdapterMedicoesGli.MyViewMedicoes>{

    List<Medicao> medicoes;
    Context context;

    public AdapterMedicoesGli(List<Medicao> medicoes, Context context){
        this.medicoes = medicoes;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewMedicoes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_medicoes, parent, false);
        return new MyViewMedicoes(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewMedicoes holder, int position) {
        Medicao medicao = medicoes.get(position);
        holder.tvDataHora.setText(medicao.getDataHora());
        holder.tvValorGlicose.setText(String.valueOf(medicao.getMedicaoGlicose()) );
    }

    @Override
    public int getItemCount() {
        return medicoes.size();
    }

    public class MyViewMedicoes extends RecyclerView.ViewHolder{

        TextView tvDataHora, tvValorGlicose;
        public MyViewMedicoes(@NonNull View itemView) {
            super(itemView);

            tvDataHora = itemView.findViewById(R.id.tvDataHoraAdapter);
            tvValorGlicose = itemView.findViewById(R.id.tvValorGlicoseAdapter);
        }
    }
}
