package com.cursoandroid.listadetarefas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.cursoandroid.listadetarefas.helper.DbHelper;
import com.cursoandroid.listadetarefas.model.Tarefa;

import java.util.ArrayList;
import java.util.List;

public class TarefaDao {
    private Context context;
    private SQLiteDatabase writeIn;
    private SQLiteDatabase readOn;

    public TarefaDao(Context context) {
        this.context = context;
        //Instanciando a classe de controle do sqlite
        DbHelper dbHelper = new DbHelper(context);

        //Recuperando uma instância do banco de dados para fazer as inserções, alterações e exclusões
        writeIn = dbHelper.getWritableDatabase();

        //Recuperando uma instância do banco de dados para fazer a leitura
        readOn = dbHelper.getReadableDatabase();
    }

    public boolean salvar(Tarefa tarefa) {

        try {
            //Definindo valores de conteúdo; é o que vamos usar para dar o insert
            ContentValues cv = new ContentValues();
            cv.put("titulo", tarefa.getTitulo());
            cv.put("fgFinalizada", "N");

            //Inserindo na tabela os valores
            writeIn.insert(DbHelper.TABELA_TAREFA, null, cv);
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public List<Tarefa> getListaTarefas(){
        //Construindo uma lista do objeto tarefas
        List<Tarefa> listaTarefas = new ArrayList<>();

        String sql = "SELECT * FROM " + DbHelper.TABELA_TAREFA + " ORDER BY id DESC;";

        //O cursor receber todos os dados que fizemos com nosso select acima
        Cursor cursor = readOn.rawQuery(sql, null);

        //Enquanto o cursor ainda tiver linhas para percorrer, vamos adicionar um objeto tarefa na nossa lista de tarefas
        while(cursor.moveToNext()) {
            //Instanciando um objeto do tipo tarefa
            Tarefa tarefa = new Tarefa();
            //Pegando o valor do cursor para o id e guardando na tarefa
            tarefa.setId( cursor.getLong(cursor.getColumnIndex("id")) );
            //Pegando o valor do cursor para o titulo/descrição e guardando na tarefa
            tarefa.setTitulo( cursor.getString( cursor.getColumnIndex("titulo")) );
            //Pegando o valor do cursor para a flag de controle e guardando na tarefa
            tarefa.setFgFinalizada( cursor.getString( cursor.getColumnIndex("fgFinalizada")) );

            //Adicionando o objeto na lista
            listaTarefas.add(tarefa);
        }

        return listaTarefas;
    }

    public boolean alterar(Tarefa tarefa) {
        try {
            //Definindo valores de conteúdo
            ContentValues cv = new ContentValues();
            cv.put("titulo", tarefa.getTitulo());

            //Definindo os argumentos da clásula where do nosso sql
            String[] args = { tarefa.getId().toString() };

            //Atualizando o registro na tabela tarefa com os valores de conteúdo, e que tenha os argumentos iguais ao que definimos em args
            writeIn.update(DbHelper.TABELA_TAREFA, cv, "id = ?", args);
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public boolean alterarStatus(Tarefa tarefa){
        try {
            //Definindo valores de conteúdo
            ContentValues cv = new ContentValues();
            cv.put("fgFinalizada", tarefa.getFgFinalizada());

            //Definindo os argumentos da clásula where do nosso sql
            String[] args = { tarefa.getId().toString() };

            //Atualizando o registro na tabela tarefa com os valores de conteúdo, e que tenha os argumentos iguais ao que definimos em args
            writeIn.update(DbHelper.TABELA_TAREFA, cv, "id = ?", args);
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }


        return true;
    }

    public boolean deletar(Tarefa tarefa) {
        try {
            //Definindo os argumentos da clásula where do nosso sql
            String[] args = { tarefa.getId().toString() };

            //Excluindo o registro na tabela tarefa que tenha os argumentos iguais ao que definimos em args
            writeIn.delete(DbHelper.TABELA_TAREFA, "id = ?", args);
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
