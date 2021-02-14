package com.davidjulio.pfinal2020.activity.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.davidjulio.pfinal2020.R;
import com.davidjulio.pfinal2020.adapter.AdapterMedicoesGli;
import com.davidjulio.pfinal2020.config.ConfigFirebase;
import com.davidjulio.pfinal2020.model.Medicao;
import com.davidjulio.pfinal2020.model.Utilizador;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HomeFragment extends Fragment {

    private TextView tvHomeFSI, tvHomeIHC, tvHomeGAlvo, tvInfoNrMedicoes;

    private DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();
    private DatabaseReference utilizadorRef;
    private Utilizador utilizador;
    private ValueEventListener valueEventListenerUtilizador;

    private RecyclerView rvMedicoes;
    private AdapterMedicoesGli adapterMedicoes;
    private List<Medicao> listaMedicoes = new ArrayList<>();
    private DatabaseReference medicoesRef = ConfigFirebase.getFirebaseDatabase().child("medicoesGlicose");
    private ValueEventListener valueEventListenerMedicoes;

    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvHomeFSI = view.findViewById(R.id.textViewHomeFSI);
        tvHomeIHC = view.findViewById(R.id.textViewHomeIHC);
        tvHomeGAlvo = view.findViewById(R.id.textViewHomeGAlvo);
        rvMedicoes = view.findViewById(R.id.rvHomeMedicoes);
        tvInfoNrMedicoes = view.findViewById(R.id.textViewHomeInfoRv);

        adapterMedicoes = new AdapterMedicoesGli(listaMedicoes, getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvMedicoes.setLayoutManager(layoutManager);
        rvMedicoes.setHasFixedSize(true);
        rvMedicoes.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
        rvMedicoes.setAdapter(adapterMedicoes);

        return view;
    }

    public void recuperarDadosUtilizador(){
        String idUtilizador = ConfigFirebase.getCurrentUser();
        utilizadorRef = firebaseRef.child("utilizadores")
                                    .child( idUtilizador );

        valueEventListenerUtilizador = utilizadorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    utilizador = (Utilizador) dataSnapshot.getValue(Utilizador.class);
                    String fsi = String.valueOf(utilizador.getFsi());
                    if (fsi.equals("null")){
                        tvHomeFSI.setText("");
                    }else{
                        tvHomeFSI.setText(fsi);
                    }
                    String rHC = String.valueOf(utilizador.getrHC());
                    if (rHC.equals("null")){
                        tvHomeIHC.setText("");
                    }else{
                        tvHomeIHC.setText(rHC);
                    }
                    String glimeciaAlvo = String.valueOf(utilizador.getGlicemiaAlvo());
                    if(glimeciaAlvo.equals("null")){
                        tvHomeGAlvo.setText("");
                    }else {
                        tvHomeGAlvo.setText(glimeciaAlvo);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void recuperarMedicoes(){

        String idUtilizador = ConfigFirebase.getCurrentUser();
        medicoesRef = firebaseRef.child("medicoesGlicose")
                                 .child( idUtilizador );

        valueEventListenerMedicoes = medicoesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    tvInfoNrMedicoes.setText("Ultimas Medições:");
                    listaMedicoes.clear();

                    for (DataSnapshot dadosMedicoes : dataSnapshot.getChildren()) {
                        Medicao medicao = dadosMedicoes.getValue(Medicao.class);
                        medicao.setIdMedicao(dadosMedicoes.getKey());
                        listaMedicoes.add(medicao);
                    }
                    Collections.sort(listaMedicoes, Medicao.BY_DATE_DESCENDING);
                    adapterMedicoes.notifyDataSetChanged();
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
        recuperarDadosUtilizador();
        recuperarMedicoes();
    }

    @Override
    public void onStop() {
        super.onStop();
        utilizadorRef.removeEventListener(valueEventListenerUtilizador);
        medicoesRef.removeEventListener(valueEventListenerMedicoes);
    }
}