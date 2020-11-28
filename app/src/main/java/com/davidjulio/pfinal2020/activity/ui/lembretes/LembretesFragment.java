package com.davidjulio.pfinal2020.activity.ui.lembretes;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.davidjulio.pfinal2020.R;
import com.davidjulio.pfinal2020.adapter.AdapterLembretes;
import com.davidjulio.pfinal2020.config.ConfigFirebase;
import com.davidjulio.pfinal2020.helper.Base64Custom;
import com.davidjulio.pfinal2020.model.Lembrete;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class LembretesFragment extends Fragment {

    private RecyclerView recyclerViewLembretes;
    private AdapterLembretes adapterLembretes;
    private List<Lembrete> listaLembretes = new ArrayList<>();

    private DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();
    private DatabaseReference lembreteRef;

    private ValueEventListener valueEventListenerLembrete;

    public LembretesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_lembretes, container, false);
        setHasOptionsMenu(true);

        lembreteRef = ConfigFirebase.getFirebaseDatabase()
                                    .child("lembretes");

        recyclerViewLembretes = view.findViewById(R.id.recyclerViewLembretes);
        FloatingActionButton fabLembrete = view.findViewById(R.id.fabAdicionarLembretes);
        fabLembrete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), AdicionarLembreteActivity.class);
                getActivity().startActivity(intent);
            }
        });

        adapterLembretes = new AdapterLembretes(listaLembretes,getContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewLembretes.setLayoutManager(layoutManager);
        recyclerViewLembretes.setHasFixedSize(true);
        recyclerViewLembretes.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
        recyclerViewLembretes.setAdapter(adapterLembretes);

        return view;
    }

    public void recuperarLembretes(){
        String emailUtilizador = autenticacao.getCurrentUser().getEmail();
        String idUtilizador = Base64Custom.codificarBase64( emailUtilizador );

        lembreteRef = firebaseRef.child("lembretes")
                            .child( idUtilizador );

        valueEventListenerLembrete = lembreteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaLembretes.clear();

                for(DataSnapshot dadosLembrete: dataSnapshot.getChildren()){
                    Lembrete lembrete = dadosLembrete.getValue(Lembrete.class);
                    lembrete.setIdLembrete( dadosLembrete.getKey() );
                    listaLembretes.add( lembrete );
                }
                adapterLembretes.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarLembretes();
    }

    @Override
    public void onStop() {
        super.onStop();
        lembreteRef.removeEventListener(valueEventListenerLembrete);
    }
}