package com.cursoandroid.listadetarefas.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cursoandroid.listadetarefas.R;
import com.cursoandroid.listadetarefas.dao.TarefaDao;
import com.cursoandroid.listadetarefas.model.Tarefa;

public class AdicionarTarefaActivity extends AppCompatActivity {
    private EditText edtNovaTarefa;
    private Button btnTarefa;
    private Long idTarefa;
    private Tarefa tarefaAtual;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tarefa);

        idTarefa = getIntent().getLongExtra("idTarefa", 0);
        edtNovaTarefa = findViewById(R.id.edtNovaTarefa);
        btnTarefa = findViewById(R.id.btnTarefa);

        //Caso a activity seja aberta passando um
        if (idTarefa == 0) {
            setTitle("Adicionar tarefa");
        } else {
            setTitle("Editar tarefa");
            //Para cada item da lista de tarefas, vamos verificar se o id é igual a variável idTarefa
            MainActivity.listaTarefas.forEach(tarefa -> {
                if (tarefa.getId() == idTarefa) {
                    tarefaAtual = tarefa;
                }
            });
            edtNovaTarefa.setText(tarefaAtual.getTitulo());
            btnTarefa.setText("ATUALIZAR");
        }

        //Adicionando evento de clique no nosso botão
        btnTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    salvarTarefa();
                } catch (Exception e) {
                    Toast.makeText(AdicionarTarefaActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Essa verificação vai acontecer sempre que o usuário clicar em algum dos itens na barra superior
        switch (item.getItemId()){
            //Caso ele clique no ícone de seta, vamos fechar a Activity
            case R.id.home:
                this.finish();
                break;
            //Caso ele clique no ícone verde, vamos salvar a tarefa
            case R.id.action_done:
                try {
                    salvarTarefa();
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void salvarTarefa() {
        try {
            //Se o texto da terfa estiver vazio, não vamos fazer nada
            if (edtNovaTarefa.getText().toString().isEmpty()) {
                return;
            }

            TarefaDao tarefaDao = new TarefaDao(this);
            Tarefa tarefa = new Tarefa();

            if (tarefaAtual == null) {
                tarefa.setTitulo(edtNovaTarefa.getText().toString());
                if (tarefaDao.salvar(tarefa)) {
                    Toast.makeText(this, "Tarefa salva com sucesso!", Toast.LENGTH_SHORT).show();
                    this.finish();
                }

            } else {
                tarefa.setId(tarefaAtual.getId());
                tarefa.setTitulo(edtNovaTarefa.getText().toString());
                if (tarefaDao.alterar(tarefa)) {
                    Toast.makeText(this, "Tarefa alterada com sucesso!", Toast.LENGTH_SHORT).show();
                    this.finish();
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }
}