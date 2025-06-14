package org.example.service;

import io.javalin.http.NotFoundResponse;
import org.example.dto.TarefaDTO;
import org.example.model.Tarefa;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TarefaService {
    private static final List<Tarefa> tarefasList = new ArrayList<>();


    public TarefaService(){
        TarefaDTO mock = new TarefaDTO();
        mock.setTitulo("teste");
        mock.setDescricao("descricao teste");
        mock.setConcluida(true);
        salvar(mock);
    }

    public List<Tarefa> listarTarefa(){
        return tarefasList;
    }

    public Tarefa buscarPorId(int id) {
        return tarefasList.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundResponse("Tarefa não encontrada!"));
    }

    public Tarefa salvar(TarefaDTO dto) {
        Tarefa tarefa = construirTarefaUsandoDTO(dto);
        tarefasList.add(tarefa);
        return buscarPorId(tarefa.getId());
    }

    @NotNull
    private static Tarefa construirTarefaUsandoDTO(TarefaDTO dto) {
        Tarefa tarefa = new Tarefa();
        tarefa.setId( (!tarefasList.isEmpty()) ? tarefasList.getLast().getId()+1 : 1 );
        tarefa.setTitulo(dto.getTitulo());
        tarefa.setDescricao(dto.getDescricao());
        tarefa.setConcluida(dto.getConcluida());
        tarefa.setDataCriacao(Instant.now());

        return tarefa;
    }
}
