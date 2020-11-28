package com.davidjulio.pfinal2020.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.davidjulio.pfinal2020.R;
import com.davidjulio.pfinal2020.model.Lembrete;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterLembretes extends RecyclerView.Adapter<AdapterLembretes.MyViewLembretes> {
    List<Lembrete> lembreteList;
    Context context;

    public AdapterLembretes(List<Lembrete> lembreteList, Context context) {
        this.lembreteList = lembreteList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewLembretes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_lembretes, parent, false);
        return new MyViewLembretes(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewLembretes holder, int position) {
        Lembrete lembrete = lembreteList.get(position);

        holder.tituloLembrete.setText(lembrete.getTitulo());
        holder.descLembrete.setText(lembrete.getDescricao());
        holder.dataLembrete.setText(lembrete.getData());
        holder.horaLembrete.setText(lembrete.getHora());

    }

    @Override
    public int getItemCount() {
        return lembreteList.size();
    }


    public class MyViewLembretes extends RecyclerView.ViewHolder{

        TextView tituloLembrete, descLembrete, dataLembrete, horaLembrete;
        Switch switchLembrete;

        public MyViewLembretes(@NonNull View itemView) {
            super(itemView);

            tituloLembrete = itemView.findViewById(R.id.textViewTituloLembrete);
            descLembrete = itemView.findViewById(R.id.textViewDescLembrete);
            dataLembrete = itemView.findViewById(R.id.textViewDataLembrete);
            horaLembrete = itemView.findViewById(R.id.textViewHoraLembrete);

            switchLembrete = itemView.findViewById(R.id.switchLembrete);
        }
    }
}
