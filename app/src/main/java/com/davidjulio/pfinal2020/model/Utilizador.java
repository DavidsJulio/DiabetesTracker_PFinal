package com.davidjulio.pfinal2020.model;

import com.davidjulio.pfinal2020.config.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class Utilizador {

    private String nome;
    private String email;
    private String pass;
    private String idUtilizador;

    public Utilizador() {
    }

    public void guardarUtilizador(){
        DatabaseReference firebase = ConfigFirebase.getFirebaseDatabase();
        firebase.child("utilizadores")
                .child(this.idUtilizador)
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

}
