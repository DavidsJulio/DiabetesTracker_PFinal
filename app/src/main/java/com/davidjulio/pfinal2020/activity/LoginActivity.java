package com.davidjulio.pfinal2020.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.davidjulio.pfinal2020.R;
import com.davidjulio.pfinal2020.config.ConfigFirebase;
import com.davidjulio.pfinal2020.model.Utilizador;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText campoEmail, campoPass;
    private Button btnLoginEntrar;

    private Utilizador utilizador;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        campoEmail = findViewById(R.id.editLoginEmail);
        campoPass = findViewById(R.id.editLoginPass);
        btnLoginEntrar = findViewById(R.id.btnLoginEntrar);

        btnLoginEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoEmail = campoEmail.getText().toString();
                String textoPass = campoPass.getText().toString();

                if( !textoEmail.isEmpty() ){
                    if( !textoPass.isEmpty() ){
                        utilizador = new Utilizador();
                        utilizador.setEmail( textoEmail );
                        utilizador.setPass( textoPass );
                        validarLogin();
                        //autenticacao.signOut();
                    }else{
                        campoPass.setError("Insira a sua password!");
                        campoPass.requestFocus();
                    }
                }else {
                    campoEmail.setError("Insira o seu email!");
                    campoEmail.requestFocus();
                }
            }
        });
    }

    public void validarLogin(){
        autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                utilizador.getEmail(),
                utilizador.getPass()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if( task.isSuccessful() ){
                    abrirActivityPrincipal();
                }else {
                    String exception = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e) {
                        exception = "Utilizador não está registado!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        exception = "Email e password não correspondem!";
                    }catch (Exception e){
                        exception = "Erro ao registar utilizador: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(LoginActivity.this, exception, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void abrirActivityPrincipal(){
        startActivity( new Intent( this, TelaPrincipalActivity.class));
        finish(); //fecha a activity de login
    }

    public void btnLRegistar (View view){
        startActivity(new Intent(this, RegistoActivity.class));
    }
}