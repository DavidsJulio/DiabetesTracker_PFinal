package com.davidjulio.pfinal2020.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permissao {

    public static boolean validarPermissoes(String[] permissoes, Activity activity, int requestCode){

        if (Build.VERSION.SDK_INT >= 23){ //estas validaçoes apareceram depois da versao marshmallow (sdk23)
            //nas versoes anteriores não é preciso fazer estas validacoes

            List<String> lista = new ArrayList<>();

            //verificar se as permissoes já foram validadas
            for(String permissao : permissoes){ //percorre todas as permicoes
                //verifica se temos a permissão ou não
                Boolean granted = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                if (!granted){

                    lista.add(permissao); //adicionar a permissão que ainda não foi concedida
                }
            }

            if(lista.isEmpty()) return true; //se a lista está vazia não é necessário pedir a permissão

            String[]  novasPermissoes = new String[ lista.size() ];
            lista.toArray( novasPermissoes );

            //pedir as permissões
            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode);
        }

        return true;
    }
}
