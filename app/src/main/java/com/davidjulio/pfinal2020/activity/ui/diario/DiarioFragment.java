package com.davidjulio.pfinal2020.activity.ui.diario;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.davidjulio.pfinal2020.R;
import com.davidjulio.pfinal2020.config.ConfigFirebase;
import com.davidjulio.pfinal2020.helper.Base64Custom;
import com.davidjulio.pfinal2020.model.Calculadora;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class DiarioFragment extends Fragment {

    private EditText etFsiPerfil, etRhcPerfil, etGliAlvoPerfil;
    private FloatingActionButton fabGuardarPerfil;
    private Calculadora calculadora;

    private FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();
    private DatabaseReference perfilRef = ConfigFirebase.getFirebaseDatabase().child("perfil");

    private ValueEventListener valueEventListenerPerfil;

    public DiarioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_diario, container, false);

        etFsiPerfil = view.findViewById(R.id.etFsiPerfil);
        etRhcPerfil = view.findViewById(R.id.etRhcPerfil);
        etGliAlvoPerfil = view.findViewById(R.id.etGliAlvoPerfil);
        calculadora = new Calculadora();
        fabGuardarPerfil = view.findViewById(R.id.fabGuardarPerfil);

        //recuperarDados();

        fabGuardarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarDados();
            }
        });


        return view;
    }

    public void guardarDados(){
        Double textoFSI = Double.parseDouble(etFsiPerfil.getText().toString());
        Double textoRHC= Double.parseDouble(etRhcPerfil.getText().toString());
        Double textoGlicemiaAlvo = Double.parseDouble(etGliAlvoPerfil.getText().toString());

        calculadora.setFsi(textoFSI);
        calculadora.setrHC(textoRHC);
        calculadora.setGlicemiaAlvo(textoGlicemiaAlvo);

        calculadora.guardar();
    }

    public void recuperarDados(){
        String emailUtilizador = autenticacao.getCurrentUser().getEmail();
        String idUtilizador = Base64Custom.codificarBase64( emailUtilizador );

        perfilRef = firebaseRef.child("perfil")
                               .child( idUtilizador );


    }
}