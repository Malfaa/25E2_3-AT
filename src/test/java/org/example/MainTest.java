package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.*;
import org.example.controller.TarefaController;
import org.example.dto.TarefaDTO;
import org.example.model.Tarefa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    ObjectMapper mapper;

    Tarefa tarefa1 = new Tarefa();

    @BeforeEach
    void setup() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());


        tarefa1.setId(1);
        tarefa1.setTitulo("Titulo 1");
        tarefa1.setDescricao("Primeira descricao");
        tarefa1.setConcluida(true);
        tarefa1.setDataCriacao(Instant.now());
    }

    //1
    @Test
    void validacao_validarEndpointHello_retornoOK() {
        Javalin app = Javalin.create()
                .get("/hello", ctx -> {
                    ctx.result("Hello, Javalin!");
                });

        JavalinTest.test(app, (server, client) -> {
            Response response = client.get("/hello");
            assertEquals(200, response.code());
            assertEquals("Hello, Javalin!", response.body().string());

        });
    }


    //2
    @Test
    void simulacao_verificandoStatusEEnviandoNovoItem_retornaCreated() {
        Javalin app = Javalin.create();
        TarefaController.rotas(app);

        JavalinTest.test(app, (server, client) -> {
            TarefaDTO novaTarefa = new TarefaDTO();
            novaTarefa.setTitulo("Teste");
            novaTarefa.setDescricao("Descrição da tarefa");
            novaTarefa.setConcluida(false);

            String jsonTarefa = mapper.writeValueAsString(novaTarefa);
            Response response = client.post("/tarefa", jsonTarefa);

            assertEquals(201, response.code());
            assertNotNull(jsonTarefa);
        });
    }

    //3
    @Test
    void path_buscaUtilizandoPathParam_retornoOk() {
        Javalin app = Javalin.create();
        TarefaController.rotas(app);

        JavalinTest.test(app, (server, client) -> {
            TarefaDTO novaTarefa = new TarefaDTO();
            novaTarefa.setTitulo("Teste");
            novaTarefa.setDescricao("Descrição da tarefa");
            novaTarefa.setConcluida(false);

            String jsonTarefa = mapper.writeValueAsString(novaTarefa);
            Response postResponse = client.post("/tarefa", jsonTarefa);

            //-----------------------------------------------------
            String responseBody = postResponse.body().string();
            Tarefa tarefaReponse = mapper.readValue(responseBody, Tarefa.class);
            int tarefaId = tarefaReponse.getId();
            Response getResponse = client.get("/tarefa/"+ tarefaId);

            assertEquals(200, getResponse.code());
            assertNotNull(getResponse.body());
        });
    }

    //4
    @Test
    void lista_verificamListaJsonSemValoresVazios_retornoOk (){
        Javalin app = Javalin.create();
        TarefaController.rotas(app);

        JavalinTest.test(app, (server, client) -> {
            TarefaDTO novaTarefa = new TarefaDTO();
            novaTarefa.setTitulo("Teste");
            novaTarefa.setDescricao("Descrição da tarefa");
            novaTarefa.setConcluida(false);

            String jsonTarefa = mapper.writeValueAsString(novaTarefa);
            client.post("/tarefa", jsonTarefa);

            var response = client.get("/tarefa");

            assertNotNull(response.body());
        });
    }
}