package com.davidjulio.pfinal2020.activity.ui.lembretes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.davidjulio.pfinal2020.R;
import com.davidjulio.pfinal2020.model.Lembrete;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AdicionarLembreteActivity extends AppCompatActivity {

    TextInputEditText editTitulo, editDescricao;
    Button btn_data, btn_hora;
    FloatingActionButton fab_add;

    Calendar calendar = Calendar.getInstance();
    String timeToNotify;

    private Lembrete lembrete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_lembrete);

        lembrete = new Lembrete();

        editTitulo = findViewById(R.id.editTitulo);
        editDescricao = findViewById(R.id.editDescricao);
        btn_data = findViewById(R.id.btn_data);
        btn_hora = findViewById(R.id.btn_hora);
        fab_add = findViewById(R.id.fab_add);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String date = sdf.format(calendar.getTime());
        btn_data.setText(date);

        String hora = "--:--";
        btn_hora.setText(hora);
    }

    public void escolherData(View view) {
        selectDate();
    }

    public void escolherHora(View view) {
        selectTime();
    }

    public void guardarLembrete(View view) {
        if(validarCamposLembretes()){
            String titulo = editTitulo.getText().toString().trim();
            String descricao = editDescricao.getText().toString().trim();
            String data = btn_data.getText().toString().trim();
            String hora = btn_hora.getText().toString().trim();

            lembrete.setTitulo(titulo);
            lembrete.setDescricao(descricao);
            lembrete.setData(data);
            lembrete.setHora(hora);
            lembrete.guardarLembrete();


            setAlarm(titulo, descricao, data, hora);
        }
    }

    private void selectTime(){
        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        boolean format = DateFormat.is24HourFormat(getApplication());

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute1) {
                if(minute1 <10){
                    timeToNotify = hourOfDay + ":" + "0"+ minute1;
                }else {
                    timeToNotify = hourOfDay + ":" + minute1;
                }
                btn_hora.setText(timeToNotify);
                //btn_hora.setText(formatTime(hourOfDay, minute1));
            }
        },hour,minute,format);
        timePickerDialog.show();
        //TODO: PRECISO ACERTAR A HORA
    }

    private void selectDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                btn_data.setText(dayOfMonth + "/" + (month+1) + "/" + year);
            }
        },year,month,day);
        datePickerDialog.show();
    }

    public boolean validarCamposLembretes(){
        String textoTitulo = editTitulo.getText().toString().trim();
        String textoDescricao = editDescricao.getText().toString().trim();
        String textoHora = btn_hora.getText().toString().trim();

        if(!textoTitulo.isEmpty()){
            if (!textoDescricao.isEmpty()){
                if(!textoHora.equals("--:--")){
                    return true;
                }else{
                    btn_hora.setError("Insira uma hora");
                    Toast.makeText(this, "Insira uma Hora!", Toast.LENGTH_LONG).show();
                    btn_hora.requestFocus();
                    return false;
                }
            }else{
                editDescricao.setError("Insira uma descrição");
                editDescricao.requestFocus();
                return false;
            }
        }else{
            editTitulo.setError("Insira um Titulo");
            editTitulo.requestFocus();
            return false;
        }

    }

    private void setAlarm(String titulo, String descricao, String data, String hora){
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        /*Intent intent = new Intent(getApplicationContext(), AlarmBrodcast.class);
        intent.putExtra("tituloLembrete", titulo);
        intent.putExtra("descricaoLembrete", descricao);
        intent.putExtra("dataLembrete", data);
        intent.putExtra("horaLembrete", hora);*/

       /* PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String dateAndTime = data + " " + timeToNotify;
        java.text.DateFormat formatada = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date = formatada.parse(dateAndTime);
            am.set(AlarmManager.RTC_WAKEUP,date.getTime(),pendingIntent);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        finish();
    }
}