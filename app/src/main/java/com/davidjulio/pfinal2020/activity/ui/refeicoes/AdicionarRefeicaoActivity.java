package com.davidjulio.pfinal2020.activity.ui.refeicoes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_refeicao);

        campoNome = findViewById(R.id.editNomeR);
        campoHidratos = findViewById(R.id.editHidratosR);
        campoCalorias = findViewById(R.id.editCaloriasR);
        campoGordura = findViewById(R.id.editGorduraR);
        campoProteinas = findViewById(R.id.editProteinaR);


    }

    public void guardarRefeicao(View view){
       if ( validarCamposRefeicao() ){
            refeicao = new Refeicao();

            refeicao.setNome(campoNome.getText().toString());

            Double hidratos = Double.parseDouble(campoHidratos.getText().toString());
            refeicao.setHidratosCarbono(hidratos);

            Integer calorias = Integer.parseInt(campoCalorias.getText().toString());
            refeicao.setCalorias(calorias);

            Double gordura = Double.parseDouble(campoGordura.getText().toString());
            refeicao.setGordura(gordura);

            Double proteina = Double.parseDouble(campoProteinas.getText().toString());
            refeicao.setProteinas(proteina);

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


}