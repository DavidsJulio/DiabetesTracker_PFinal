package com.davidjulio.pfinal2020.activity.ui.configuration;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import android.app.AlertDialog;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.davidjulio.pfinal2020.R;
import com.davidjulio.pfinal2020.activity.TelaPrincipalActivity;
import com.davidjulio.pfinal2020.config.ConfigFirebase;
import com.davidjulio.pfinal2020.helper.Permissao;
import com.davidjulio.pfinal2020.model.Utilizador;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;


public class ConfigurationFragment extends Fragment {

    private CircleImageView cvConfigImgPerfil;
    private TextInputEditText etConfigNome, etConfigEmail, etConfigPass;
    private Button btnConfigData;
    private RadioGroup radioGroupSexo;
    private RadioButton rbMasculino, rbFeminino, radioButton;
    private EditText etConfigAltura, etConfigPeso;
    private ImageButton ibConfigCamara, ibConfigGaleria;

    private DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();
    private DatabaseReference configRef;
    private ValueEventListener valueEventListenerConfig;
    private String sexo = "";

    private String[] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private static final int CAMARA_SELECIONADA = 100;
    private static final int GALERIA_SELECIONADA = 200;
    private Bitmap imagem = null;
    private StorageReference storageReference;

    public ConfigurationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_configuration, container, false);

        storageReference = ConfigFirebase.getFirebaseStorage();
        Permissao.validarPermissoes(permissoes, getActivity(), 1);

        cvConfigImgPerfil = view.findViewById(R.id.cvConfigImgPerfil);
        etConfigNome = view.findViewById(R.id.etConfigNome);
        etConfigEmail = view.findViewById(R.id.etConfigEmail);
        etConfigPass = view.findViewById(R.id.etConfigPass);
        btnConfigData = view.findViewById(R.id.btnConfigData);
        radioGroupSexo = view.findViewById(R.id.radioGroupSexo);
        rbMasculino = view.findViewById(R.id.rbMasculino);
        rbFeminino = view.findViewById(R.id.rbFeminino);
        ibConfigCamara = view.findViewById(R.id.ibConfigCamara);
        ibConfigGaleria = view.findViewById(R.id.ibConfigGaleria);

        FirebaseUser utilizador = Utilizador.getUtilizador();
        Uri url = utilizador.getPhotoUrl();

        if(url != null){
            //Bib firebase
            Glide.with(getActivity())
                    .load(url)
                    .into(cvConfigImgPerfil);


        }else{
            cvConfigImgPerfil.setImageResource(R.drawable.padrao);
        }

        ibConfigCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(i, CAMARA_SELECIONADA);
                }
            }
        });

        ibConfigGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new  Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null){
                    //caso seja possivel abri a galeria
                    startActivityForResult(intent, GALERIA_SELECIONADA);
                }
            }
        });

        radioGroupSexo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbMasculino:
                        sexo = "M";
                        break;
                    case R.id.rbFeminino:
                        sexo = "F";
                        break;
                }
            }
        });

        etConfigAltura = view.findViewById(R.id.etConfigAltura);
        etConfigPeso = view.findViewById(R.id.etConfigPeso);

        btnConfigData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataNascimento();
            }
        });

        return view;
    }




    public void dataNascimento(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
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
                btnConfigData.setText(diaDoMes + "/" + mes + "/" + year);
            }
        },year,month,day);
        datePickerDialog.show();
    }

    public void guardarUtilizador(){
        Utilizador utilizador = new Utilizador();
        recuperarDadosConfig(utilizador);
    }

    public void recuperarDadosConfig(Utilizador utilizadorAux){
        String nome = etConfigNome.getText().toString();
        String email = etConfigEmail.getText().toString();
        String dataNascimento = btnConfigData.getText().toString();

        String altura = etConfigAltura.getText().toString();
        String peso = etConfigPeso.getText().toString();



        utilizadorAux.setNome(nome);
        utilizadorAux.setEmail(email);
        utilizadorAux.setDataNascimento(dataNascimento);


        if(sexo != ""){
            utilizadorAux.setSexo(sexo);
        }

        Integer valorAltura = Integer.parseInt(altura);
        try {
            utilizadorAux.setAltura(valorAltura);
        }catch (NumberFormatException e){
            utilizadorAux.setAltura(0);
        }

        Double valorPeso = Double.parseDouble(peso);
        try {
            utilizadorAux.setPeso(valorPeso);
        }catch (NumberFormatException e){
            utilizadorAux.setPeso(0.0);
        }

        utilizadorAux.guardar();
    }

    public void recuperarUtilizador(){
        String idUtilizador = ConfigFirebase.getCurrentUser();

        configRef = firebaseRef.child("utilizadores").child(idUtilizador);

        valueEventListenerConfig = configRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Utilizador util = dataSnapshot.getValue(Utilizador.class);
                    etConfigNome.setText(util.getNome());
                    etConfigEmail.setText(util.getEmail());
                    
                    btnConfigData.setText(util.getDataNascimento());

                    if (util.getSexo() != null) {
                        switch (util.getSexo()) {
                            case "M":
                                rbMasculino.setChecked(true);
                                break;
                            case "F":
                                rbFeminino.setChecked(true);
                        }
                    }
                    String altura = String.valueOf(util.getAltura());
                    if(altura.equals("null")){
                        etConfigAltura.setText("");
                    }else {
                        etConfigAltura.setText(altura);
                    }

                    String peso = String.valueOf(util.getPeso());
                    if(peso.equals("null")){
                        etConfigPeso.setText("");
                    }else{
                        etConfigPeso.setText(peso);
                    }


/*                    double nrAltura = util.getAltura() / 100.0;
                    double nrPeso = util.getPeso();
                    Double imc = (nrPeso / (nrAltura * nrAltura ));
                    Log.d("IMC", "IMC: " + imc);*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == getActivity().RESULT_OK){
            try {
                switch (requestCode){
                    case CAMARA_SELECIONADA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case GALERIA_SELECIONADA:
                        Uri imagemSelecionada = data.getData();
                        cvConfigImgPerfil.setImageURI(imagemSelecionada);
                        imagem = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imagemSelecionada);
                        break;
                }

                if (imagem != null){
                    cvConfigImgPerfil.setImageBitmap( imagem );
                    String idUtilizador = ConfigFirebase.getCurrentUser();

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //Guardar a imagem
                    final StorageReference imageRef = storageReference.child("imagens")
                                                                      .child(idUtilizador)
                                                                      .child("perfil")
                                                                      .child("perfil.jpeg");

                    UploadTask uploadTask = imageRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Erro ao fazer Upload da Imagem", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getActivity(), "Sucesso ao fazer Upload da Imagem", Toast.LENGTH_SHORT).show();

                            imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();
                                    atualizarFoto(url);
                                }
                            });
                        }
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void atualizarFoto(Uri url){
        Utilizador.atualizarFoto(url);
    }

    public void alertaParaPermissao(){
        AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(getActivity());
        dialog.setTitle("Permissões negadas");
        dialog.setMessage("Para utilizar a aplicação precisa de aceitar as permissões");
        dialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().getFragmentManager().popBackStack();
            }

        });

        final AlertDialog alertDialog = dialog.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                TextView message = alertDialog.findViewById(android.R.id.message);
                if(message != null){
                    message.setTextSize(20);
                }

            }
        });
        alertDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarUtilizador();
    }

    @Override
    public void onStop() {
        super.onStop();
        guardarUtilizador();
        configRef.removeEventListener(valueEventListenerConfig);
    }
}