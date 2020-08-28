package com.davidjulio.pfinal2020.config;

import com.google.firebase.auth.FirebaseAuth;

public class ConfigFirebase {

    private static FirebaseAuth autenticacao; //vai ser o mesmo independente do numero

    //devoleve a instancia
    public static FirebaseAuth getFirebaseAutenticacao(){

        if(autenticacao == null) { //caso ainda não tenha
            autenticacao = FirebaseAuth.getInstance(); //recupera a instancia;
        }
        return autenticacao;
    }
}
