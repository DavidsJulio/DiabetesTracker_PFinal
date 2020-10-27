package com.davidjulio.pfinal2020.activity.ui.refeicoes;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.davidjulio.pfinal2020.R;


public class RefeicoesFragment extends Fragment {

    //Widgets
    private SearchView searchViewRefeicoes;
    private RecyclerView recyclerViewRefeicoes;

    //private List<Refeicoes> refeicoes;

    public RefeicoesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_refeicoes, container, false);
        setHasOptionsMenu(true);

        searchViewRefeicoes = view.findViewById(R.id.searchViewRefeicoes);
        recyclerViewRefeicoes = view.findViewById(R.id.recyclerViewRefeicoes);

        //Configurar searchView
        searchViewRefeicoes.setQueryHint("Pesquisar Refeições"); //quando clica na lupa, msg referente
        searchViewRefeicoes.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Log.d("onQueryTextSubmit", "texto digitado: "+ query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.d("onQueryTextChange", "texto digitado: "+ newText);
                String refeicao = newText.toUpperCase();
                pesquisarRefeicao( refeicao );

                return true;
            }
        }); //listener para quando o utilizador começa a digitar o texto ou pesquisa
        return view;
    }

    private void pesquisarRefeicao(String refeicao){

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                Intent intent = new Intent();
                intent.setClass(getActivity(), AdicionarRefeicaoActivity.class);
                getActivity().startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}