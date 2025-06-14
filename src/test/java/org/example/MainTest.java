package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.*;
import org.example.controller.TarefaController;
import org.example.dto.TarefaDTO;
import org.example.model.Tarefa;
import org.example.service.TarefaService;
import org.example.utils.TarefaFixture;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.NotExtensible;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MainTest {
    ObjectMapper mapper;

    @Mock
    private TarefaService tarefaService;

    @InjectMocks
    private TarefaController tarefaController;


    @BeforeEach
    void setup() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

    }

    @NotNull
    private Javalin rotasParaApp(){
        Javalin app = Javalin.create();
        tarefaController.rotas(app);
        return app;
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

        //verify();
    }


    //2
    @Test
    void simulacao_verificandoStatusEEnviandoNovoItem_retornaCreated() {//TESTASR
        TarefaDTO novaTarefaDTO = TarefaFixture.buildTarefaDTO();

        Tarefa novaTarefa = TarefaFixture.buildTarefa();

        when(tarefaService.salvar(novaTarefaDTO)).thenReturn(novaTarefa);

        JavalinTest.test(rotasParaApp(), (server, client) -> {


            String jsonTarefa = mapper.writeValueAsString(novaTarefa);
            Response response = client.post("/tarefa", jsonTarefa);

            assertEquals(201, response.code());
            assertNotNull(jsonTarefa);
        });

        verify(tarefaService).salvar(any());
    }

    //3
    @Test
    void path_buscaUtilizandoPathParam_retornoOk() {


        JavalinTest.test(rotasParaApp(), (server, client) -> {
            TarefaDTO novaTarefa = TarefaFixture.buildTarefaDTO();

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


        JavalinTest.test(rotasParaApp(), (server, client) -> {
            TarefaDTO novaTarefaDTO = TarefaFixture.buildTarefaDTO();

            String jsonTarefa = mapper.writeValueAsString(novaTarefaDTO);
            client.post("/tarefa", jsonTarefa);

            var response = client.get("/tarefa");

            assertNotNull(response.body());
            assertEquals(novaTarefaDTO.getTitulo(), mapper.readValue(response.body().string(), TarefaDTO.class).getTitulo());
        });
    }
}