package com.davidjulio.pfinal2020.activity.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.davidjulio.pfinal2020.R;
import com.davidjulio.pfinal2020.config.ConfigFirebase;
import com.davidjulio.pfinal2020.helper.Base64Custom;
import com.davidjulio.pfinal2020.model.Calculadora;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment {

    private TextView tvHomeFSI, tvHomeIHC, tvHomeGAlvo;

    private FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();
    private DatabaseReference perfilRef = ConfigFirebase.getFirebaseDatabase().child("perfil");
    private Calculadora calculadora;
    private ValueEventListener valueEventListenerPerfil;


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



        return view;
    }

    public void recuperarDadosPerfil(){
        String emailUtilizador = autenticacao.getCurrentUser().getEmail();
        String idUtilizador = Base64Custom.codificarBase64( emailUtilizador );

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

    @Override
    public void onStart() {
        super.onStart();
        //recuperarPerfil
        recuperarDadosPerfil();
    }

    @Override
    public void onStop() {
        super.onStop();
        perfilRef.removeEventListener(valueEventListenerPerfil);
    }
}