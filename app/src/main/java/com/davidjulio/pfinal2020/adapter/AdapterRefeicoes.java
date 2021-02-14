package com.davidjulio.pfinal2020.adapter;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterRefeicoes extends RecyclerView.Adapter<AdapterRefeicoes.MyViewRefeicoes> { //implements Filterable

    List<Refeicao> refeicoes;
    //List<Refeicao> refeicoesFilter;
    Context context;

    public AdapterRefeicoes(List<Refeicao> refeicoes, Context context){
        this.refeicoes = refeicoes;
        this.context = context;
        //refeicoesFilter = new ArrayList<>(refeicoes); //copy para usar de maneira independente
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

        //Image view utilizando a biblioteca Picasso
        String urlFoto = refeicao.getUrlFoto();
        Picasso.get().load(urlFoto).into(holder.foto);

    }


    @Override
    public int getItemCount() {
        return refeicoes.size();
    }

    /*
    @Override
    public Filter getFilter() {
        return filter;
    }


    public Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
           //retornar o resultado
            List<Refeicao> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredList.add((Refeicao) refeicoesFilter);
            }else{
                String filtro = constraint.toString().toLowerCase().trim();

                for(Refeicao item: refeicoesFilter){
                    if(item.getNome().toLowerCase().contains(filtro)){
                        filteredList.add(item);
                    }
                }

            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            refeicoes.clear();
            refeicoes.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };*/


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
