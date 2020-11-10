package com.davidjulio.pfinal2020.activity.ui.refeicoes;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.davidjulio.pfinal2020.R;
import com.davidjulio.pfinal2020.adapter.AdapterRefeicoes;
import com.davidjulio.pfinal2020.config.ConfigFirebase;
import com.davidjulio.pfinal2020.helper.Base64Custom;
import com.davidjulio.pfinal2020.model.Refeicao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class RefeicoesFragment extends Fragment {

    //Widgets
    //private SearchView searchViewRefeicoes;
    private FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();

    private RecyclerView recyclerViewRefeicoes;
    private AdapterRefeicoes adapterRefeicoes;

    private List<Refeicao> listaRefeicoes = new ArrayList<>();
    private DatabaseReference refeicaoRef;


    private ValueEventListener valueEventListenerRefeicoes;

    private DatabaseReference utilizadorRef;

    public RefeicoesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_refeicoes, container, false);
        setHasOptionsMenu(true);

        //searchViewRefeicoes = view.findViewById(R.id.searchViewRefeicoes); //view.findview porque é fragment
        recyclerViewRefeicoes = view.findViewById(R.id.recyclerViewRefeicoes);

        //inico
        listaRefeicoes = new ArrayList<>();//lista
        refeicaoRef = ConfigFirebase.getFirebaseDatabase()
                                    .child("refeicoes");


        FloatingActionButton fab = view.findViewById(R.id.fabAdicionarRefeicao);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adicionarRefeicao();
            }
        });

        /*
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
                String textoRefeicao = newText.toUpperCase();


                return true;
            }
        }); //listener para quando o utilizador começa a digitar o texto ou pesquisa */

        //Configura adapter
        adapterRefeicoes = new AdapterRefeicoes(listaRefeicoes, getContext());

        //configurar recycler
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewRefeicoes.setLayoutManager(layoutManager);
        recyclerViewRefeicoes.setHasFixedSize(true);
        recyclerViewRefeicoes.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
        recyclerViewRefeicoes.setAdapter(adapterRefeicoes);
        return view;

    }




    //MENU ADDICIONAR
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pesquisa, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_pesquisa:
                Intent intent = new Intent();
                intent.setClass(getActivity(), AdicionarRefeicaoActivity.class);
                getActivity().startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void adicionarRefeicao(){
        Intent intent = new Intent();
        intent.setClass(getActivity(), AdicionarRefeicaoActivity.class);
        getActivity().startActivity(intent);
    }

    public void recuperarRefeicoes(){
        String emailUtilizador = autenticacao.getCurrentUser().getEmail();
        String idUtilizador = Base64Custom.codificarBase64( emailUtilizador );

        refeicaoRef = firebaseRef.child("refeicoes")
                                 .child( idUtilizador );

        valueEventListenerRefeicoes = refeicaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaRefeicoes.clear();

                for(DataSnapshot dadosRefeicoes: dataSnapshot.getChildren()){
                    Refeicao refeicao = dadosRefeicoes.getValue(Refeicao.class);
                    refeicao.setChave( dadosRefeicoes.getKey() );

                    listaRefeicoes.add( refeicao );
                }
                adapterRefeicoes.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarRefeicoes();
    }

    @Override
    public void onStop() {
        super.onStop();
        refeicaoRef.removeEventListener(valueEventListenerRefeicoes);
    }
}