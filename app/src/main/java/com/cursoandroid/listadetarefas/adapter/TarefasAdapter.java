package com.cursoandroid.listadetarefas.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.cursoandroid.listadetarefas.R;
import com.cursoandroid.listadetarefas.activities.AdicionarTarefaActivity;
import com.cursoandroid.listadetarefas.dao.TarefaDao;
import com.cursoandroid.listadetarefas.holder.TarefasHolder;
import com.cursoandroid.listadetarefas.model.Tarefa;

import java.util.List;

public class TarefasAdapter extends RecyclerView.Adapter<TarefasHolder> {
    private List<Tarefa> listaTarefas;
    private Context context;

    public TarefasAdapter(List<Tarefa> listaTarefas, Context context) {
        this.listaTarefas = listaTarefas;
        this.context = context;
    }

    @NonNull
    @Override
    public TarefasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarefas_lista, parent, false);
        return new TarefasHolder(item);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull TarefasHolder holder, int position) {
        //Pega a atrefa na posição atual
        Tarefa tarefa = listaTarefas.get(position);

        try {
            //Se a tarefa já foi concluída, muda a cor de fundo
            if(tarefa.getFgFinalizada().equals("S")) {
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.fg_finalizada));
            }

            //Ao clicar na tarefa, vamos abrir uma caixa de diálogo
            holder.tituloTarefa.setOnClickListener(view -> {
                //Criação da caixa de diálogo
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                //Se a tarefa estiver finalizada, o título será DESMARCAR, se não será CONCLUIR
                dialog.setTitle(tarefa.getFgFinalizada().equals("N") ? "CONCLUIR" : "DESMARCAR");
                dialog.setMessage(tarefa.getFgFinalizada().equals("N") ? "Deseja marcar a nota como concluída?" : "Deseja desmarcar a nota como concluída?");
                //Definindo o que acontece ao clicar no botão SIM
                dialog.setPositiveButton("SIM", (dialog1, which) -> {
                    try {
                        TarefaDao tarefaDao = new TarefaDao(context);
                        //Se a tarefa não estiver finalizada, vamos alterar o status para S; se estiver, vai voltar ao status N
                        tarefa.setFgFinalizada( tarefa.getFgFinalizada().equals("N") ? "S" : "N" );
                        if (tarefaDao.alterarStatus(tarefa)) {
                            //Mandando o recyclerView recarregar a visualização da nossa lista
                            notifyItemChanged(position);
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                dialog.setNegativeButton("NÃO", (dialog2, which) -> {
                    dialog2.dismiss();
                });
                dialog.show();
            });

            //Ao clicar em editar, vamos abrir uma nova Activity, passando o id da tarefa
            holder.btnEditar.setOnClickListener(view -> {
                Intent intent = new Intent(context, AdicionarTarefaActivity.class);
                intent.putExtra("idTarefa", tarefa.getId());
                context.startActivity(intent);
            });

            //Ao clicar em apagar, vamos abrir uma caixa de diálogo
            holder.btnApagar.setOnClickListener(view -> {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("EXCLUIR");
                dialog.setMessage("Deseja excluir essa nota?");
                //Se o usuário clicar em SIM, a tarefa será apagada
                dialog.setPositiveButton("SIM", (dialog1, which) -> {
                    try {
                        TarefaDao tarefaDao = new TarefaDao(context);
                        if (tarefaDao.deletar(tarefa)) {
                            listaTarefas.remove(tarefa);
                            notifyItemRemoved(position);
                            Toast.makeText(context, "Tarefa excluída com sucesso!", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                dialog.setNegativeButton("NÃO", (dialog2, which) -> {
                    dialog2.dismiss();
                });
                dialog.show();
            });

            //Defindo o título da tarefa
            holder.tituloTarefa.setText(tarefa.getTitulo());
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return listaTarefas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return listaTarefas.size() == 0 ? 0 : listaTarefas.get(position).getId().intValue();
    }
}
