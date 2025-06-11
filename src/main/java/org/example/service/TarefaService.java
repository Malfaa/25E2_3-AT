package org.example.service;

import io.javalin.http.NotFoundResponse;
import org.example.dto.TarefaDTO;
import org.example.model.Tarefa;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TarefaService {
    private List<Tarefa> tarefasList = new ArrayList<>();


    public TarefaService(){
        TarefaDTO mock = new TarefaDTO();
        mock.setTitulo("teste");
        mock.setDescricao("descricao teste");
        mock.setConcluida(true);
        salvar(mock);
    }

    public TarefaService (List<Tarefa> lista) {
        this.tarefasList = lista;
    }

    public List<Tarefa> listarTarefa(){
        return tarefasList;
    }

    public Tarefa buscarPorId(int id) {
        return tarefasList.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundResponse("Rótulo não encontrado!"));
    }

    public Tarefa salvar(TarefaDTO dto) {
        Tarefa tarefa = construirTarefaUsandoDTO(dto);
        tarefasList.add(tarefa);
        return buscarPorId(tarefa.getId());
    }

    @NotNull
    private static Tarefa construirTarefaUsandoDTO(TarefaDTO dto) {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(dto.getTitulo());
        tarefa.setDescricao(dto.getDescricao());
        tarefa.setConcluida(dto.getConcluida());
        tarefa.setDataCriacao(Instant.now());

        return tarefa;
    }
}
