package colectivo.interfaz;

import com.google.gson.Gson;
import com.google.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Geocoder {

	/**
	 * Clase que proporciona métodos para obtener coordenadas geográficas utilizando la API Geocoding de Google Maps.
	 */
	
    private static final String API_KEY = "";

    /**
     * Obtiene las coordenadas geográficas de una dirección dada.
     *
     * @param street Dirección de la calle.
     * @param number Número de la dirección.
     * @return Coordenadas geográficas (LatLng) de la dirección proporcionada.
     * @throws IOException Si ocurre un error durante la solicitud a la API Geocoding.
     */
    public static LatLng getCoordinates(String street, String number) throws IOException {
        URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=" + street + "+" + number + "+Puerto+Madryn,+Chubut,+Argentina&key=" + API_KEY);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Error getting coordinates: " + responseCode);
        }

        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
        Gson gson = new Gson();
        Response response = gson.fromJson(reader, Response.class);

        if (response.results.length == 0) {
            return null;
        }
        
        System.out.println(response);
        return new LatLng();
    }

    public static class Response {

        public Location[] results;
    }

    public static class Location {

        public Geometry geometry;
    }

    public static class Geometry {

        public Location location;
    }

    public static void main(String[] args) throws IOException {
        LatLng coordinates = Geocoder.getCoordinates("1 De Marzo", "405");

        if (coordinates != null) {
            System.out.println("Latitud: " + coordinates.lat);
            System.out.println("Longitud: " + coordinates.lng);
        } else {
            System.out.println("No se encontraron coordenadas para la parada 1 De Marzo, número 405.");
        }
    }
}
