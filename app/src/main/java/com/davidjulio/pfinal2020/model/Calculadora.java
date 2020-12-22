package com.davidjulio.pfinal2020.model;

import com.davidjulio.pfinal2020.config.ConfigFirebase;
import com.davidjulio.pfinal2020.helper.Base64Custom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Calculadora {

    private Integer fsi;
    private Double rHC;
    private Integer glicemiaAlvo;
    private String emailFamilar;

    public Calculadora() {
    }

    public void guardar(){
        FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        String idUtilizador = Base64Custom.codificarBase64( autenticacao.getCurrentUser().getEmail() );

        DatabaseReference firebase = ConfigFirebase.getFirebaseDatabase();
        firebase.child( "perfil" )
                .child( idUtilizador )
                .setValue( this );
    }

    public String getEmailFamilar() {
        return emailFamilar;
    }

    public void setEmailFamilar(String emailFamilar) {
        this.emailFamilar = emailFamilar;
    }


    public Double getrHC() {
        return rHC;
    }

    public void setrHC(Double rHC) {
        this.rHC = rHC;
    }

    public Integer getFsi() {
        return fsi;
    }

    public void setFsi(Integer fsi) {
        this.fsi = fsi;
    }

    public Integer getGlicemiaAlvo() {
        return glicemiaAlvo;
    }

    public void setGlicemiaAlvo(Integer glicemiaAlvo) {
        this.glicemiaAlvo = glicemiaAlvo;
    }

    public int calculoInsulina(Integer glicemiaRefeicao, Double hcRefeicao){

        Double correcaoGlicemia = (double)( glicemiaRefeicao - glicemiaAlvo )/fsi;
        Double metabolizacaoHC = hcRefeicao / rHC;


        //TODO: ARREDONDAMENTOS CORRETAMENTE
        int dose = (int)(correcaoGlicemia + metabolizacaoHC);

        return  dose;
    }
}
