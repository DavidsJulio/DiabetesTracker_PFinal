package com.davidjulio.pfinal2020.model;

import com.davidjulio.pfinal2020.config.ConfigFirebase;
import com.davidjulio.pfinal2020.helper.Base64Custom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Refeicao implements Serializable {

    private String nome;
    private Double hidratosCarbono;
    private Integer calorias;
    private Double proteinas;
    private Double gordura;
    private String idRefeicao;
    private String urlFoto;

    public Refeicao() {
        DatabaseReference reference = ConfigFirebase.getFirebaseDatabase().child("refeicoes");
        setIdRefeicao( reference.push().getKey() );
    }

    public void guardar(){
        FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        String idUtilizador = Base64Custom.codificarBase64( autenticacao.getCurrentUser().getEmail() );

        DatabaseReference firebase = ConfigFirebase.getFirebaseDatabase();
        firebase.child("refeicoes")
                .child(idUtilizador)
                .child(idRefeicao)
                .setValue(this);
    }

    //remove da database
    public void eliminar(){
        FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        String idUtilizador = Base64Custom.codificarBase64( autenticacao.getCurrentUser().getEmail() );

        DatabaseReference firebase = ConfigFirebase.getFirebaseDatabase();
        firebase.child("refeicoes")
                .child(idUtilizador)
                .child( getIdRefeicao() )
                .removeValue();
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getIdRefeicao() {
        return idRefeicao;
    }

    public void setIdRefeicao(String idRefeicao) {
        this.idRefeicao = idRefeicao;
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


}
