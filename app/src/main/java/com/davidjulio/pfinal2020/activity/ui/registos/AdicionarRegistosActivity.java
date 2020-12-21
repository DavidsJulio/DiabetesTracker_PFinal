package com.davidjulio.pfinal2020.activity.ui.registos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.davidjulio.pfinal2020.R;
import com.davidjulio.pfinal2020.helper.DateUtil;
import com.davidjulio.pfinal2020.model.Medicao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AdicionarRegistosActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private Button btnData, btnHora;
    private EditText etGlicose, etHC, etInsulina, etNota;
    private FloatingActionButton fabGuardar;

    private Calendar calendar = Calendar.getInstance();
    String timeToNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_registos);

        //Bar
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //Data/Hora
        btnData = findViewById(R.id.btnMedicaoData);
        btnHora = findViewById(R.id.btnMedicaoHora);
        //EditText
        etGlicose = findViewById(R.id.etRegistoGlicose);
        etHC = findViewById(R.id.etRegistoHc);
        etInsulina = findViewById(R.id.etRegistoInsulina);
        etNota = findViewById(R.id.etRegistoAdicionarNota);
        //Fab
        fabGuardar = findViewById(R.id.fabRegistoGuardar);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String date = sdf.format(calendar.getTime());
        btnData.setText(date);

        String hora = "--:--";
        btnHora.setText(hora);

        fabGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarMedicao();
            }
        });

    }

    public void guardarMedicao(){
        if(validarCamposMedicao()){
                String data = btnData.getText().toString();
                String hora = btnHora.getText().toString();
                String glicose = etGlicose.getText().toString().trim();
                String hc = etHC.getText().toString().trim();
                String insulina = etInsulina.getText().toString().trim();
                String nota = etNota.getText().toString().trim();

                String dataHora = data + " " + hora;
                String dataAux[] = data.split("/");
                String dia = dataAux[0];
                String mes = dataAux[1];
                String ano = dataAux[2];

                String dataAuxiliar = ano + "/" + mes + "/" + dia + " " + hora;
                Medicao medicao = new Medicao();

                medicao.setDataHora(dataHora);
                medicao.setDataHoraAux(dataAuxiliar);

            try {
                Double valorGlicose = Double.parseDouble(glicose);
                medicao.setMedicaoGlicose(valorGlicose);
            }catch (NumberFormatException e){
                Double valorGlicose = 0.0;
                medicao.setMedicaoGlicose(valorGlicose);
            }

            try {
                Double valorHc = Double.parseDouble(hc);
                medicao.setMedicaoHC(valorHc);
            }catch (NumberFormatException e){
                medicao.setMedicaoHC(0.0);
            }

            try {
                Double valorInsulina = Double.parseDouble(insulina);
                medicao.setMedicaoInsulina(valorInsulina);
            }catch (NumberFormatException e){
                medicao.setMedicaoInsulina(0.0);
            }
            medicao.setEditavel("S");
            medicao.setNota(nota);
            medicao.guardar();
            finish();
        }
    }


    public boolean validarCamposMedicao(){
        String hora = btnHora.getText().toString();
        String glicose = etGlicose.getText().toString();

        if(!hora.equals("--:--")){
            if(!glicose.isEmpty()){
                return true;
            }else{
                etGlicose.setError("Insira um valor para a Glicose!");
                etGlicose.requestFocus();
                return false;
            }
        }else{
            btnHora.setError("Insira uma Hora");
            Toast.makeText(this, "Por favor, Insira uma Hora", Toast.LENGTH_LONG).show();
            btnHora.requestFocus();
            return false;
        }
    }

    public void escolherData(View view) {
        selectDate();
    }

    public void escolherHora(View view) {
        selectTime();
    }

    private void selectDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String diaDoMes;
                if(dayOfMonth < 10){
                    diaDoMes = "0" + dayOfMonth;
                }else{
                    diaDoMes = "" + dayOfMonth;
                }

                String mes;
                month += 1;
                if(month < 10){

                    mes = "0" + month;
                }else{
                    mes = "" + month;
                }
                btnData.setText(diaDoMes + "/" + mes + "/" + year);
            }
        },year,month,day);
        datePickerDialog.show();
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
                btnHora.setText(timeToNotify);
                //btn_hora.setText(formatTime(hourOfDay, minute1));
            }
        },hour,minute,format);
        timePickerDialog.show();
        //TODO: PRECISO ACERTAR A HORA
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}