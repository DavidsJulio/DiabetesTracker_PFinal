package com.davidjulio.pfinal2020.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.davidjulio.pfinal2020.R;
import com.davidjulio.pfinal2020.config.ConfigFirebase;
import com.davidjulio.pfinal2020.helper.Base64Custom;
import com.davidjulio.pfinal2020.model.Utilizador;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

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
                validarCampos();
            }
        });
    }

    public void validarCampos(){
        String textoNome = campoNome.getText().toString();
        String textoEmail = campoEmail.getText().toString();
        String textoPass = campoPass.getText().toString();

        if( !textoNome.isEmpty() ) {
            if( !textoEmail.isEmpty() ){
                if( !textoPass.isEmpty() ){
                    utilizador = new Utilizador();
                    utilizador.setNome(textoNome);
                    utilizador.setEmail(textoEmail);

                    utilizador.setPass(textoPass);
                    registarUtilizador();
                }else{
                    campoPass.setError("Insira a sua password!");
                    campoPass.requestFocus();
                }
            }else{
                campoEmail.setError("Insira o seu email!");
                campoEmail.requestFocus();
            }
        }else{
            campoNome.setError("Insira o seu nome!");
            campoNome.requestFocus();
        }

    }

    public void registarUtilizador(){

        autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                utilizador.getEmail(), utilizador.getPass()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if ( task.isSuccessful() ){
                    String idUtilizador = Base64Custom.codificarBase64(utilizador.getEmail()); //email codificar para base64 e ficamos com o id
                    utilizador.setIdUtilizador(idUtilizador); //guardamos o email
                    utilizador.guardarUtilizador();
                    //TODO: ter em atenção ao erro de criar conta
            /*        utilizador.guardar();*/
                    finish(); //fecha e envia para activity principal

                }else{
                    String exception = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        exception = "Insira uma password mais forte!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        exception = "Insira um email válido!";
                    }catch (FirebaseAuthUserCollisionException e){
                        exception = "Esse email já existe!";
                    }catch (Exception e){
                        exception = "Erro ao registar utilizador: " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(RegistoActivity.this, exception, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

