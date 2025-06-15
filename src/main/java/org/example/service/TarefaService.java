package org.example.service;

import io.javalin.http.NotFoundResponse;
import org.example.dto.TarefaDTO;
import org.example.model.Tarefa;
import org.example.repository.TarefaRepository;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.List;

public class TarefaService {
    private final TarefaRepository repository;

    public TarefaService (TarefaRepository repository){
        this.repository = repository;
    }

    public List<Tarefa> listarTarefas(){
        return repository.findAll();
    }

    public Tarefa buscarPorId(int id){
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundResponse("Tarefa não encontrada!"));
    }

    public Tarefa salvarTarefa(TarefaDTO tarefaDTO){
        Tarefa tarefa = construirTarefaVindoDoDTO(tarefaDTO);
        var id = repository.insert(tarefa);
        return buscarPorId(id);
    }

    @NotNull
    private Tarefa construirTarefaVindoDoDTO(TarefaDTO tarefaDTO) {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(tarefaDTO.getTitulo());
        tarefa.setDescricao(tarefaDTO.getDescricao());
        tarefa.setConcluida(tarefaDTO.isConcluida());
        tarefa.setDataCriacao(Instant.now());

        return tarefa;
    }

}
