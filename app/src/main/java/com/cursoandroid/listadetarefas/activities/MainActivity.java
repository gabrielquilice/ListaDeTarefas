package com.cursoandroid.listadetarefas.activities;

import android.content.Intent;
import android.os.Bundle;

import com.cursoandroid.listadetarefas.R;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cursoandroid.listadetarefas.adapter.TarefasAdapter;
import com.cursoandroid.listadetarefas.dao.TarefaDao;
import com.cursoandroid.listadetarefas.databinding.ActivityMainBinding;
import com.cursoandroid.listadetarefas.model.Tarefa;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    public static List<Tarefa> listaTarefas = new ArrayList<>();
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        recyclerView = findViewById(R.id.rvTarefas);

        //Criando um evento de clique para o FloatingActionButton, nosso botão +
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itNovaTarefa = new Intent(MainActivity.this, AdicionarTarefaActivity.class);
                startActivity(itNovaTarefa);
            }
        });
    }

    //Esse método é chamado todas as vezes em que abrimos essa tela
    @Override
    protected void onResume() {
        super.onResume();
        //Carregando a lista de tarefas cadastradas
        loadListaTarefas();
    }

    private void loadListaTarefas(){
        //Instanciando a classe que fará as consultas no sqlite
        TarefaDao tarefaDao = new TarefaDao(this);

        //Pegando a lista de tarefas cadastradas e adicionando na variável
        listaTarefas = tarefaDao.getListaTarefas();

        //Definindo as configurações da recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        //Definindo o adaptador para a recyclerView, usando a nossa lista de tarefas
        TarefasAdapter tarefasAdapter = new TarefasAdapter(listaTarefas, this);
        recyclerView.setAdapter(tarefasAdapter);
    }

}