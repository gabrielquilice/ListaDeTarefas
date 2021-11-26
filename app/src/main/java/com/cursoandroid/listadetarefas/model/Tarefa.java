package com.cursoandroid.listadetarefas.model;

public class Tarefa {
    //Identificador da tarefa
    private Long id;

    //Título/descrição da tarefa
    private String titulo;

    //Flag para indicar se a tarefa já foi concluída ou não
    private String fgFinalizada;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFgFinalizada() {
        return fgFinalizada;
    }

    public void setFgFinalizada(String fgFinalizada) {
        this.fgFinalizada = fgFinalizada;
    }
}
