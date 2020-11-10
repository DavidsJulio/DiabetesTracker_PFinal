package com.davidjulio.pfinal2020.activity.ui.refeicoes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.davidjulio.pfinal2020.R;
import com.davidjulio.pfinal2020.config.ConfigFirebase;
import com.davidjulio.pfinal2020.helper.Base64Custom;
import com.davidjulio.pfinal2020.helper.Permissao;
import com.davidjulio.pfinal2020.model.Refeicao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class AdicionarRefeicaoActivity extends AppCompatActivity {

    private TextInputEditText campoNome, campoHidratos, campoCalorias, campoGordura, campoProteinas;
    private FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();

    private Refeicao refeicao;
    private Double proteina ;

    private String[] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private ImageButton ibCamara, ibGaleria;
    private static final int CAMARA_SELECIONADA = 100;
    private static final int GALERIA_SELECIONADA = 200;
    private ImageView ivRefeicoes;

    private StorageReference storageRef;

   private Bitmap imagem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_refeicao);



        storageRef = ConfigFirebase.getFirebaseStorage();
        refeicao = new Refeicao();

        //Validar permissões
        Permissao.validarPermissoes(permissoes, this, 1);

        campoNome = findViewById(R.id.editNomeR);
        campoHidratos = findViewById(R.id.editHidratosR);
        campoCalorias = findViewById(R.id.editCaloriasR);
        campoGordura = findViewById(R.id.editGorduraR);
        campoProteinas = findViewById(R.id.editProteinaR);

        ibCamara = findViewById(R.id.ibAdicionarFoto);
        ibGaleria = findViewById(R.id.ibAdicionarImagem);
        ivRefeicoes = findViewById(R.id.ivAdicionarRefeicao);

        campoCalorias.setText("0");
        campoGordura.setText("0.0");
        campoProteinas.setText("0.0");

        ibCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new  Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null){
                    //caso seja possivel abri a camara
                    startActivityForResult(intent, CAMARA_SELECIONADA);
                }
            }
        });

        ibGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //caminho padrao, para as fotos
                Intent intent = new  Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                if (intent.resolveActivity(getPackageManager()) != null){
                    //caso seja possivel abri a galeria
                    startActivityForResult(intent, GALERIA_SELECIONADA);
                }
            }
        });
    }

    public void guardarRefeicao(View view){
       if ( validarCamposRefeicao() ){
            //refeicao = new Refeicao();

            refeicao.setNome(campoNome.getText().toString());

            Double hidratos = Double.parseDouble(campoHidratos.getText().toString());
            refeicao.setHidratosCarbono(hidratos);

            String textoCalorias = campoCalorias.getText().toString();
            Integer calorias = Integer.parseInt(campoCalorias.getText().toString());

            //TODO: Corrigir quando vai empty.....
            if(textoCalorias.length()>0) {
                refeicao.setCalorias(calorias);
            }else{
                refeicao.setCalorias(0);
            }

            Double gordura = Double.parseDouble(campoGordura.getText().toString());
            String textoGordura = campoHidratos.getText().toString();
            if(!textoGordura.isEmpty()) {
                refeicao.setGordura(gordura);
            }else{
                refeicao.setGordura(0.0);
            }

            proteina = Double.parseDouble(campoProteinas.getText().toString());
            String textoProteina = campoHidratos.getText().toString();
            if(!textoProteina.isEmpty()) {
                refeicao.setProteinas(proteina);
            }else if(proteina == null){
                refeicao.setProteinas(0.0);
            }

            refeicao.guardar();
            finish();
       }
    }

    public boolean validarCamposRefeicao() {

        String textoNome = campoNome.getText().toString();
        String textoHidratos = campoHidratos.getText().toString();


        if (!textoNome.isEmpty()) {
            if (!textoHidratos.isEmpty()) {
                return true;
            } else {
                campoHidratos.setError("Preencha os Hidratos de Carbono");
                campoHidratos.requestFocus();
                return false;
            }
        } else {
            campoNome.setError("Preencha o nome para a sua Refeição!");
            campoNome.requestFocus();
            return false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_voltar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_undo:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            //Condição para ver se não houve nenhum erro
            //Bitmap imagem = null; // null porque recuperamos imagem de dois sitios

            try { // imagem configurada para a camara e galeria
                switch (requestCode ){
                    case CAMARA_SELECIONADA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case GALERIA_SELECIONADA:
                        Uri localDaImagem = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localDaImagem);
                        break;
                }

                //garantir que é diferente de nula
                if (imagem != null){
                    ivRefeicoes.setImageBitmap( imagem );

                    //guardar imagem no firebase
                    String urlImagem = refeicao.getIdRefeicao();
                    guardarFoto(urlImagem);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void guardarFoto(String urlImagem){//String urlString

        String emailUtilizador = autenticacao.getCurrentUser().getEmail();
        String idUtilizador = Base64Custom.codificarBase64( emailUtilizador );

        ivRefeicoes.setDrawingCacheEnabled(true);
        ivRefeicoes.buildDrawingCache();

        Bitmap bitmap = ivRefeicoes.getDrawingCache();

        //comperssao bitmap -> png/jpeg
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);

        byte[] dados = baos.toByteArray(); //permite enviar para o Firebase

        //criar o nó
        //atenção ao final
        final StorageReference imagemRef = storageRef.child("imagens")
                                                     .child(idUtilizador)
                                                     .child("refeicoes")
                                                     .child(urlImagem);

        //upload da imagem
        UploadTask uploadTask = imagemRef.putBytes(dados);
        
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri url = task.getResult();
                        String urlConvertida = url.toString();

                        refeicao.setUrlFoto( urlConvertida ); //guardar o link url do firebase
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdicionarRefeicaoActivity.this, "Falha ao fazer upload", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int resultadoPermissoes : grantResults){
            if ( resultadoPermissoes == PackageManager.PERMISSION_DENIED){
                //caso a permissão seja negada, alertar o utilizador
                alertaParaPermissao();
            }
        }
    }

    private void alertaParaPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para utilizar a aplicação precisa de aceitar as permissões");
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}