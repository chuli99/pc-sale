package ar.edu.um.programacion2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PostEntitiesService {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public PostEntitiesService() {
        this.restTemplate = new RestTemplate();
    }

    public void postAllEntities() {
        try {
            // 1. Post de dispositivos
            String dispositivosJson = JSONDeserializer.dispositivosFromJson();
            postEntity(dispositivosJson, "http://localhost:8080/api/dispositivos");

            // 2. Post de características
            String caracteristicasJson = JSONDeserializer.caracteristicasFromJson();
            postEntity(caracteristicasJson, "http://localhost:8080/api/caracteristicas");

            // 3. Post de opciones
            String opcionesJson = JSONDeserializer.opcionesFromJson();
            postEntity(opcionesJson, "http://localhost:8080/api/opciones");

            // 4. Post de adicionales
            String adicionalesJson = JSONDeserializer.adicionalesFromJson();
            postEntity(adicionalesJson, "http://localhost:8080/api/adicionales");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para enviar el JSON mediante POST al servidor
    private void postEntity(String json, String url) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<String> request = new HttpEntity<>(json, headers);

            // Realizar el POST
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            // Verificar el estado de la respuesta
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Entidad posteada exitosamente a " + url);
            } else {
                System.out.println("Error al postear la entidad a " + url + ": " + response.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
