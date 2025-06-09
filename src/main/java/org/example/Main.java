package org.example;

import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {

        //1
        Javalin app = Javalin.create().start(7000);
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
        List<Map<String, String>> usuarios = new ArrayList<>();

        app.post("/usuario", ctx -> {
            var novoUsuario = ctx.bodyAsClass(Map.class);
            usuarios.add(novoUsuario);
            System.out.println("Novo usuário criado: " + novoUsuario);
            ctx.status(HttpStatus.CREATED).json(novoUsuario);
        });

        //6
        app.get("/usuarios", ctx -> ctx.json(usuarios));

        app.get("/usuario/{email}", ctx -> {
            String email = ctx.pathParam("email");
            Optional<Map<String, String>> usuarioEncontrado = usuarios.stream()
                    .filter(usuario -> email.equals(usuario.get("email")))
                    .findFirst();

            if (usuarioEncontrado.isPresent()) {
                ctx.json(usuarioEncontrado.get());
            } else {
                ctx.status(HttpStatus.NOT_FOUND);
                ctx.json(Map.of("Error", "Usuário com o e-mail '" + email + "' não foi encontrado."));

            }
        });
    }
}