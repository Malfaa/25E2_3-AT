package org.example.controller;

import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import org.example.dto.TarefaDTO;
import org.example.service.TarefaService;

public class TarefaController {
    public static final String TAREFA_PATH = "/tarefa";
    public static final String TAREFA_BUSCA_PATH = "/tarefa" + "/{id}";

    public static void registrarRotas(Javalin app) {
        TarefaService service = new TarefaService();

        app.get(TAREFA_PATH, ctx -> {
            ctx.json(service.listarTarefa());
        });

        app.get(TAREFA_BUSCA_PATH, ctx -> {
            var id = parseIdParam(ctx);
            ctx.json(service.buscarPorId(id));
        });

        app.post(TAREFA_PATH, ctx -> {
            TarefaDTO dto = ctx.bodyValidator(TarefaDTO.class)
                    .check(r -> r.getTitulo() != null && !r.getTitulo().isBlank(), "Titulo é obrigatório")
                    .check(r -> r.getDescricao() != null && !r.getDescricao().isBlank(), "Descrição é obrigatório")
                    .get();
            var novaTarefa = service.salvar(dto);
            ctx.status(201).json(novaTarefa);
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
