package ar.edu.um.programacion2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class JSONDeserializer {

    public static JSONRequester jsonRequester = new JSONRequester();
    public static ObjectMapper mapper = new ObjectMapper();

    public static String dispositivosFromJson() throws IOException {
        String jsonResponse = jsonRequester.getJSONFromEndpoint(
            "http://192.168.194.254:8080",
            "/api/dispositivos",
            "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdWxpYW5jYXN0aWxsbyIsImV4cCI6MTczNjUyMzE5MiwiYXV0aCI6IlJPTEVfVVNFUiIsImlhdCI6MTcyNzg4MzE5Mn0.FbIpN_C-wofDOe7mYMpFGySrNFgmqo4mMFFB8nrcmfBsuzKxT4mzLsmyQkjQmSWF4bN4n0HOuOpYAeBv7QMHaw"
        );
        ArrayNode originalArray = (ArrayNode) mapper.readTree(jsonResponse);
        ArrayNode resultArray = mapper.createArrayNode();
        Set<Integer> processedIds = new HashSet<>();

        for (int i = 0; i < originalArray.size(); i++) {
            ObjectNode device = (ObjectNode) originalArray.get(i);
            int id = device.get("id").asInt();

            if (!processedIds.contains(id)) {
                processedIds.add(id);
                ObjectNode simpleDevice = mapper.createObjectNode();
                simpleDevice.put("id", id);
                simpleDevice.put("codigo", device.get("codigo").asText());
                simpleDevice.put("nombre", device.get("nombre").asText());
                simpleDevice.put("descripcion", device.get("descripcion").asText());
                simpleDevice.put("precioBase", device.get("precioBase").asDouble());
                simpleDevice.put("moneda", device.get("moneda").asText());
                resultArray.add(simpleDevice);
            }
        }
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resultArray);
    }

    public static String caracteristicasFromJson() throws IOException {
        String jsonResponse = jsonRequester.getJSONFromEndpoint(
            "http://192.168.194.254:8080",
            "/api/dispositivos",
            "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdWxpYW5jYXN0aWxsbyIsImV4cCI6MTczNjUyMzE5MiwiYXV0aCI6IlJPTEVfVVNFUiIsImlhdCI6MTcyNzg4MzE5Mn0.FbIpN_C-wofDOe7mYMpFGySrNFgmqo4mMFFB8nrcmfBsuzKxT4mzLsmyQkjQmSWF4bN4n0HOuOpYAeBv7QMHaw"
        );
        ArrayNode originalArray = (ArrayNode) mapper.readTree(jsonResponse);
        ArrayNode resultArray = mapper.createArrayNode();
        Set<Integer> processedIds = new HashSet<>();

        for (int i = 0; i < originalArray.size(); i++) {
            ObjectNode device = (ObjectNode) originalArray.get(i);
            ArrayNode personalizaciones = (ArrayNode) device.get("personalizaciones");

            for (int j = 0; j < personalizaciones.size(); j++) {
                ObjectNode personalizacion = (ObjectNode) personalizaciones.get(j);
                int personalizacionId = personalizacion.get("id").asInt();

                if (!processedIds.contains(personalizacionId)) {
                    processedIds.add(personalizacionId);
                    personalizacion.put("dispositivo", device.get("id").asInt());
                    personalizacion.remove("opciones");
                    resultArray.add(personalizacion);
                }
            }
        }
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resultArray);
    }

    public static String opcionesFromJson() throws IOException {
        String jsonResponse = jsonRequester.getJSONFromEndpoint(
            "http://192.168.194.254:8080",
            "/api/dispositivos",
            "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdWxpYW5jYXN0aWxsbyIsImV4cCI6MTczNjUyMzE5MiwiYXV0aCI6IlJPTEVfVVNFUiIsImlhdCI6MTcyNzg4MzE5Mn0.FbIpN_C-wofDOe7mYMpFGySrNFgmqo4mMFFB8nrcmfBsuzKxT4mzLsmyQkjQmSWF4bN4n0HOuOpYAeBv7QMHaw"
        );
        ArrayNode originalArray = (ArrayNode) mapper.readTree(jsonResponse);
        ArrayNode resultArray = mapper.createArrayNode();
        Set<Integer> processedIds = new HashSet<>();

        for (int i = 0; i < originalArray.size(); i++) {
            ObjectNode device = (ObjectNode) originalArray.get(i);
            ArrayNode personalizaciones = (ArrayNode) device.get("personalizaciones");

            for (int j = 0; j < personalizaciones.size(); j++) {
                ObjectNode personalizacion = (ObjectNode) personalizaciones.get(j);
                ArrayNode opciones = (ArrayNode) personalizacion.get("opciones");

                for (int k = 0; k < opciones.size(); k++) {
                    ObjectNode opcion = (ObjectNode) opciones.get(k);
                    int opcionId = opcion.get("id").asInt();

                    if (!processedIds.contains(opcionId)) {
                        processedIds.add(opcionId);
                        opcion.put("personalizacion", personalizacion.get("id").asInt());
                        resultArray.add(opcion);
                    }
                }
            }
        }
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resultArray);
    }

    public static String adicionalesFromJson() throws IOException {
        String jsonResponse = jsonRequester.getJSONFromEndpoint(
            "http://192.168.194.254:8080",
            "/api/dispositivos",
            "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdWxpYW5jYXN0aWxsbyIsImV4cCI6MTczNjUyMzE5MiwiYXV0aCI6IlJPTEVfVVNFUiIsImlhdCI6MTcyNzg4MzE5Mn0.FbIpN_C-wofDOe7mYMpFGySrNFgmqo4mMFFB8nrcmfBsuzKxT4mzLsmyQkjQmSWF4bN4n0HOuOpYAeBv7QMHaw"
        );
        ArrayNode originalArray = (ArrayNode) mapper.readTree(jsonResponse);
        ArrayNode resultArray = mapper.createArrayNode();
        Set<Integer> processedIds = new HashSet<>();

        for (int i = 0; i < originalArray.size(); i++) {
            ObjectNode device = (ObjectNode) originalArray.get(i);
            ArrayNode adicionales = (ArrayNode) device.get("adicionales");

            for (int j = 0; j < adicionales.size(); j++) {
                ObjectNode adicional = (ObjectNode) adicionales.get(j);
                int adicionalId = adicional.get("id").asInt();

                if (!processedIds.contains(adicionalId)) {
                    processedIds.add(adicionalId);
                    adicional.put("dispositivo", device.get("id").asInt());
                    resultArray.add(adicional);
                }
            }
        }

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resultArray);
    }
}
