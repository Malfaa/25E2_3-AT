CREATE TABLE tarefas (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         titulo VARCHAR(255) NOT NULL,
                         descricao TEXT,
                         concluida BOOLEAN NOT NULL DEFAULT FALSE,
                         data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO tarefas (titulo, descricao, concluida) VALUES
                                                       ('Estudar Javalin e testes unitários', 'Revisar os conceitos de injeção de dependência e mocks.', FALSE),
                                                       ('Fazer compras no supermercado', 'Comprar leite, pão, ovos e frutas.', FALSE),
                                                       ('Terminar o relatório do projeto', 'Concluir a seção de análise de dados e enviar para revisão.', TRUE),
                                                       ('Ligar para o cliente', NULL, FALSE),
                                                       ('Limpar o código do controller', 'Refatorar os handlers para torná-los mais limpos e eficientes.', TRUE)