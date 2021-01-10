package com.davidjulio.pfinal2020.model;

import android.net.Uri;
import android.util.Log;

import com.davidjulio.pfinal2020.config.ConfigFirebase;
import com.davidjulio.pfinal2020.helper.Base64Custom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import androidx.annotation.NonNull;

public class Utilizador {

    private String nome;
    private String email;
    private String pass;
    private String idUtilizador;

    private String dataNascimento;
    private String sexo;
    private Integer altura;
    private Double peso;

    public Utilizador() {
    }

    public void guardarUtilizador(){
        DatabaseReference firebase = ConfigFirebase.getFirebaseDatabase();
        firebase.child("utilizadores")
                .child(this.idUtilizador)
                .setValue(this);
    }

    public void guardar(){
        FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        String idUtilizador = Base64Custom.codificarBase64( autenticacao.getCurrentUser().getEmail() );

        DatabaseReference firebase = ConfigFirebase.getFirebaseDatabase();
        firebase.child("utilizadores")
                .child(idUtilizador)
                .setValue(this);
    }

    @Exclude
    public String getIdUtilizador() {
        return idUtilizador;
    }

    public void setIdUtilizador(String idUtilizador) {
        this.idUtilizador = idUtilizador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Integer getAltura() {
        return altura;
    }

    public void setAltura(Integer altura) {
        this.altura = altura;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public static FirebaseUser getUtilizador(){
        FirebaseAuth utilizador = ConfigFirebase.getFirebaseAutenticacao();
        return utilizador.getCurrentUser();
    }

    public static boolean atualizarFoto(Uri url){

        try {
            FirebaseUser utilizador = getUtilizador();
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(url)
                    .build();

            utilizador.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar foto de perfil.");
                    }
                }
            });

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
}

