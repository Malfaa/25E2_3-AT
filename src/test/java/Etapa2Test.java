import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.http.NotFoundResponse;
import io.javalin.testtools.JavalinTest;
import okhttp3.Response;
import org.example.controller.TarefaController;
import org.example.dto.TarefaDTO;
import org.example.model.Tarefa;
import org.example.service.TarefaService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static utils.TarefaFixture.*;

@ExtendWith(MockitoExtension.class)
public class Etapa2Test {

    @Mock
    private TarefaService tarefaService;

    @InjectMocks
    private TarefaController tarefaController;

    private ObjectMapper mapper;

    @BeforeEach
    void setup(){
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }


    //1
    @Test
    void validacao_validarEndpointHello_retornoOK(){
        Javalin app = Javalin.create().get("/hello", ctx -> ctx.result("Hello, Javalin!"));

        JavalinTest.test(app, (server, client) -> {
            Response response = client.get("/hello");
            assertEquals(200, response.code());
            assert response.body() != null;
            assertEquals("Hello, Javalin!", response.body().string());

        });
    }

    //2
    @Test
    void simulacao_verificandoStatusEEnviandoNovoItem_retornaCreated(){
        var dto = criarDTOMock("Titulo DTO Mock", "Descricao DTO Mock", true);
        var tarefa = buildTarefa();

        when(tarefaService.salvarTarefa(dto)).thenReturn(tarefa);

        JavalinTest.test(criarAppComRotas(), (server, client) -> {
            String json = mapper.writeValueAsString(dto);
            var resposta = client.post("/tarefas", json);

            assertEquals(201, resposta.code());

            assert resposta.body() != null;
            var respostaTarefa = mapper.readValue(
                    resposta.body().string(),
                    Tarefa.class
            );

            assertEquals(ID_TAREFA, respostaTarefa.getId());
            assertEquals(TITULO_TAREFA, respostaTarefa.getTitulo());
            assertEquals(DESCRICAO_TAREFA, respostaTarefa.getDescricao());
        });

        verify(tarefaService).salvarTarefa(dto);
    }

    //3
    @Test
    void path_buscaUtilizandoPathParam_retornoOk(){
        Tarefa tarefa = buildTarefa();

        when(tarefaService.buscarPorId(ID_TAREFA)).thenReturn(tarefa);

        JavalinTest.test(criarAppComRotas(),(server, client) -> {
            var resposta = client.get("/tarefas/" + ID_TAREFA);
            assertEquals(200, resposta.code());

            assert resposta.body() != null;
            Tarefa respotaTarefa = mapper.readValue(
                    resposta.body().string(),
                    Tarefa.class
            );

            assertEquals(ID_TAREFA, respotaTarefa.getId());
            assertEquals(TITULO_TAREFA, "Tarefa 1");
        });

        verify(tarefaService).buscarPorId(ID_TAREFA);
    }

    //4
    @Test
    void lista_verificaListaJsonSemValoresVazios_retornoOk(){
        Tarefa tarefaUm = criarTarefaMock(1, "PRIMEIRA TAREFA", "Descricao da Tarefa UM", true);
        Tarefa tarefaDois = criarTarefaMock(2, "SEGUNDA TAREFA", "Descricao da Tarefa DOIS", true);

        List<Tarefa> tarefas = List.of(tarefaUm, tarefaDois);
        when(tarefaService.listarTarefas()).thenReturn(tarefas);

        JavalinTest.test(criarAppComRotas(), (server, client) -> {
            var resposta = client.get("/tarefas");
            assertEquals(200, resposta.code());

            assertNotNull(resposta.body());
            List<Tarefa> respotaTarefas = mapper.readValue(
                    resposta.body().string(),
                    new TypeReference<List<Tarefa>>() {
                    }
            );

            assertEquals(2, respotaTarefas.size());
            assertEquals("PRIMEIRA TAREFA", respotaTarefas.get(0).getTitulo());
            assertEquals("SEGUNDA TAREFA", respotaTarefas.get(1).getTitulo());
        });
    }

    @Test
    void titulo_verificaSeIdInvalido_retorna400() {
        JavalinTest.test(criarAppComRotas(), (server, client) -> {
            var response = client.get("/tarefas/teste");
            assertEquals(400, response.code());

            assert response.body() != null;
            assertFalse(response.body().string().contains("ID inválido. Use um numero inteiro!"));
        });

        verifyNoInteractions(tarefaService);
    }

    @Test
    void deveRetornar404AoBuscarRotuloInexistente() {
        when(tarefaService.buscarPorId(ID_TAREFA)).thenThrow(new NotFoundResponse("Tarefa não encontrada"));

        JavalinTest.test(criarAppComRotas(), (server, client) -> {
            var response = client.get("/tarefas/" + ID_TAREFA);
            assertEquals(404, response.code());
        });

        verify(tarefaService).buscarPorId(ID_TAREFA);
    }



    @NotNull
    private Javalin criarAppComRotas() {
        Javalin app = Javalin.create();
        tarefaController.rotas(app);
        return app;
    }

    @NotNull
    private static Tarefa criarTarefaMock(int id, String titulo, String descricao, boolean concluida) {
        Tarefa tarefa = new Tarefa();
        tarefa.setId(id);
        tarefa.setTitulo(titulo);
        tarefa.setDescricao(descricao);
        tarefa.setConcluida(concluida);

        return tarefa;
    }
    @NotNull
    private static TarefaDTO criarDTOMock(String titulo, String descricao, boolean concluida) {
        TarefaDTO tarefaDTO = new TarefaDTO();
        tarefaDTO.setTitulo(titulo);
        tarefaDTO.setDescricao(descricao);
        tarefaDTO.setConcluida(concluida);

        return tarefaDTO;
    }
}
