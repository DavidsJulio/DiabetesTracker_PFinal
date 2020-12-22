package com.davidjulio.pfinal2020.activity.ui.refeicoes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.ActionBar;
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

import com.davidjulio.pfinal2020.R;
import com.davidjulio.pfinal2020.config.ConfigFirebase;
import com.davidjulio.pfinal2020.helper.Permissao;
import com.davidjulio.pfinal2020.model.Refeicao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class AdicionarRefeicaoActivity extends AppCompatActivity {

    private TextInputEditText campoNome, campoHidratos, campoCalorias, campoGordura, campoProteinas;
    private FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();

    private Refeicao refeicao;

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

    private Refeicao refeicaoSelecionada;
    Bundle bundleRefeicao;
    
    private FloatingActionButton fabSalvar;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_refeicao);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

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

        fabSalvar = findViewById(R.id.fabSalvarR);

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

        refeicaoSelecionada();
        carregaDados();

    }

    public void refeicaoSelecionada(){
        bundleRefeicao = getIntent().getExtras();
        if(bundleRefeicao != null) {
            actionBar.setTitle("Editar - Apagar Refeição");
            fabSalvar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(AdicionarRefeicaoActivity.this, "Atualizado com Sucesso", Toast.LENGTH_LONG).show();
                    String urlImagem = refeicaoSelecionada.getIdRefeicao();
                    guardarFotoRefeicao(urlImagem, refeicaoSelecionada);
                    //recuperarRefeicaoDigitada(refeicaoSelecionada);
                }
            });
        }
    }

    public void carregaDados(){
        if(bundleRefeicao != null){
            //caso o bundle seja diferente de null conseguimos recuperar a refeicao
            refeicaoSelecionada = (Refeicao) bundleRefeicao.getSerializable(RefeicoesFragment.REFEICAO_SELECIONADA); //refeicao recolhida

            campoNome.setText(refeicaoSelecionada.getNome());
            campoHidratos.setText(String.valueOf(refeicaoSelecionada.getHidratosCarbono()));

            Integer valorCalorias = refeicaoSelecionada.getCalorias();
            if(valorCalorias == 0) {
                campoCalorias.setText("");
            }else{
                campoCalorias.setText(String.valueOf(valorCalorias));
            }

            Double valorGordura = refeicaoSelecionada.getGordura();
            if(valorGordura == 0.0){
                campoGordura.setText("");
            }else{
                campoGordura.setText(String.valueOf(valorGordura));
            }

            Double valorProteinas = refeicaoSelecionada.getProteinas();
            if(valorProteinas == 0.0){
                campoProteinas.setText("");
            }else {
                campoProteinas.setText(String.valueOf(valorProteinas));
            }

            String foto = refeicaoSelecionada.getUrlFoto();
            if(foto != null){
                Picasso.get().load(foto).into(ivRefeicoes);
            }
        }
    }

    public void guardarRefeicao(View view){
        if ( validarCamposRefeicao() ){
            String urlImagem = refeicao.getIdRefeicao();
            guardarFotoRefeicao(urlImagem, refeicao);
            finish();
        }

    }

    public void recuperarRefeicaoDigitada(Refeicao refeicaoAux){
        refeicaoAux.setNome(campoNome.getText().toString());

        Double hidratos = Double.parseDouble(campoHidratos.getText().toString());
        refeicaoAux.setHidratosCarbono(hidratos);

        String textoCalorias = campoCalorias.getText().toString();

        try {
            Integer calorias = Integer.parseInt(textoCalorias);
            refeicaoAux.setCalorias(calorias);
        }catch (NumberFormatException e){
            Integer calorias = 0;
            refeicaoAux.setCalorias(calorias);
        }

        String textoGordura = campoGordura.getText().toString();
        try {
            Double gordura = Double.parseDouble(textoGordura);
            refeicaoAux.setGordura(gordura);
        }catch (NumberFormatException e){
            Double gordura = 0.0;
            refeicaoAux.setGordura(gordura);
        }

        String textoProteina = campoProteinas.getText().toString();
        try {
            Double proteina = Double.parseDouble(textoProteina);
            refeicaoAux.setProteinas(proteina);
        }catch (NumberFormatException e){
            Double proteina = 0.0;
            refeicaoAux.setProteinas(proteina);
        }
        refeicaoAux.guardar();
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

    private void guardarFotoRefeicao(String urlImagem, final Refeicao aux){//String urlString
        String idUtilizador = ConfigFirebase.getCurrentUser();
        ivRefeicoes.setDrawingCacheEnabled(true);
        ivRefeicoes.buildDrawingCache();

        Bitmap bitmap = ivRefeicoes.getDrawingCache();

        //comperssao bitmap -> png/jpeg
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);

        byte[] dados = baos.toByteArray(); //permite enviar para o Firebase

        final StorageReference imagemRef = storageRef.child("imagens")
                                                     .child(idUtilizador)
                                                     .child("refeicoes")
                                                     .child(urlImagem);

        UploadTask uploadTask = imagemRef.putBytes(dados);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri url = task.getResult();
                        String urlConvertida = url.toString();

                        if (urlConvertida != null){
                            aux.setUrlFoto( urlConvertida );
                        }
                        recuperarRefeicaoDigitada(aux);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        if (bundleRefeicao != null)
            getMenuInflater().inflate(R.menu.menu_eliminar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.action_delete:
                eliminarDialog();
                break;
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
                switch (requestCode){
                    case CAMARA_SELECIONADA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case GALERIA_SELECIONADA:
                        Uri imagemSelecionada = data.getData();
                        //localDaImagem = imagemSelecionada.toString();
                        ivRefeicoes.setImageURI(imagemSelecionada);
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), imagemSelecionada);
                        break;
                }

                if (imagem != null){
                    ivRefeicoes.setImageBitmap( imagem );
                    /*String urlImagem = refeicao.getIdRefeicao();
                    guardarFoto(urlImagem);*/
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
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

    public void eliminarDialog(){
        //instanciar
        AlertDialog.Builder dialog = new AlertDialog.Builder( this );

        //titulo e mensagem
        dialog.setTitle("Eliminar");
        dialog.setMessage("Tem a certeza que pertende eliminar esta Refeição?");

        //impedir clicar fora dos botoes
        dialog.setCancelable(false);

        //icon
        dialog.setIcon(R.drawable.ic__delete_24);

        //botao sim/não
        dialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                refeicaoSelecionada.eliminar();
                finish();
            }

        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //criar e exibir
        dialog.create();
        dialog.show();
    }
}