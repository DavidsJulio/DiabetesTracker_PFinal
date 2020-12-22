package com.davidjulio.pfinal2020.activity.ui.registos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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

    private Bundle bundleRegisto;
    private Medicao medicaoSelecionada;
    private boolean editavel = false;

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

        medicaoSelecionada();
        carregarRegistos();
    }

    public void medicaoSelecionada(){
        bundleRegisto = getIntent().getExtras();
        if(bundleRegisto != null){

            fabGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editavel) {
                        recuperarDadosDigitados(medicaoSelecionada);
                        //medicaoSelecionada.guardar();
                        Toast.makeText(AdicionarRegistosActivity.this, "Atualizado com Sucesso!", Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        Toast.makeText(AdicionarRegistosActivity.this, "Este registo não pode ser Editado!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void carregarRegistos(){
        if(bundleRegisto != null){
            medicaoSelecionada = (Medicao) bundleRegisto.getSerializable(RegistosFragment.MEDICAO_SELECIONADA);

            if (medicaoSelecionada.getEditavel().equals("S")) {
                actionBar.setTitle("Medição Editável");
                editavel = true;
            }else{
                actionBar.setTitle("Medição Não Editável");
                editavel = false;
            }

            String dataHora = medicaoSelecionada.getDataHora();
            String dataAux[] = dataHora.split(" ");
            String data = dataAux[0];
            String hora = dataAux[1];

            btnData.setText(data);
            btnHora.setText(hora);
            etGlicose.setText(String.valueOf(medicaoSelecionada.getMedicaoGlicose()));

            Double valorHC = medicaoSelecionada.getMedicaoHC();
            if(valorHC == 0.0){
                etHC.setText("");
            }else {
                etHC.setText(String.valueOf(medicaoSelecionada.getMedicaoHC()));
            }

            Double valorInsulina = medicaoSelecionada.getMedicaoInsulina();
            if (valorInsulina == 0.0){
                etInsulina.setText("");
            }else{
                etInsulina.setText(String.valueOf(medicaoSelecionada.getMedicaoInsulina()));
            }
            etNota.setText(medicaoSelecionada.getNota());
        }
    }

    public void guardarMedicao(){
        if(validarCamposMedicao()){
            Medicao medicao = new Medicao();
            recuperarDadosDigitados(medicao);
            finish();
        }
    }

    public void recuperarDadosDigitados(Medicao medicaoAux){
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


        medicaoAux.setDataHora(dataHora);
        medicaoAux.setDataHoraAux(dataAuxiliar);

        try {
            Double valorGlicose = Double.parseDouble(glicose);
            medicaoAux.setMedicaoGlicose(valorGlicose);
        }catch (NumberFormatException e){
            Double valorGlicose = 0.0;
            medicaoAux.setMedicaoGlicose(valorGlicose);
        }

        try {
            Double valorHc = Double.parseDouble(hc);
            medicaoAux.setMedicaoHC(valorHc);
        }catch (NumberFormatException e){
            medicaoAux.setMedicaoHC(0.0);
        }

        try {
            Double valorInsulina = Double.parseDouble(insulina);
            medicaoAux.setMedicaoInsulina(valorInsulina);
        }catch (NumberFormatException e){
            medicaoAux.setMedicaoInsulina(0.0);
        }
        medicaoAux.setEditavel("S");
        medicaoAux.setNota(nota);
        medicaoAux.guardar();
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
                String hour;
                if(hourOfDay < 10){
                    hour = "0" + hourOfDay;
                }else{
                    hour = "" + hourOfDay;
                }

                String minutos;
                if(minute1 < 10){
                    minutos = "0" + minute1;
                }else {
                    minutos = "" + minute1;
                }

                timeToNotify = hour + ":" + minutos;
                btnHora.setText(timeToNotify);
            }
        },hour,minute,format);
        timePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (bundleRegisto != null){
            getMenuInflater().inflate(R.menu.menu_eliminar, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.action_delete:
                eliminarMedicao();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void eliminarMedicao(){
        AlertDialog.Builder dialog = new AlertDialog.Builder( this );
        dialog.setTitle("Eliminar");
        if(medicaoSelecionada.getEditavel().equals("S")) {
            dialog.setMessage("Tem a certeza que pertende eliminar esta Medição?");
        }else{
            dialog.setMessage("Não é aconselhavel eliminar esta Medição!\n\nTem a certeza que pertende eliminar esta Medição?");
        }
        dialog.setCancelable(false);

        dialog.setIcon(R.drawable.ic__delete_24);
        dialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                medicaoSelecionada.eliminar();
                finish();
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.create();
        dialog.show();
    }

}