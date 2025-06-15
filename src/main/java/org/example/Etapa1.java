package org.example;

import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import org.example.model.Tarefa;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Etapa1 {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7070);

        //1
        app.get("/hello", ctx -> ctx.result("Hello, Javalin!"));

        //2
        app.get("/status", ctx -> {
            var status = Map.of("status", "ok",
                    "timestamp", Instant.now().toString());
            ctx.json(status);
        });

        //3
        app.post("/echo", ctx -> {
            String msg = ctx.body();
            ctx.status(HttpStatus.CREATED);
            ctx.result("A mensagem é: " + msg);
        });

        //4
        //Busca com parâmetro
        app.get("/saudacao/{nome}", ctx ->{
            String nome = ctx.pathParam("nome");
            String mensagem = "Olá, " + nome + "!";
            var resultado = Map.of("mensagem", mensagem);
            ctx.json(resultado);
        });

        //5
        List<Tarefa> tarefas = new ArrayList<>();

        //Adiciona 3 novas tarefas com POST
        app.post("/tarefas", ctx -> {
            Tarefa novaTarefa = new Tarefa();
            novaTarefa.setId(1);
            novaTarefa.setTitulo("Novo Titulo");
            novaTarefa.setDescricao("Titulo description");
            novaTarefa.setConcluida(true);
            novaTarefa.setDataCriacao(Instant.now());

            Tarefa novaTarefa2 = new Tarefa();
            novaTarefa2.setId(2);
            novaTarefa2.setTitulo("Novo Titulo2");
            novaTarefa2.setDescricao("");
            novaTarefa2.setConcluida(false);
            novaTarefa2.setDataCriacao(Instant.now());

            Tarefa novaTarefa3 = new Tarefa();
            novaTarefa3.setId(3);
            novaTarefa3.setTitulo("Novo Titulo Novamente");
            novaTarefa3.setDescricao("Nova descricao");
            novaTarefa3.setConcluida(true);
            novaTarefa3.setDataCriacao(Instant.now());

            tarefas.add(novaTarefa);
            tarefas.add(novaTarefa2);
            tarefas.add(novaTarefa3);

            System.out.println("Nova tarefa criada: " + novaTarefa);
            ctx.status(HttpStatus.CREATED).json(novaTarefa);
        });

        //6
        //Faz a busca de todos as Tarefas da lista
        app.get("/tarefas", ctx -> ctx.json(tarefas));

        //Faz a busca somento da Tarefa com o mesmo ID passado na rota
        app.get("/tarefas/{id}", ctx -> {
            String id = ctx.pathParam("id");
            Optional<Tarefa> tarefaEncontrada = tarefas.stream()
                    .filter(tarefa -> id.equals(String.valueOf(tarefa.getId())))
                    .findFirst();

            if (tarefaEncontrada.isPresent()) {
                ctx.json(tarefaEncontrada.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND);
                ctx.json(Map.of("Error", "Tarefa com o id: '" + id + "' não foi encontrada."));
            }
        });
    }
}