package org.example.repository;

import org.example.model.Tarefa;
import org.jdbi.v3.core.Jdbi;
import java.util.List;
import java.util.Optional;


public class TarefaRepository {
    private final Jdbi dataSource;

    public TarefaRepository (Jdbi jdbi) {
        this.dataSource = jdbi;
    }

    public List<Tarefa> findAll() {
        return dataSource.withHandle(handle ->
                handle.createQuery("SELECT * FROM tarefas")
                        .mapToBean(Tarefa.class)
                        .list());
    }

    public Optional<Tarefa> findById(int id) {
        return dataSource.withHandle(handle ->
                handle.createQuery("SELECT * FROM tarefas WHERE id = :id")
                        .bind("id", id)
                        .mapToBean(Tarefa.class)
                        .findOne());
    }

    public int insert(Tarefa t) {
        return dataSource.withHandle(handle ->
                handle.createUpdate("INSERT INTO tarefas (titulo, descricao, concluida, data_criacao) " +
                                "VALUES (:titulo, :descricao, :concluida, :dataCriacao)")
                        .bindBean(t)
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class)
                        .one());
    }

}
