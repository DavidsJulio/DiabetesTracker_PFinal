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
import com.davidjulio.pfinal2020.helper.Base64Custom;
import com.davidjulio.pfinal2020.model.Calculadora;
import com.davidjulio.pfinal2020.model.Medicao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HomeFragment extends Fragment {

    private TextView tvHomeFSI, tvHomeIHC, tvHomeGAlvo, tv13;

    private FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();
    private DatabaseReference perfilRef = ConfigFirebase.getFirebaseDatabase().child("perfil");
    private Calculadora calculadora;
    private ValueEventListener valueEventListenerPerfil;

    private RecyclerView rvMedicoes;
    private AdapterMedicoesGli adapterMedicoes;
    private List<Medicao> listaMedicoes = new ArrayList<>();
    private DatabaseReference medicoesRef = ConfigFirebase.getFirebaseDatabase().child("medicoesGlicose");
    private ValueEventListener valueEventListenerMedicoes;

    public HomeFragment() {
        // Required empty public constructor
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
        tv13 = view.findViewById(R.id.textViewHomeInfoRv);


        adapterMedicoes = new AdapterMedicoesGli(listaMedicoes, getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvMedicoes.setLayoutManager(layoutManager);
        rvMedicoes.setHasFixedSize(true);
        rvMedicoes.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
        rvMedicoes.setAdapter(adapterMedicoes);

        return view;
    }

    public void recuperarDadosPerfil(){
        String idUtilizador = ConfigFirebase.getCurrentUser();
        perfilRef = firebaseRef.child("perfil")
                .child( idUtilizador );

        valueEventListenerPerfil = perfilRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    calculadora = (Calculadora) dataSnapshot.getValue(Calculadora.class);
                    String fsi = String.valueOf(calculadora.getFsi());
                    tvHomeFSI.setText(fsi);
                    String rHC = String.valueOf(calculadora.getrHC());
                    tvHomeIHC.setText(rHC);
                    String glimeciaAlvo = String.valueOf(calculadora.getGlicemiaAlvo());
                    tvHomeGAlvo.setText(glimeciaAlvo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void recuperarMedicoes(){
       /* String emailUtilizador = autenticacao.getCurrentUser().getEmail();
        String idUtilizador = Base64Custom.codificarBase64( emailUtilizador );*/

        String idUtilizador = ConfigFirebase.getCurrentUser();
        medicoesRef = firebaseRef.child("medicoesGlicose")
                                 .child( idUtilizador );

        valueEventListenerMedicoes = medicoesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    tv13.setText("Ultimas Medições:");
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
        //recuperarPerfil
        recuperarDadosPerfil();
        recuperarMedicoes();
    }

    @Override
    public void onStop() {
        super.onStop();
        perfilRef.removeEventListener(valueEventListenerPerfil);
        medicoesRef.removeEventListener(valueEventListenerMedicoes);
    }
}