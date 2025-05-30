package hr.javafx.businesstraveltracker.repository;

import hr.javafx.businesstraveltracker.exception.ApiRequestException;

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

/**
 * Klasa koja provodi slanje zahtjeva API-u.
 */
public class HttpRequestHandler {

    private final String baseUri;

    private final HttpClient client;

    public HttpRequestHandler() {
        Properties props = new Properties();

        try (FileReader reader = new FileReader("app.properties")) {
            props.load(reader);
        } catch (IOException e) {
            throw new ApiRequestException("app.properties file not found");
        }

        baseUri = props.getProperty("api.uri");

        client = HttpClient.newHttpClient();
    }

    /**
     * Šalje HTTP zahtjev.
     * @param uriPath putanja zathtjeva
     * @param method metoda
     * @param body tijelo zahtjeva
     * @return rezultat zahtjeva
     */
    public String makeHttpRequest(String uriPath, String method, String body) {
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(baseUri + uriPath));

            switch (method) {
                case "GET":
                    requestBuilder.GET();
                    break;
                case "POST":
                    requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body));
                    break;
                case "PUT":
                    requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(body));
                    break;
                case "DELETE":
                    requestBuilder.DELETE();
                    break;
                default:
                    throw new ApiRequestException("Invalid method: " + method);
            }

            HttpRequest request = requestBuilder.header("Content-Type", "application/json").build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() < 200 || response.statusCode() > 299) {
                throw new ApiRequestException("Error while trying to interact with API. Status: " + response.statusCode());
            }
            return response.body();
        }catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApiRequestException(e);
        }
    }

    /**
     * Šalje HTTP zahtjev
     * @param urlPart putanja zahtjeva
     * @param method metoda
     * @return rezultat zahtjeva
     */
    public String makeHttpRequest(String urlPart, String method){
        return makeHttpRequest(urlPart, method, null);
    }
}
