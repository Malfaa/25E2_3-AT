package org.example.controller;

import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import org.example.dto.TarefaDTO;
import org.example.service.TarefaService;

import java.time.Instant;

public class TarefaController {

    public static final String TAREFA_PATH = "/tarefas";
    public static final String TAREFAS_BY_ID_PATH = TAREFA_PATH + "/{id}";

    private final TarefaService service;

    public TarefaController (TarefaService service){
        this.service = service;
    }

    public void rotas(Javalin app){
        app.get(TAREFA_PATH, ctx -> ctx.json(service.listarTarefas()));

        app.get(TAREFAS_BY_ID_PATH, ctx -> {
            var id = parseIdParametro(ctx);
            ctx.json(service.buscarPorId(id));
        });

        app.post(TAREFA_PATH, ctx -> {
           TarefaDTO dto = ctx.bodyValidator(TarefaDTO.class)
                   .check(t -> t.getTitulo() != null && !t.getTitulo().isBlank(), "Titulo é obrigatório")
                   .get();
           var novaTarefa = service.salvarTarefa(dto);
           ctx.status(201).json(novaTarefa);
        });

        app.get("/status", ctx -> {
            String jsonString = String.format("{\"4status\":\"%d\",\"timestamp\":\"%s\"}",
                    ctx.statusCode(),
                    Instant.now().toString());
            ctx.result(jsonString);
        });

    }

    private int parseIdParametro( Context ctx) {
        try {
            return Integer.parseInt(ctx.pathParam("id"));
        }catch (Exception e){
            throw new BadRequestResponse("ID Inválido. Utilize um número inteiro.");
        }
    }

}
