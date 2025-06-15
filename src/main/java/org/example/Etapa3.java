package org.example;

import io.javalin.Javalin;
import org.example.configuration.DbConfiguration;
import org.example.controller.TarefaController;
import org.example.dto.TarefaDTO;
import org.example.repository.TarefaRepository;
import org.example.service.TarefaService;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.util.*;

import static org.example.utils.HttpUtil.getConnection;
import static org.example.utils.HttpUtil.gravacaoDados;


public class Etapa3 {

    static final String URL_TAREFA_PATH = "http://localhost:7070/tarefas";

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7070);
        var tarefaService = instanciarTodasAsClasses();

        TarefaController tc = new TarefaController(tarefaService);
        tc.rotas(app);

        System.out.println("Exercicio 1 ao 4!");
        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.println("Digite o número de um exercício:");
            System.out.println("1. Exercicio 1");
            System.out.println("2. Exercicio 2");
            System.out.println("3. Exercicio 3");
            System.out.println("4. Exercicio 4");

            System.out.println("0. Sair");
            System.out.println("Opção:");

            try {
                int escolha = scan.nextInt();
                switch (escolha) {
                    case 1:
                        clientPost();
                        break;
                    case 2:
                        clientGet();
                        break;
                    case 3:
                        buscaComParam();
                        break;
                    case 4:
                        buscaStatus();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Opção inválida, tente novamente");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //a
    //Faz o envio utilizando um POST, insere no banco de dados
    public static void clientPost() throws IOException, URISyntaxException {
        HttpURLConnection conn = getConnection(URL_TAREFA_PATH, "POST");
        try {
            conn.setDoOutput(true);
            String jsonResposta = "";

            if (conn.getRequestMethod().equals("POST")){
                TarefaDTO novaTarefa = new TarefaDTO();
                novaTarefa.setTitulo("Titulo do HTTPURLCONNECTION");
                novaTarefa.setDescricao("Descrição mostra que foi gravado pelo HTTPURLCONNECTION");
                novaTarefa.setConcluida(true);

                jsonResposta = gravacaoDados(conn, novaTarefa);
            }

            var responseCode = conn.getResponseCode();
            System.out.println("-- 1. Client Post --");
            System.out.println("Código da resposta: " + responseCode);
            System.out.println("Resposta do post: " + jsonResposta);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            conn.disconnect();
        }
    }

    //b
    //Faz a busca de todos os itens utilizando o GET
    public static void clientGet() throws IOException, URISyntaxException{
        HttpURLConnection connection = getConnection(URL_TAREFA_PATH, "GET");

        try{
            StringBuilder strBuilder = new StringBuilder();
            BufferedReader reader;

            var responseCode = connection.getResponseCode();

            if( responseCode >= HttpURLConnection.HTTP_OK && responseCode <= HttpURLConnection.HTTP_MULT_CHOICE) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            String input;
            while ((input = reader.readLine()) != null) {
                strBuilder.append(input);
            }

            System.out.println("-- 2. Client Get --");
            System.out.println("Código da resposta: " + responseCode);
            System.out.println("Resposta: " + strBuilder);
            reader.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            connection.disconnect();
        }
    }

    //c
    //Faz uma busca utilizando parâmetro no GET
    public static void buscaComParam() throws IOException, URISyntaxException {
        System.out.println("Digite o ID desejado: ");
        Scanner scan = new Scanner(System.in);
        int id = scan.nextInt();

        String urlComId = String.format("http://localhost:7070/tarefas/%d", id);

        HttpURLConnection connection = getConnection(urlComId, "GET");

        StringBuilder strBuilder = new StringBuilder();
        BufferedReader reader;

        var responseCode = connection.getResponseCode();

        if( responseCode >= HttpURLConnection.HTTP_OK && responseCode <= HttpURLConnection.HTTP_MULT_CHOICE) {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        }else {
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }

        String input;
        while ((input = reader.readLine()) != null) {
            strBuilder.append(input);
        }

        System.out.println("-- 3. Client Get Param --");
        System.out.println("Código da resposta: " + responseCode);
        System.out.println("Resposta: " + strBuilder);

        reader.close();

        System.out.println(connection.getResponseCode());
    }


    //d
    //Faz uma busca em um endpoint status
    public static void buscaStatus() throws IOException, URISyntaxException {
        HttpURLConnection connection = getConnection("http://localhost:7070/status", "GET");

        BufferedReader reader;
        StringBuilder strBuilder = new StringBuilder();

        var response = connection.getResponseCode();
        if(response >= HttpURLConnection.HTTP_OK && response < HttpURLConnection.HTTP_MULT_CHOICE){
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        }else{
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }
        String input;
        while ((input = reader.readLine()) != null){
            strBuilder.append(input);
        }

        System.out.println(connection.getResponseCode());
        System.out.println(strBuilder);
    }

    @NotNull
    private static TarefaService instanciarTodasAsClasses() {
        var dbConfig = DbConfiguration.createJdbi();
        var tarefaRepository = new TarefaRepository(dbConfig);
        return new TarefaService(tarefaRepository);
    }
}
