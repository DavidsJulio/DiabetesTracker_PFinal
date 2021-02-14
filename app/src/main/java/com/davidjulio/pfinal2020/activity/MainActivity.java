package com.davidjulio.pfinal2020.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.davidjulio.pfinal2020.R;
import com.davidjulio.pfinal2020.config.ConfigFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class MainActivity extends IntroActivity { //mudar de AppCompatActivity para extender IntroActivity

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide(new FragmentSlide.Builder()
                .background(R.color.colorWhite)
                .fragment(R.layout.intro_1)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.colorWhite)
                .fragment(R.layout.intro_2)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.colorWhite)
                .fragment(R.layout.intro_3)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.colorWhite)
                .fragment(R.layout.intro_registo)
                .canGoForward(false)
                .build());
    }

    @Override
    protected void onStart() {
        super.onStart();
        verificarUtilizadorLoggado();
    }

    public void btnRegisto (View view){
        startActivity(new Intent(this, RegistoActivity.class));
    }

    public void btnEntrar (View view){
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void verificarUtilizadorLoggado(){
        autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        if( autenticacao.getCurrentUser() != null){
            abrirActivityPrincipal();
            finish();
        }
    }

    public void abrirActivityPrincipal(){
       startActivity( new Intent( this, TelaPrincipalActivity.class));
    }
}