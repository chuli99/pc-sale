package ar.edu.um.programacion2.service.mapper;

import ar.edu.um.programacion2.config.Constants;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class JSONRequester {

    public JSONRequester() {}

    @Transactional
    public String getJSONFromEndpoint(String baseUrl, String endpointSuffix, String authToken) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
            .newBuilder(URI.create(baseUrl + endpointSuffix))
            .header("Accept", "application/json")
            .header("Authorization", "Bearer " + authToken)
            .GET()
            .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException(
                    String.format("Solicitud al endpoint %s%s fallida. Motivo: %s", baseUrl, endpointSuffix, response.body())
                );
            }
            return response.body();
        } catch (IOException | InterruptedException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        JSONRequester requester = new JSONRequester();

        String baseUrl = "http://192.168.194.254:8080";
        String endpointSuffix = "/api/dispositivos";
        String authToken =
            "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdWxpYW5jYXN0aWxsbyIsImV4cCI6MTczNjUyMzE5MiwiYXV0aCI6IlJPTEVfVVNFUiIsImlhdCI6MTcyNzg4MzE5Mn0.FbIpN_C-wofDOe7mYMpFGySrNFgmqo4mMFFB8nrcmfBsuzKxT4mzLsmyQkjQmSWF4bN4n0HOuOpYAeBv7QMHaw";

        String resultado = requester.getJSONFromEndpoint(baseUrl, endpointSuffix, authToken);
        System.out.println(resultado);
    }
}
