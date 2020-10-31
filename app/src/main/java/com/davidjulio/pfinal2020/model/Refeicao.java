package com.davidjulio.pfinal2020.model;

import com.davidjulio.pfinal2020.config.ConfigFirebase;
import com.davidjulio.pfinal2020.helper.Base64Custom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Refeicao {

    private String nome;
    private Double hidratosCarbono;
    private Integer calorias;
    private Double proteinas;
    private Double gordura;
    private String chave;

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

    public Double getHidratosCarbono() {
        return hidratosCarbono;
    }

    public void setHidratosCarbono(Double hidratosCarbono) {
        this.hidratosCarbono = hidratosCarbono;
    }

    public Integer getCalorias() {
        return calorias;
    }

    public void setCalorias(Integer calorias) {
        this.calorias = calorias;
    }

    public Double getProteinas() {
        return proteinas;
    }

    public void setProteinas(Double proteinas) {
        this.proteinas = proteinas;
    }

    public Double getGordura() {
        return gordura;
    }

    public void setGordura(Double gordura) {
        this.gordura = gordura;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }


    /*
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

 */
}
