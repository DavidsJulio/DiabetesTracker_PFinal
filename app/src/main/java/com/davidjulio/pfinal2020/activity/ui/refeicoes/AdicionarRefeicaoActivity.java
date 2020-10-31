package com.davidjulio.pfinal2020.activity.ui.refeicoes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.davidjulio.pfinal2020.R;
import com.davidjulio.pfinal2020.model.Refeicao;
import com.google.android.material.textfield.TextInputEditText;

public class AdicionarRefeicaoActivity extends AppCompatActivity {

    private TextInputEditText campoNome, campoHidratos, campoCalorias, campoGordura, campoProteinas;

    private Refeicao refeicao;
    private Double proteina ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_refeicao);

        campoNome = findViewById(R.id.editNomeR);
        campoHidratos = findViewById(R.id.editHidratosR);
        campoCalorias = findViewById(R.id.editCaloriasR);
        campoGordura = findViewById(R.id.editGorduraR);
        campoProteinas = findViewById(R.id.editProteinaR);

        campoCalorias.setText("0");
        campoGordura.setText("0.0");
        campoProteinas.setText("0.0");

    }

    public void guardarRefeicao(View view){
       if ( validarCamposRefeicao() ){
            refeicao = new Refeicao();

            refeicao.setNome(campoNome.getText().toString());

            Double hidratos = Double.parseDouble(campoHidratos.getText().toString());
            refeicao.setHidratosCarbono(hidratos);

            String textoCalorias = campoCalorias.getText().toString();
            Integer calorias = Integer.parseInt(campoCalorias.getText().toString());

            //TODO: Corrigir quando vai empty.....
            if(textoCalorias.length()>0) {
                refeicao.setCalorias(calorias);
            }else{
                refeicao.setCalorias(0);
            }

            Double gordura = Double.parseDouble(campoGordura.getText().toString());
            String textoGordura = campoHidratos.getText().toString();
            if(!textoGordura.isEmpty()) {
                refeicao.setGordura(gordura);
            }else{
                refeicao.setGordura(0.0);
            }

            proteina = Double.parseDouble(campoProteinas.getText().toString());
            String textoProteina = campoHidratos.getText().toString();
            if(!textoProteina.isEmpty()) {
                refeicao.setProteinas(proteina);
            }else if(proteina == null){
                refeicao.setProteinas(0.0);
            }
            refeicao.guardar();
            finish();

       }
    }

    public boolean validarCamposRefeicao() {

        String textoNome = campoNome.getText().toString();
        String textoHidratos = campoHidratos.getText().toString();


        if (!textoNome.isEmpty()) {
            if (!textoHidratos.isEmpty()) {
                return true;
            } else {
                campoHidratos.setError("Preencha os Hidratos de Carbono");
                campoHidratos.requestFocus();
                return false;
            }
        } else {
            campoNome.setError("Preencha o nome para a sua Refeição!");
            campoNome.requestFocus();
            return false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_voltar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_undo:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}