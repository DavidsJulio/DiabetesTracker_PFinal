package com.davidjulio.pfinal2020.activity.ui.registos;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davidjulio.pfinal2020.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class RegistosFragment extends Fragment {

    private RecyclerView rvRegistosListagem;
    private FloatingActionButton fabAdicionarRegisto;

    public RegistosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registos, container, false);

        rvRegistosListagem = view.findViewById(R.id.rvRegistosListagem);
        fabAdicionarRegisto = view.findViewById(R.id.fabAdicionarRegisto);
        fabAdicionarRegisto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarRegistos();
            }
        });


        return view;
    }

    public void adicionarRegistos(){
        Intent intent = new Intent();
        intent.setClass(getActivity(), AdicionarRegistosActivity.class);
        getActivity().startActivity(intent);
    }
}