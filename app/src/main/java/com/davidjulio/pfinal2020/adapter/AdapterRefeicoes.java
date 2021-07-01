package com.davidjulio.pfinal2020.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.davidjulio.pfinal2020.R;
import com.davidjulio.pfinal2020.model.Refeicao;
import com.squareup.picasso.Picasso;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterRefeicoes extends RecyclerView.Adapter<AdapterRefeicoes.MyViewRefeicoes>{ // implements Filterable

    List<Refeicao> refeicoes;
    Context context;
 /*   List<Refeicao> refeicoesListFiltered;*/


    public AdapterRefeicoes(List<Refeicao> refeicoes, Context context){
        this.refeicoes = refeicoes;
        this.context = context;
       /* this.refeicoesListFiltered = refeicoes;*/
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
        holder.numeroHidratos.setText("Carbohydrates: "+String.valueOf(refeicao.getHidratosCarbono()) + " g");

        if(refeicao.getCalorias() == 0) {
            holder.numeroCalorias.setText("Calories: " + "-- kcal");
        }else{
            holder.numeroCalorias.setText("Calories: " + String.valueOf(refeicao.getCalorias()) + " kcal");
        }

        //Image view utilizando a biblioteca Picasso
        String urlFoto = refeicao.getUrlFoto();
        Picasso.get().load(urlFoto).into(holder.foto);

    }


    @Override
    public int getItemCount() {
        return refeicoes.size();
    }


   /* @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if(charString.isEmpty()){
                    refeicoesListFiltered = refeicoes;
                }else{
                    List<Refeicao> filteredList = new ArrayList<>();
                    for(Refeicao row : refeicoes){
                        if(row.getNome().toLowerCase().contains(charString.toLowerCase())){
                            filteredList.add(row);
                        }

                    }
                    refeicoesListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = refeicoesListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                refeicoesListFiltered = (ArrayList<Refeicao>) results.values;
                notifyDataSetChanged();
            }
        };
    }*/


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
