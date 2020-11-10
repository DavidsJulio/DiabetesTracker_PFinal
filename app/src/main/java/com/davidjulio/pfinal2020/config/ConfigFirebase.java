package com.davidjulio.pfinal2020.config;

import com.davidjulio.pfinal2020.helper.Base64Custom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfigFirebase {

    private static FirebaseAuth autenticacao; //vai ser o mesmo independente do numero
    private static DatabaseReference firebase;
    private static StorageReference storage;


    //devolve a instancia
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

    //TODO: FIREBASE STORAGE
    public static StorageReference getFirebaseStorage(){
        if(storage == null) { //caso ainda não tenha
            storage = FirebaseStorage.getInstance().getReference();
        }
        return storage;
    }


}
