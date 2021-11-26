package com.cursoandroid.listadetarefas.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.cursoandroid.listadetarefas.R;
import com.cursoandroid.listadetarefas.dao.TarefaDao;
import com.cursoandroid.listadetarefas.model.Tarefa;
import com.google.android.material.textfield.TextInputEditText;

import java.util.stream.Collectors;

public class AdicionarTarefaActivity extends AppCompatActivity {
    private EditText edtNovaTarefa;
    private Long idTarefa;
    private Tarefa tarefaAtual;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tarefa);

        idTarefa = getIntent().getLongExtra("idTarefa", 0);
        edtNovaTarefa = findViewById(R.id.edtNovaTarefa);

        //Caso a activity seja aberta passando um
        if (idTarefa == 0) {
            setTitle("Adicionar tarefa");
        } else {
            setTitle("Editar tarefa");
            MainActivity.listaTarefas.forEach(tarefa -> {
                if (tarefa.getId() == idTarefa) {
                    tarefaAtual = tarefa;
                }
            });
            edtNovaTarefa.setText(tarefaAtual.getTitulo());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.home:
                this.finish();
                break;
            case R.id.action_done:
                try {
                    if (edtNovaTarefa.getText().toString().isEmpty()) {
                        return false;
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
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}