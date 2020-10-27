package com.davidjulio.pfinal2020.model;

import com.davidjulio.pfinal2020.config.ConfigFirebase;
import com.davidjulio.pfinal2020.helper.Base64Custom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Refeicao {

    private String nome;
    private double hidratosCarbono;
    private int calorias;
    private double proteinas;
    private double gordura;

    public Refeicao() {
    }

    public void guardar(){
        FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        String idUtilizador = Base64Custom.codificarBase64( autenticacao.getCurrentUser().getEmail() );

        DatabaseReference firebase = ConfigFirebase.getFirebaseDatabase();
        firebase.child("refeicoes")
                .child(idUtilizador)
                .push()
                .setValue(this);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getHidratosCarbono() {
        return hidratosCarbono;
    }

    public void setHidratosCarbono(double hidratosCarbono) {
        this.hidratosCarbono = hidratosCarbono;
    }

    public int getCalorias() {
        return calorias;
    }

    public void setCalorias(int calorias) {
        this.calorias = calorias;
    }

    public double getProteinas() {
        return proteinas;
    }

    public void setProteinas(double proteinas) {
        this.proteinas = proteinas;
    }

    public double getGordura() {
        return gordura;
    }

    public void setGordura(double gordura) {
        this.gordura = gordura;
    }
}
