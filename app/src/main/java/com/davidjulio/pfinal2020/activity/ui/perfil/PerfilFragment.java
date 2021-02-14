package com.davidjulio.pfinal2020.activity.ui.perfil;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.davidjulio.pfinal2020.R;
import com.davidjulio.pfinal2020.config.ConfigFirebase;
import com.davidjulio.pfinal2020.model.Utilizador;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class PerfilFragment extends Fragment {

    private EditText etFsiPerfil, etRhcPerfil, etGliAlvoPerfil;
    private TextInputEditText etEmailPerfil;
    private FloatingActionButton fabGuardarPerfil;
    private TextView tvIMCResultado;

    private DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();
    private DatabaseReference utilizadorRef = ConfigFirebase.getFirebaseDatabase().child("utilizadores");

    private ValueEventListener valueEventListenerPerfil;
    private Utilizador utilizador;

    public PerfilFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        etFsiPerfil = view.findViewById(R.id.etFsiPerfil);
        etRhcPerfil = view.findViewById(R.id.etRhcPerfil);
        etGliAlvoPerfil = view.findViewById(R.id.etGliAlvoPerfil);
        etEmailPerfil = view.findViewById(R.id.tEmailPerfil);

        utilizador = new Utilizador();
        fabGuardarPerfil = view.findViewById(R.id.fabGuardarPerfil);
        tvIMCResultado = view.findViewById(R.id.tvPerfilIMCResultado);

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
            utilizador.setFsi(textoFSI);
        } catch (NumberFormatException e){
            etFsiPerfil.setError("Insira o seu Fator de Sensibidade à Insulina!");
            etFsiPerfil.requestFocus();
            return;
        }

        try{
            Double textoRHC= Double.parseDouble(etRhcPerfil.getText().toString());
            utilizador.setrHC(textoRHC);
        } catch (NumberFormatException e){
            etRhcPerfil.setError("Insira o seu Rácio para Hidratos de Carbono!");
            etRhcPerfil.requestFocus();
            return;
        }

        try {
            Integer textoGlicemiaAlvo = Integer.parseInt(etGliAlvoPerfil.getText().toString());
            utilizador.setGlicemiaAlvo(textoGlicemiaAlvo);
        }catch (NumberFormatException e){
            etGliAlvoPerfil.setError("Insira a sua Glicemia Alvo!");
            etGliAlvoPerfil.requestFocus();
            return;
        }

        utilizador.setEmailFamilar(textoEmail);
        utilizador.guardar();
    }

    public void recuperarDados(){
        String idUtilizador = ConfigFirebase.getCurrentUser();
        utilizadorRef = firebaseRef.child("utilizadores")
                                   .child( idUtilizador );

        valueEventListenerPerfil = utilizadorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    utilizador = (Utilizador) dataSnapshot.getValue(Utilizador.class);

                    String fsi = String.valueOf(utilizador.getFsi());
                    if(fsi.equals("null")){
                        etFsiPerfil.setText("");
                    }else {
                        etFsiPerfil.setText(fsi);
                    }
                    String rHC = String.valueOf(utilizador.getrHC());
                    if(rHC.equals("null")){
                        etRhcPerfil.setText("");
                    }else {
                        etRhcPerfil.setText(rHC);
                    }
                    String glimeciaAlvo = String.valueOf(utilizador.getGlicemiaAlvo());
                    if(glimeciaAlvo.equals("null")){
                        etGliAlvoPerfil.setText("");
                    }else {
                        etGliAlvoPerfil.setText(glimeciaAlvo);
                    }

                    etEmailPerfil.setText(utilizador.getEmailFamilar());

                    if (utilizador.getImc() != null) {
                        Double imcRound = Math.round(utilizador.getImc() * 100.0) / 100.0;

                        String resultadoImc = utilizador.tabelaIMC(utilizador.getImc());

                        tvIMCResultado.setText(resultadoImc + ": " + imcRound);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*imcRef = firebaseRef.child("utilizadores")
                            .child(idUtilizador);

        valueEventListenerIMC = imcRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    utilizadorTeste = (Utilizador) dataSnapshot.getValue(Utilizador.class);

                    Double imcRound = Math.round(utilizador.getImc() * 100.0)/100.0;

                    String resultadoImc = calculadora.tabelaIMC(utilizador.getImc());

                    tvIMCResultado.setText(resultadoImc + ": " + imcRound);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarDados();
    }

    @Override
    public void onStop() {
        super.onStop();
        utilizadorRef.removeEventListener(valueEventListenerPerfil);
     /*   imcRef.removeEventListener(valueEventListenerIMC);*/
    }
}