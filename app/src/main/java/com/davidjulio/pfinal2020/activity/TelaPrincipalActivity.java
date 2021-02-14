package com.davidjulio.pfinal2020.activity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.davidjulio.pfinal2020.R;
import com.davidjulio.pfinal2020.config.ConfigFirebase;
import com.davidjulio.pfinal2020.helper.UtilizadorHelper;
import com.davidjulio.pfinal2020.model.Utilizador;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.internal.Util;

public class TelaPrincipalActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();

    private DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();
    private DatabaseReference utilizadorRef;
    private CircleImageView cImageNav;

    private TextView textoUsername, textoEmailUtilizador;
    View hView;

    private ValueEventListener valueEventListenerUtilizador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d("Error", "Utilizador : "+ ConfigFirebase.getCurrentUser());
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

       /* autenticacao.signOut();*/
        //Cria referencia para a area de navegação
        NavigationView navigationView = findViewById(R.id.nav_view);
        //config para passar os dados
        hView = navigationView.getHeaderView(0);
        textoUsername = hView.findViewById(R.id.textUsernameNavHeader);
        textoEmailUtilizador = hView.findViewById(R.id.textEmailNavHeader);
        cImageNav = hView.findViewById(R.id.cImageNav);

        // Define as configurações do Nav.Drawer
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_calculadora, R.id.nav_refeicoes, R.id.nav_registos, R.id.nav_lembretes,
                R.id.nav_info, R.id.nav_config, R.id.nav_perfil)
                .setOpenableLayout(drawer) //versão atual
                .build(); //constroi

        //config a area que carrega os fragmentos
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        //configura o nav(controller) menu superior de navegação
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        //Configura a navegação para o navView = carrega os itens de menu (diario, calculadora, etc)
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tela_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_exit){
            autenticacao.signOut();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void recuperarUtilizador(){
        String idUtilizador = ConfigFirebase.getCurrentUser();

        utilizadorRef = firebaseRef.child("utilizadores").child(idUtilizador);
        Log.d("Info", "utilizadorRef: "+utilizadorRef);
        valueEventListenerUtilizador = utilizadorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Utilizador utilizador = dataSnapshot.getValue(Utilizador.class);
                    textoUsername.setText(utilizador.getNome());
                    textoEmailUtilizador.setText(utilizador.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseUser utilizador = UtilizadorHelper.getUtilizador();
        Uri url = utilizador.getPhotoUrl();

        if(url != null){
            //Bib firebase
            Glide.with(this)
                    .load(url)
                    .into(cImageNav);
        }else{
            cImageNav.setImageResource(R.drawable.padrao);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarUtilizador();
    }

    @Override
    protected void onStop() {
        super.onStop();
        utilizadorRef.removeEventListener(valueEventListenerUtilizador); //vai remover o event listener
    }

}