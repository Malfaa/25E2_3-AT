package org.example.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.TarefaDTO;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpUtil {

    public static HttpURLConnection getConnection(String stringUrl, String reqMetodo) throws IOException, URISyntaxException {
        URL url = new URI(stringUrl).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod(reqMetodo);
        connection.setRequestProperty("Accept", "application/json");

        if(reqMetodo.equals("POST") || reqMetodo.equals("PUT")) {
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        }

        return connection;
    }

    public static void gravacaoDados(HttpURLConnection connection, TarefaDTO dto) throws IOException {
        ObjectMapper jsonMap = new ObjectMapper();
        var json = jsonMap.writeValueAsString(dto);

        try(OutputStream os = connection.getOutputStream()) {
            byte[] input = json.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }
    }
}
