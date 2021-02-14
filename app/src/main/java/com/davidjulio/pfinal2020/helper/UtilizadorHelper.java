package com.davidjulio.pfinal2020.helper;

import android.net.Uri;
import android.util.Log;

import com.davidjulio.pfinal2020.config.ConfigFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import androidx.annotation.NonNull;

public class UtilizadorHelper {

    public static FirebaseUser getUtilizador(){
        FirebaseAuth utilizador = ConfigFirebase.getFirebaseAutenticacao();
        return utilizador.getCurrentUser();
    }

    public static boolean atualizarFoto(Uri url){

        try {
            FirebaseUser utilizador = getUtilizador();
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(url)
                    .build();

            utilizador.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar foto de perfil.");
                    }
                }
            });

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
}
