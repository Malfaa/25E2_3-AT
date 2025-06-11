package org.example;

import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import org.example.model.Tarefa;

import java.time.Instant;
import java.util.*;

public class Main {

    public static Javalin getApp(){
        return Javalin.create();
    }
    public static void main(String[] args) {

        //1
        Javalin app = getApp().start(7000);
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
        app.get("/saudacao/{nome}", ctx ->{
            String nome = ctx.pathParam("nome");
            String mensagem = "Olá, " + nome + "!";
            var resultado = Map.of("mensagem", mensagem);
            ctx.json(resultado);
        });


        //5
        List<Tarefa> tarefas = new ArrayList<>();
        app.post("/tarefa", ctx -> {
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
            novaTarefa2.setId(3);
            novaTarefa2.setTitulo("Novo Titulo Novamente");
            novaTarefa2.setDescricao("Nova descricao");
            novaTarefa2.setConcluida(true);
            novaTarefa2.setDataCriacao(Instant.now());

            tarefas.add(novaTarefa);
            tarefas.add(novaTarefa2);
            tarefas.add(novaTarefa3);

            System.out.println("Nova tarefa criada: " + novaTarefa);
            ctx.status(HttpStatus.CREATED).json(novaTarefa);
        });

        //6
        app.get("/tarefas", ctx -> ctx.json(tarefas));

        app.get("/tarefa/{id}", ctx -> {
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