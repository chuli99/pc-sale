package um.prog2.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JSONRequester {

    public JSONRequester() {}

    @Transactional
    public String getJSONFromEndpoint(String baseUrl, String endpointSuffix, String authToken) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + endpointSuffix))
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
}
