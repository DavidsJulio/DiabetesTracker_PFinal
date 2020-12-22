package com.davidjulio.pfinal2020.model;

import com.davidjulio.pfinal2020.config.ConfigFirebase;
import com.davidjulio.pfinal2020.helper.Base64Custom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.Comparator;

public class Medicao implements Serializable {

    private String idMedicao;
    private Double medicaoGlicose;
    private Double medicaoHC;
    private Double medicaoInsulina;

    private String dataHora;
    private String dataHoraAux;
    private String nota;
    private String editavel;

    public Medicao() {
        DatabaseReference reference = ConfigFirebase.getFirebaseDatabase().child("medicoesGlicose");
        setIdMedicao( reference.push().getKey() );
    }

    public void guardar(){
        FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        String idUtilizador = Base64Custom.codificarBase64( autenticacao.getCurrentUser().getEmail() );

        DatabaseReference firebase = ConfigFirebase.getFirebaseDatabase();
        firebase.child("medicoesGlicose")
                .child(idUtilizador)
                .child(idMedicao)
                .setValue(this);
    }

    public String getEditavel() {
        return editavel;
    }

    public void setEditavel(String editavel) {
        this.editavel = editavel;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public Double getMedicaoHC() {
        return medicaoHC;
    }

    public void setMedicaoHC(Double medicaoHC) {
        this.medicaoHC = medicaoHC;
    }

    public Double getMedicaoInsulina() {
        return medicaoInsulina;
    }

    public void setMedicaoInsulina(Double medicaoInsulina) {
        this.medicaoInsulina = medicaoInsulina;
    }

    public String getDataHoraAux() {
        return dataHoraAux;
    }

    public void setDataHoraAux(String dataHoraAux) {
        this.dataHoraAux = dataHoraAux;
    }

    public Double getMedicaoGlicose() {
        return medicaoGlicose;
    }

    public void setMedicaoGlicose(Double medicaoGlicose) {
        this.medicaoGlicose = medicaoGlicose;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public String getIdMedicao() {
        return idMedicao;
    }

    public void setIdMedicao(String idMedicao) {
        this.idMedicao = idMedicao;
    }

    public static final Comparator<Medicao> BY_DATE_DESCENDING = new Comparator<Medicao>() {
        @Override
        public int compare(Medicao o1, Medicao o2) {
            return o2.getDataHoraAux().compareTo(o1.getDataHoraAux());
        }
    };

    public static final Comparator<Medicao> BY_DATE_ASCENDING = new Comparator<Medicao>() {
        @Override
        public int compare(Medicao o1, Medicao o2) {
            return o1.getDataHoraAux().compareTo(o2.getDataHoraAux());
        }
    };
}
