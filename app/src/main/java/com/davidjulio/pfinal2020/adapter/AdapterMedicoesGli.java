package com.davidjulio.pfinal2020.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterMedicoesGli extends RecyclerView.Adapter<AdapterMedicoesGli.MyViewMedicoes>{

    public AdapterMedicoesGli(){

    }


    @NonNull
    @Override
    public MyViewMedicoes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewMedicoes holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewMedicoes extends RecyclerView.ViewHolder{

        public MyViewMedicoes(@NonNull View itemView) {
            super(itemView);
        }
    }
}
