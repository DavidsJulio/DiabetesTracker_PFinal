package com.davidjulio.pfinal2020.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfigFirebase {

    private static FirebaseAuth autenticacao; //vai ser o mesmo independente do numero
    private static DatabaseReference firebase;


    //devoleve a instancia
    public static FirebaseAuth getFirebaseAutenticacao(){

        if(autenticacao == null) { //caso ainda não tenha
            autenticacao = FirebaseAuth.getInstance(); //recupera a instancia;
        }
        return autenticacao;
    }

    public static DatabaseReference getFirebaseDatabase(){
        if(firebase == null){
            firebase = FirebaseDatabase.getInstance().getReference();
        }
        return firebase; //metodo que retorna automaticamente a instancia do firebase que nos permite guardar os dados na bd
    }
}
