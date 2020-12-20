package com.davidjulio.pfinal2020.activity.ui.perfil;

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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class PerfilFragment extends Fragment {

    private EditText etFsiPerfil, etRhcPerfil, etGliAlvoPerfil;
    private TextInputEditText etEmailPerfil;
    private FloatingActionButton fabGuardarPerfil;
    private Calculadora calculadora;

    private FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();
    private DatabaseReference perfilRef = ConfigFirebase.getFirebaseDatabase().child("perfil");

    private ValueEventListener valueEventListenerPerfil;

    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        etFsiPerfil = view.findViewById(R.id.etFsiPerfil);
        etRhcPerfil = view.findViewById(R.id.etRhcPerfil);
        etGliAlvoPerfil = view.findViewById(R.id.etGliAlvoPerfil);
        etEmailPerfil = view.findViewById(R.id.tEmailPerfil);
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
        String textoEmail = etEmailPerfil.getText().toString();

        try{
            Integer textoFSI = Integer.parseInt(etFsiPerfil.getText().toString());
            calculadora.setFsi(textoFSI);
        } catch (NumberFormatException e){
            etFsiPerfil.setError("Insira o seu Fator de Sensibidade à Insulina!");
            etFsiPerfil.requestFocus();
            return;
        }

        try{
            Double textoRHC= Double.parseDouble(etRhcPerfil.getText().toString());
            calculadora.setrHC(textoRHC);
        } catch (NumberFormatException e){
            etRhcPerfil.setError("Insira o seu Rácio para Hidratos de Carbono!");
            etRhcPerfil.requestFocus();
            return;
        }

        try {
            Integer textoGlicemiaAlvo = Integer.parseInt(etGliAlvoPerfil.getText().toString());
            calculadora.setGlicemiaAlvo(textoGlicemiaAlvo);
        }catch (NumberFormatException e){
            etGliAlvoPerfil.setError("Insira a sua Glicemia Alvo!");
            etGliAlvoPerfil.requestFocus();
            return;
        }

        calculadora.setEmailFamilar(textoEmail);
        calculadora.guardar();
    }

    public void recuperarDados(){
/*        String emailUtilizador = autenticacao.getCurrentUser().getEmail();
        String idUtilizador = Base64Custom.codificarBase64( emailUtilizador );*/
        String idUtilizador = ConfigFirebase.getCurrentUser();
        perfilRef = firebaseRef.child("perfil")
                               .child( idUtilizador );

        valueEventListenerPerfil = perfilRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    calculadora = (Calculadora) dataSnapshot.getValue(Calculadora.class);

                    String fsi = String.valueOf(calculadora.getFsi());
                    etFsiPerfil.setText(fsi);

                    String rHC = String.valueOf(calculadora.getrHC());
                    etRhcPerfil.setText(rHC);

                    String glimeciaAlvo = String.valueOf(calculadora.getGlicemiaAlvo());
                    etGliAlvoPerfil.setText(glimeciaAlvo);

                    etEmailPerfil.setText(calculadora.getEmailFamilar());
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
        recuperarDados();
    }

    @Override
    public void onStop() {
        super.onStop();
        perfilRef.removeEventListener(valueEventListenerPerfil);
    }
}