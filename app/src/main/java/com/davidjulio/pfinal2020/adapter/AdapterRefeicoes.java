package com.davidjulio.pfinal2020.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.davidjulio.pfinal2020.R;
import com.davidjulio.pfinal2020.model.Refeicao;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterRefeicoes extends RecyclerView.Adapter<AdapterRefeicoes.MyViewRefeicoes> {

    List<Refeicao> refeicoes;
    Context context;

    public AdapterRefeicoes(List<Refeicao> refeicoes, Context context){

        this.refeicoes = refeicoes;
        this.context = context;

    }
    @NonNull
    @Override
    public MyViewRefeicoes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_refeicoes, parent, false);
        return new MyViewRefeicoes(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewRefeicoes holder, int position) {
        Refeicao refeicao = refeicoes.get(position);

        holder.nomeRefeicao.setText(refeicao.getNome());
        holder.numeroHidratos.setText("Hidratos de Carbono: "+String.valueOf(refeicao.getHidratosCarbono()) + " g");

        if(refeicao.getCalorias() == 0) {
            holder.numeroCalorias.setText("Calorias: " + "-- kcal");
        }else{
            holder.numeroCalorias.setText("Calorias: " + String.valueOf(refeicao.getCalorias()) + " kcal");
        }

        //Image view usando a bibliotecaPicasso
        String urlFoto = refeicao.getUrlFoto();
        Picasso.get().load(urlFoto).into(holder.foto);

    }


    @Override
    public int getItemCount() {
        return refeicoes.size();
    }

    public class MyViewRefeicoes extends RecyclerView.ViewHolder{

        TextView nomeRefeicao, numeroHidratos, numeroCalorias;
        ImageView foto;

        public MyViewRefeicoes(@NonNull View itemView) {
            super(itemView);

            nomeRefeicao = itemView.findViewById(R.id.textNomeRefeicao);
            numeroHidratos = itemView.findViewById(R.id.textNumeroHidratos);
            numeroCalorias = itemView.findViewById(R.id.textNumeroCalorias);
            foto = itemView.findViewById(R.id.ivFotoRefeicao);
        }
    }

}
