package org.example.dto;

import java.util.Objects;

public class TarefaDTO {
    private String titulo;
    private String descricao;
    private boolean concluida;


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

    @Override
    public String toString() {
        return "TarefaDTO{" +
                "titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", concluida=" + concluida +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo, descricao, concluida);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TarefaDTO tarefaDTO = (TarefaDTO) o;
        return Objects.equals(titulo, tarefaDTO.titulo) && Objects.equals(descricao, tarefaDTO.descricao) && Objects.equals(concluida, tarefaDTO.concluida);
    }

}
