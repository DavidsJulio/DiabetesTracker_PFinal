package com.davidjulio.pfinal2020.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.davidjulio.pfinal2020.R;
import com.davidjulio.pfinal2020.config.ConfigFirebase;
import com.davidjulio.pfinal2020.model.Utilizador;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistoActivity extends AppCompatActivity {

    private TextInputEditText campoNome, campoEmail, campoPass;
    private Button btnRegistar;

    private FirebaseAuth autenticacao;

    private Utilizador utilizador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registo);

        campoNome = findViewById(R.id.editName);
        campoEmail = findViewById(R.id.editEmail);
        campoPass = findViewById(R.id.editPass);
        btnRegistar = findViewById(R.id.btnRegistar);

        btnRegistar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //funcao validar campos, nesta funcao é chamada a registar utilizador
                validarCampos();
            }
        });
    }

    public void validarCampos(){
        String textoNome = campoNome.getText().toString();
        String textEmail = campoEmail.getText().toString();
        String textoPass = campoPass.getText().toString();

        if(textoNome.isEmpty()){
            campoNome.setError("Insira o seu nome!");
            campoNome.requestFocus();
        }else if(textEmail.isEmpty()){
            campoEmail.setError("Insira o seu email!");
            campoEmail.requestFocus();
        }else if(textoPass.isEmpty()){
            campoPass.setError("Insira a sua password!");
            campoPass.requestFocus();
        }

        utilizador = new Utilizador();
        utilizador.setNome(textoNome);
        utilizador.setEmail(textEmail);
        utilizador.setPass(textoPass);
        registarUtilizador();

        //Alternativa, escolher 1 ou 2!
        /*
        if (!textoNome.isEmpty()){
            if (!textEmail.isEmpty()){
                if (!textoPass.isEmpty()){

                }else{
                    campoNome.setError("Insira a sua password");
                    campoNome.requestFocus();
                    //Toast.makeText(RegistoActivity.this, "Insira a sua password!", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(RegistoActivity.this, "Insira o seu email!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(RegistoActivity.this, "Insira o seu nome!", Toast.LENGTH_SHORT).show();
        }

         */
    }

    public void registarUtilizador(){

        autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                utilizador.getEmail(), utilizador.getPass()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if ( task.isSuccessful() ){
                    Toast.makeText(RegistoActivity.this, "Sucesso ao Registar", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RegistoActivity.this, "Erro ao Registar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}

