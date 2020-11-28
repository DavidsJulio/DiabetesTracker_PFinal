package com.davidjulio.pfinal2020.activity.ui.refeicoes;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.davidjulio.pfinal2020.R;
import com.davidjulio.pfinal2020.adapter.AdapterRefeicoes;
import com.davidjulio.pfinal2020.config.ConfigFirebase;
import com.davidjulio.pfinal2020.helper.Base64Custom;
import com.davidjulio.pfinal2020.helper.RecyclerItemClickListener;
import com.davidjulio.pfinal2020.model.Refeicao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;


public class RefeicoesFragment extends Fragment {

    private FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();

    private RecyclerView recyclerViewRefeicoes;
    private AdapterRefeicoes adapterRefeicoes;

    private List<Refeicao> listaRefeicoes = new ArrayList<>();
    private DatabaseReference refeicaoRef;

    private ValueEventListener valueEventListenerRefeicoes;

   // private SearchView searchView;



    public RefeicoesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_refeicoes, container, false);
        setHasOptionsMenu(true);

        recyclerViewRefeicoes = view.findViewById(R.id.recyclerViewRefeicoes);

        //inico
        listaRefeicoes = new ArrayList<>();//lista
        refeicaoRef = ConfigFirebase.getFirebaseDatabase()
                                    .child("refeicoes");

        FloatingActionButton fab = view.findViewById(R.id.fabAdicionarRefeicoes);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adicionarRefeicao();
            }
        });

        //Configurar adapter
        adapterRefeicoes = new AdapterRefeicoes(listaRefeicoes, getContext());

        //Configurar recycler
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewRefeicoes.setLayoutManager(layoutManager);
        recyclerViewRefeicoes.setHasFixedSize(true);
        recyclerViewRefeicoes.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
        recyclerViewRefeicoes.setAdapter(adapterRefeicoes);

        //evento de click
        recyclerViewRefeicoes.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerViewRefeicoes,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Refeicao refeicaoSelecionada = listaRefeicoes.get(position);

                                Intent intent = new Intent(getActivity(), AdicionarRefeicaoActivity.class);
                                intent.putExtra("refeicao", refeicaoSelecionada);
                                startActivity(intent);
                            }
                            @Override
                            public void onLongItemClick(View view, int position) {
                            }
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            }
                        })
        );
        return view;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_pesquisa, menu);

        /*
        MenuItem item = menu.findItem(R.id.action_pesquisa);
        SearchView searchView = new SearchView(((TelaPrincipalActivity) getActivity()).getSupportActionBar().getThemedContext());
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);//MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW| MenuItem.SHOW_AS_ACTION_IF_ROOM  MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setActionView(searchView);
        
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE); //mudar o teclado

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapterRefeicoes.getFilter().filter(newText);
                return true;
            }
        });


        //dapterRefeicoes.getFilter().filter(newText);
        /*searchView.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {

                                          }
                                      }
        );*/

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

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
                    refeicao.setIdRefeicao( dadosRefeicoes.getKey() );
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