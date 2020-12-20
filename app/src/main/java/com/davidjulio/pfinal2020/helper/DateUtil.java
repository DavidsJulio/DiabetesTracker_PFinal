package com.davidjulio.pfinal2020.helper;


import java.text.SimpleDateFormat;

public class DateUtil {

    public static String dataAtual(){
        long data = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dataString = sdf.format(data);
        return dataString;
    }

    public static String dataAtualAno(){
        long data = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String dataString = sdf.format(data);
        return dataString;
    }


}
