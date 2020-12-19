package com.davidjulio.pfinal2020.activity.ui.calculadora;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.davidjulio.pfinal2020.R;
import com.davidjulio.pfinal2020.activity.TelaPrincipalActivity;
import com.davidjulio.pfinal2020.activity.ui.refeicoes.AdicionarRefeicaoActivity;
import com.davidjulio.pfinal2020.config.ConfigFirebase;
import com.davidjulio.pfinal2020.helper.Base64Custom;
import com.davidjulio.pfinal2020.helper.DateUtil;
import com.davidjulio.pfinal2020.model.Calculadora;
import com.davidjulio.pfinal2020.model.Refeicao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CalculadoraFragment extends Fragment  {

    Spinner spinnerRefeicoesCalculadora;
    EditText valorGlicose, valorHidratos;
    Button btnCalcular, btnAvisar;
    ImageButton ibBluetooth, ibAdicionarRefeicao;
    TextView tvResultado;
    Calculadora calculadora;

    public static final String HC_CALCULADORA = "hcCalculadora";

    private FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();


    private List<String> listaRefeicoes = new ArrayList<>();


    private DatabaseReference calculadoraRef;
    private DatabaseReference refeicaoRef;
    private ValueEventListener valueEventListenerRefeicoes;
    private ValueEventListener valueEventListenerCalculadora;

    private String email;

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
        btnAvisar = view.findViewById(R.id.bAvisarCalculadora);
        spinnerRefeicoesCalculadora = view.findViewById(R.id.spinnerRefeicoesCalculadora);

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

        btnAvisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoResultado = tvResultado.getText().toString();
                if(textoResultado.equals("")){
                    btnCalcular.requestFocus();
                    Toast.makeText(getActivity(), "Antes de Enviar, precisa de Calcular!", Toast.LENGTH_LONG).show();
                    return;
                }else {
                    avisarFamilar();
                }
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

        valueEventListenerCalculadora = calculadoraRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    calculadora = dataSnapshot.getValue(Calculadora.class);
                    calculadora.setFsi(calculadora.getFsi());
                    calculadora.setrHC(calculadora.getrHC());
                    calculadora.setGlicemiaAlvo(calculadora.getGlicemiaAlvo());
                    email = calculadora.getEmailFamilar();
                }else{
                    alert();
                }
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
                return;
            }

        }catch (NumberFormatException e){
            valorGlicose.setError("Insira um valor para a Glicemia");
            valorGlicose.requestFocus();
            return;
        }
    }

    public void avisarFamilar(){

        String assunto = "Calculo da insulina";
        String dataAtual = DateUtil.dataAtual();
        String doses = tvResultado.getText().toString();
        String glicoseAtual = valorGlicose.getText().toString();
        String hidratosAtual = valorHidratos.getText().toString();

        StringBuilder sb = new StringBuilder();
        sb.append("Data/Hora: " + dataAtual);
        sb.append("\n\nGlicose Atual: "+glicoseAtual+ " mg/dL");
        sb.append("\nHidratos de Carbono: "+ hidratosAtual+" g");
        sb.append("\n\nDoses a administrar: "+doses);


        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, assunto);
        intent.putExtra(Intent.EXTRA_TEXT, sb.toString());

        intent.setType("text/plain");

        startActivity( Intent.createChooser( intent, "Avise o seu familiar!" ) );
    }

    public void alert(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setTitle("Dados em falta");
        dialog.setMessage("Insira os dados no seu perfil: \n" +
                          "\n- Fator de Sensibilidade à Insulina;\n" +
                          "- Rácio Insulina:Hidratos de Carbono;" +
                          "\n- Glicemia - Alvo;");
        dialog.setCancelable(false);
        dialog.setIcon(R.drawable.ic_baseline_person_24);

        dialog.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), TelaPrincipalActivity.class);
                getActivity().startActivity(intent);
            }
        });

        final AlertDialog alertDialog = dialog.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                TextView message = alertDialog.findViewById(android.R.id.message);
                if(message != null){
                    message.setTextSize(20);
                }

            }
        });
        alertDialog.show();
  /*      dialog.create();
        dialog.show();*/
    }

    public void dadosSpinner(){
        String emailUtilizador = autenticacao.getCurrentUser().getEmail();
        String idUtilizador = Base64Custom.codificarBase64( emailUtilizador );


        refeicaoRef = firebaseRef.child("refeicoes")
                .child( idUtilizador );


        valueEventListenerRefeicoes = refeicaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaRefeicoes.clear();
                listaRefeicoes.add("-- Selecione uma Refeição --");
                for(DataSnapshot dadosRefeicoes: dataSnapshot.getChildren()){
                    Refeicao refeicao = dadosRefeicoes.getValue(Refeicao.class);
                    //valorHidratos.setText(refeicao.getHidratosCarbono().toString());
                    listaRefeicoes.add( refeicao.getNome() );

                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_result, listaRefeicoes);
                arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spinnerRefeicoesCalculadora.setAdapter(arrayAdapter);

                spinnerRefeicoesCalculadora.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("Spinner", "Spinner: "+ spinnerRefeicoesCalculadora.getSelectedItem().toString());
                        String refeicaoSelecionada = spinnerRefeicoesCalculadora.getSelectedItem().toString();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarValoresCalculo();
        dadosSpinner();
    }

    @Override
    public void onStop() {
        super.onStop();
        calculadoraRef.removeEventListener(valueEventListenerCalculadora);
        refeicaoRef.removeEventListener(valueEventListenerRefeicoes);

    }
}