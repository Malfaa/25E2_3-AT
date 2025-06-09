package org.example;

import io.javalin.Javalin;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @BeforeAll
    static void setup(){
        Javalin app = Javalin.create().start(7000);
    }

    @Test
    void main() {
    }
}