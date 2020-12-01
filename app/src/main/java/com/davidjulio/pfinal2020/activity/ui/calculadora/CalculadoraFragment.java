package com.davidjulio.pfinal2020.activity.ui.calculadora;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.davidjulio.pfinal2020.R;
import com.davidjulio.pfinal2020.activity.ui.refeicoes.AdicionarRefeicaoActivity;
import com.davidjulio.pfinal2020.config.ConfigFirebase;
import com.davidjulio.pfinal2020.helper.Base64Custom;
import com.davidjulio.pfinal2020.model.Calculadora;
import com.davidjulio.pfinal2020.model.Refeicao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CalculadoraFragment extends Fragment  {

    Spinner spinnerRefeicoesCalculadora;
    EditText valorGlicose, valorHidratos;
    Button btnCalcular;
    ImageButton ibBluetooth, ibAdicionarRefeicao;
    TextView tvResultado;
    Calculadora calculadora;

    public static final String HC_CALCULADORA = "hcCalculadora";
    Bundle bundle;

    private FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();
    private DatabaseReference calculadoraRef;

    private List<Refeicao> listaRefeicoes = new ArrayList<>();

    public CalculadoraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calculadora, container, false);

        valorGlicose = view.findViewById( R.id.edValorGlicoseCalculadora );
        valorHidratos = view.findViewById( R.id.edValorHidratosCalculadora );
        btnCalcular = view.findViewById( R.id.bCalcularCalculadora );
        tvResultado = view.findViewById( R.id.tvResultadoCalculadora);
        ibAdicionarRefeicao = view.findViewById(R.id.ibAdicionarRefeicaoCalculadora);

        recuperarValoresCalculo();

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcular();
            }
        });

        ibAdicionarRefeicao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarRefeicao();
            }
        });

        return view;
    }

    public void adicionarRefeicao (){
        Intent intent = new Intent();
        intent.setClass(getActivity(), AdicionarRefeicaoActivity.class);

        /*String hcCalculadora = valorHidratos.getText().toString();
        intent.putExtra(HC_CALCULADORA, hcCalculadora);
*/

        getActivity().startActivity(intent);
    }

    public void recuperarValoresCalculo(){
        String emailUtilizador = autenticacao.getCurrentUser().getEmail();
        String idUtilizador = Base64Custom.codificarBase64( emailUtilizador );

        calculadoraRef = firebaseRef.child( "perfil" )
                .child( idUtilizador );

        calculadoraRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                calculadora = dataSnapshot.getValue( Calculadora.class );
                calculadora.setFsi( calculadora.getFsi() );
                calculadora.setrHC( calculadora.getrHC() );
                calculadora.setGlicemiaAlvo( calculadora.getGlicemiaAlvo() );
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void calcular(){

        Double glicoseHora, hidratosHora;
        try {
            glicoseHora = Double.parseDouble(valorGlicose.getText().toString());
            try {
                hidratosHora = Double.parseDouble(valorHidratos.getText().toString());
                int resultado = calculadora.calculoInsulina(glicoseHora, hidratosHora);
                tvResultado.setText(resultado + " Doses");
            }catch (NumberFormatException e){
                valorHidratos.setError("Insira um valor para os Hidratos");
                valorHidratos.requestFocus();
            }

        }catch (NumberFormatException e){
            valorGlicose.setError("Insira um valor para a Glicemia");
            valorGlicose.requestFocus();
        }
    }

}