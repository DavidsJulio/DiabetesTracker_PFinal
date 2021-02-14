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
    private Double imc;

    private Integer fsi;
    private Double rHC;
    private Integer glicemiaAlvo;
    private String emailFamilar;

    public Utilizador() {
    }

    public void guardarUtilizador(){
        FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        String idUtilizador = Base64Custom.codificarBase64( autenticacao.getCurrentUser().getEmail() );
        DatabaseReference firebase = ConfigFirebase.getFirebaseDatabase();
        firebase.child("utilizadores")
                .child(idUtilizador)
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

    public Double getImc() {
        return imc;
    }

    public void setImc(Double imc) {
        this.imc = imc;
    }

    public Integer getFsi() {
        return fsi;
    }

    public void setFsi(Integer fsi) {
        this.fsi = fsi;
    }

    public Double getrHC() {
        return rHC;
    }

    public void setrHC(Double rHC) {
        this.rHC = rHC;
    }

    public Integer getGlicemiaAlvo() {
        return glicemiaAlvo;
    }

    public void setGlicemiaAlvo(Integer glicemiaAlvo) {
        this.glicemiaAlvo = glicemiaAlvo;
    }

    public String getEmailFamilar() {
        return emailFamilar;
    }

    public void setEmailFamilar(String emailFamilar) {
        this.emailFamilar = emailFamilar;
    }

    public int calculoInsulina(Integer glicemiaRefeicao, Double hcRefeicao){

        Double correcaoGlicemia = (double)( glicemiaRefeicao - glicemiaAlvo )/fsi;
        Double metabolizacaoHC = hcRefeicao / rHC;


        //TODO: ARREDONDAMENTOS CORRETAMENTE
        int dose = (int)(correcaoGlicemia + metabolizacaoHC);

        return  dose;
    }

    public String tabelaIMC(Double imc){
        String resultadoImc = "";
        if ( imc < 17) {
            resultadoImc = "Muito abaixo do peso";
        }else if( imc >= 17 && imc <= 18.49 ){
            resultadoImc = "Abaixo do peso";
        }else if( imc >= 18.50 && imc <= 24.99 ){
            resultadoImc = "Peso normal";
        }else if( imc >= 25 && imc <= 29.99 ){
            resultadoImc = "Acima do peso";
        }else if( imc >= 30 && imc <= 34.99){
            resultadoImc = "Obesidade I";
        }else if( imc >= 35 && imc <= 39.99){
            resultadoImc = "Obesidade II - Severa";
        }else if(imc > 40){
            resultadoImc = "Obesidade III - Mórbida";
        }

        return resultadoImc;
    }


}

