package com.davidjulio.pfinal2020.activity.ui.registos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.davidjulio.pfinal2020.R;
import com.davidjulio.pfinal2020.adapter.AdapterRegistos;
import com.davidjulio.pfinal2020.config.ConfigFirebase;
import com.davidjulio.pfinal2020.helper.RecyclerItemClickListener;
import com.davidjulio.pfinal2020.model.Medicao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class RegistosFragment extends Fragment {

    private RecyclerView rvRegistosListagem;
    private FloatingActionButton fabAdicionarRegisto;
    private LinearLayout linearLayoutInfo;

    private List<Medicao> listaMedicoes;
    private DatabaseReference registoRef;
    private AdapterRegistos adapterRegistos;
    private DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();
    private ValueEventListener valueEventListenerRegisto;
    public static final String MEDICAO_SELECIONADA = "medicao";

    public RegistosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registos, container, false);

        rvRegistosListagem = view.findViewById(R.id.rvRegistosListagem);
        fabAdicionarRegisto = view.findViewById(R.id.fabAdicionarRegisto);


        listaMedicoes = new ArrayList<>();
        registoRef = ConfigFirebase.getFirebaseDatabase().child("medicoesGlicose");

        adapterRegistos = new AdapterRegistos(listaMedicoes, getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvRegistosListagem.setLayoutManager(layoutManager);
        rvRegistosListagem.setHasFixedSize(true);
        rvRegistosListagem.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
        rvRegistosListagem.setAdapter(adapterRegistos);


        rvRegistosListagem.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rvRegistosListagem,
                                                  new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Medicao medicaoSelecionada = listaMedicoes.get(position);
                        Intent iRegistos = new Intent(getActivity(), AdicionarRegistosActivity.class);
                        iRegistos.putExtra(MEDICAO_SELECIONADA, medicaoSelecionada);
                        startActivity(iRegistos);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }));


        fabAdicionarRegisto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarRegistos();
            }
        });


        return view;
    }

    public void adicionarRegistos(){
        Intent intent = new Intent();
        intent.setClass(getActivity(), AdicionarRegistosActivity.class);
        getActivity().startActivity(intent);
    }

    public void recuperarRegistos(){
        String idUtilizador = ConfigFirebase.getCurrentUser();

        registoRef = firebaseRef.child("medicoesGlicose").child(idUtilizador);

        valueEventListenerRegisto = registoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    listaMedicoes.clear();

                    for(DataSnapshot dadosRegisto: dataSnapshot.getChildren()){
                        Medicao medicao = dadosRegisto.getValue(Medicao.class);
                        medicao.setIdMedicao( dadosRegisto.getKey() );
                        listaMedicoes.add( medicao );
                    }
                    Collections.sort(listaMedicoes, Medicao.BY_DATE_DESCENDING);
                    adapterRegistos.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarRegistos();
    }

    @Override
    public void onStop() {
        super.onStop();
        registoRef.removeEventListener(valueEventListenerRegisto);
    }
}