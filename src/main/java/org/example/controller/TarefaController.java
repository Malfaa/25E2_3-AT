package org.example.controller;

import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import org.example.dto.TarefaDTO;
import org.example.service.TarefaService;

import java.time.Instant;

public class TarefaController {
    public static final String TAREFA_PATH = "/tarefa";
    public static final String TAREFA_BUSCA_PATH = "/tarefa/{id}";

    public static void rotas(Javalin app) {
        TarefaService service = new TarefaService();

        app.get(TAREFA_PATH, ctx -> {
            ctx.json(service.listarTarefa());
            System.out.println(ctx.body());
        });

        app.get(TAREFA_BUSCA_PATH, ctx -> {
            var id = parseIdParam(ctx);
            ctx.json(service.buscarPorId(id));
            System.out.println(ctx.body());
        });

        app.post(TAREFA_PATH, ctx -> { //{"titulo":"Esté Titylo","descricao":"Esta é a descricao","concluida":"true"}
            try {
                String requestBody = ctx.body();
                if (requestBody == null || requestBody.isBlank()) {
                    throw new BadRequestResponse("Request body is empty");
                }

                TarefaDTO dto = ctx.bodyValidator(TarefaDTO.class)
                        .check(r -> r.getTitulo() != null && !r.getTitulo().isBlank(), "Titulo é obrigatório")
                        .get();

                var novaTarefa = service.salvar(dto);
                ctx.status(201).json(novaTarefa);
                System.out.println(ctx.body());
            }catch (BadRequestResponse e){
                ctx.status(400).result(e.getMessage());
            } catch (Exception e) {
                ctx.status(500).result("Internal server error");
            }
        });

        app.get("/status", ctx -> {
            String jsonString = String.format("{\"status\":\"%d\",\"timestamp\":\"%s\"}",
                    ctx.statusCode(),
                    Instant.now().toString());
            ctx.result(jsonString);
        });
    }

    private static int parseIdParam(Context ctx) {
        try {
            return Integer.parseInt(ctx.pathParam("id"));
        } catch (NumberFormatException e) {
            throw new BadRequestResponse("ID inválido. Use um numero inteiro!");
        }
    }
}