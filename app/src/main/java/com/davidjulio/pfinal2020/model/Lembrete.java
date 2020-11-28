package com.davidjulio.pfinal2020.model;

import com.davidjulio.pfinal2020.config.ConfigFirebase;
import com.davidjulio.pfinal2020.helper.Base64Custom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Lembrete implements Serializable {

    private String titulo;
    private String descricao;
    private String data;
    private String hora;
    private String idLembrete;

    public Lembrete(){
        DatabaseReference reference = ConfigFirebase.getFirebaseDatabase().child("lembretes");
        setIdLembrete( reference.push().getKey() );
    }

    public void guardarLembrete(){
        FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        String idUtilizador = Base64Custom.codificarBase64( autenticacao.getCurrentUser().getEmail() );

        DatabaseReference firebase = ConfigFirebase.getFirebaseDatabase();
        firebase.child("lembretes")
                .child(idUtilizador)
                .child(idLembrete)
                .setValue(this);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getIdLembrete() {
        return idLembrete;
    }

    public void setIdLembrete(String idLembrete) {
        this.idLembrete = idLembrete;
    }
}
