package com.davidjulio.pfinal2020.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.davidjulio.pfinal2020.R;
import com.davidjulio.pfinal2020.config.ConfigFirebase;
import com.davidjulio.pfinal2020.helper.Base64Custom;
import com.davidjulio.pfinal2020.model.Utilizador;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
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

public class TelaPrincipalActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();

    private DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();
    private DatabaseReference utilizadorRef;

    private TextView textoUsername, textoEmailUtilizador;
    View hView;

    private ValueEventListener valueEventListenerUtilizador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        //Cria referencia para a area de navegação
        NavigationView navigationView = findViewById(R.id.nav_view);
        //config para passar os dados
        hView = navigationView.getHeaderView(0);
        textoUsername = hView.findViewById(R.id.textUsernameNavHeader);
        textoEmailUtilizador = hView.findViewById(R.id.textEmailNavHeader);

        // Define as configurações do Nav.Drawer
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_calculadora, R.id.nav_refeicoes, R.id.nav_lembretes,
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
        //autenticacao = ConfigFirebase.getFirebaseAutenticacao();
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


    public void recuperarPerfil(){
        String emailUtilizador = autenticacao.getCurrentUser().getEmail();
        String idUtilizador = Base64Custom.codificarBase64( emailUtilizador );
        utilizadorRef = firebaseRef.child("utilizadores")
                                    .child( idUtilizador );

        //valueEventListenerUsuario, objeto para o value
        valueEventListenerUtilizador = utilizadorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Utilizador utilizador = snapshot.getValue( Utilizador.class );

             /*   Log.d("Info", "utilizador: "+utilizador);
                Log.d("infoUsername", "username: "+utilizador.getNome());
                Log.d("infoUsername", "email: "+utilizador.getEmail());*/

                textoUsername.setText( utilizador.getNome() );
                //Log.d("infoUsername", "email: "+utilizador.getEmail());
                textoEmailUtilizador.setText( utilizador.getEmail() );
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarPerfil();
    }

    @Override
    protected void onStop() {
        super.onStop();
        utilizadorRef.removeEventListener(valueEventListenerUtilizador); //vai remover o event listener
    }

}