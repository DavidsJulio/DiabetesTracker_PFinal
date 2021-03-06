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
import com.davidjulio.pfinal2020.model.Medicao;
import com.davidjulio.pfinal2020.model.Refeicao;
import com.davidjulio.pfinal2020.model.Utilizador;
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
    Utilizador utilizador;

    private FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();

    private List<String> listaRefeicoes = new ArrayList<>();

    private DatabaseReference utilizadorRef;
    private DatabaseReference refeicaoRef;
    private ValueEventListener valueEventListenerRefeicoes;
    private ValueEventListener valueEventListenerUtilizador;

    private String email;

    public CalculadoraFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
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
                    Toast.makeText(getActivity(), R.string.msg_error_send_calculo, Toast.LENGTH_LONG).show();
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
        String idUtilizador = ConfigFirebase.getCurrentUser();

        utilizadorRef = firebaseRef.child( "utilizadores" )
                                    .child( idUtilizador );

        valueEventListenerUtilizador = utilizadorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    utilizador = dataSnapshot.getValue(Utilizador.class);
                    utilizador.setFsi(utilizador.getFsi());
                    utilizador.setrHC(utilizador.getrHC());
                    utilizador.setGlicemiaAlvo(utilizador.getGlicemiaAlvo());
                    email = utilizador.getEmailFamilar();
                    if(utilizador.getFsi() == null || utilizador.getrHC() == null || utilizador.getGlicemiaAlvo() == null){
                        alert();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void calcular(){
        Double hidratosHora;
        int glicoseHora, resultado;
        try {
            glicoseHora = Integer.parseInt(valorGlicose.getText().toString());

            try {
                hidratosHora = Double.parseDouble(valorHidratos.getText().toString());
                resultado = utilizador.calculoInsulina(glicoseHora, hidratosHora);
                tvResultado.setText(resultado + " Doses");
            }catch (NumberFormatException e){
                valorHidratos.setError(getString(R.string.insert_hc));
                valorHidratos.requestFocus();
                return;
            }

        }catch (NumberFormatException e){
            valorGlicose.setError(getString(R.string.insert_g));
            valorGlicose.requestFocus();
            return;
        }
        guardarValoresGlicose(glicoseHora, hidratosHora, resultado);
    }

    public void avisarFamilar(){
        String assunto = getString(R.string.calculoInsulina);
        String dataAtual = DateUtil.dataAtual();
        String doses = tvResultado.getText().toString();
        String glicoseAtual = valorGlicose.getText().toString();
        String hidratosAtual = valorHidratos.getText().toString();

        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.data_hora) + dataAtual);
        sb.append(getString(R.string.glicoseAtual)+glicoseAtual+ " mg/dL");
        sb.append(getString(R.string.hidratosCarbono)+ hidratosAtual+" g");
        sb.append(getString(R.string.resultado)+doses);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, assunto);
        intent.putExtra(Intent.EXTRA_TEXT, sb.toString());

        intent.setType("text/plain");

        startActivity( Intent.createChooser( intent, getString(R.string.warn_family) ) );
    }

    public void alert(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setTitle(R.string.miss_data);
        dialog.setMessage(getString(R.string.insert_profile_data) +
                          getString(R.string.fsi) +
                          getString(R.string.insulin_carbs) +
                          getString(R.string.target_glucose));
        dialog.setCancelable(false);
        dialog.setIcon(R.drawable.ic_baseline_person_24);

        dialog.setPositiveButton(R.string.continue_string, new DialogInterface.OnClickListener() {
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

    }

    public void dadosSpinner(){
        String idUtilizador = ConfigFirebase.getCurrentUser();
        refeicaoRef = firebaseRef.child("refeicoes")
                                .child( idUtilizador );

        valueEventListenerRefeicoes = refeicaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaRefeicoes.clear();
                listaRefeicoes.add(getString(R.string.select_meal));
                for(DataSnapshot dadosRefeicoes: dataSnapshot.getChildren()){
                    Refeicao refeicao = dadosRefeicoes.getValue(Refeicao.class);
                    listaRefeicoes.add( refeicao.getNome() + getString(R.string.divideCarbs) + refeicao.getHidratosCarbono());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_result, listaRefeicoes);
                arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spinnerRefeicoesCalculadora.setAdapter(arrayAdapter);

                spinnerRefeicoesCalculadora.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String refeicaoSelecionada = spinnerRefeicoesCalculadora.getSelectedItem().toString();

                        if(refeicaoSelecionada.equals(getString(R.string.select_meal))){
                            valorHidratos.setText("");
                        }else{
                            String hcSplit[] = refeicaoSelecionada.split(getString(R.string.divideCarbs));
                            String hc = hcSplit[1];
                            valorHidratos.setText(hc);
                        }
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

    public void guardarValoresGlicose(Integer glicoseHora, Double hc, Integer insulina){
        Medicao medicao = new Medicao();

        String dataAtual = DateUtil.dataAtual();
        String dataAnoFirst = DateUtil.dataAtualAno();
        Integer insulinaDouble = insulina;

        medicao.setMedicaoInsulina(insulinaDouble);
        medicao.setMedicaoGlicose(glicoseHora);
        medicao.setMedicaoHC(hc);

        medicao.setDataHoraAux(dataAnoFirst);
        medicao.setDataHora(dataAtual);
        medicao.setEditavel("N");
        medicao.guardar();
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
        utilizadorRef.removeEventListener(valueEventListenerUtilizador);
        refeicaoRef.removeEventListener(valueEventListenerRefeicoes);
    }
}