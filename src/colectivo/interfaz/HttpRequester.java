package colectivo.interfaz;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jxmapviewer.viewer.GeoPosition;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class HttpRequester {

	/**
     * Realiza una solicitud a la API de enrutamiento de OSRM para obtener la ruta más rápida.
     *
     * @param coordinates Coordenadas de inicio y destino en formato "longitud,latitud;longitud,latitud".
     * @return Lista de GeoPosition que representa la ruta más rápida.
     */
	public static List<GeoPosition> getFastestRoute(String coordinates) {
		try {
			// URL de la api a la que hacer la request (se encuentra en la documentación
			String apiUrl = String.format("http://router.project-osrm.org/route/v1/driving/%s?geometries=geojson&overview=full&steps=true", coordinates);
	        URL url = new URL(apiUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			// Hacer request a la url
	        int responseCode = connection.getResponseCode();
	        // Verificar que la request sea exitosa
	        if (responseCode == HttpURLConnection.HTTP_OK) {
		        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		        StringBuilder response = new StringBuilder();
		        String line;
		        
		        // Leer la respuesta (viene en formato JSON)
		        while ((line = reader.readLine()) != null) {
		            response.append(line);
		        }
		        reader.close();
		        
		        // Parsear la respuesta a una lista de coordenadas
		        JsonElement jsonElement = JsonParser.parseString(response.toString());
		        JsonObject jsonObject = jsonElement.getAsJsonObject();
		        
		        JsonArray routesArray = jsonObject.getAsJsonArray("routes");

		        // Verifica si hay al menos una ruta en la respuesta
		        if (!routesArray.isEmpty()) {
		            JsonObject routeObject = routesArray.get(0).getAsJsonObject();
		            JsonObject geometryObject = routeObject.getAsJsonObject("geometry");
		            JsonArray coordinatesArray = geometryObject.getAsJsonArray("coordinates");

		            // Inicializa una lista para almacenar las coordenadas
		            List<GeoPosition> coordenadas = new ArrayList<>();

		            // Recorre las coordenadas y agrégalas a la lista
		            for (JsonElement coordinateElement : coordinatesArray) {
		                JsonArray coordinateArray = coordinateElement.getAsJsonArray();
		                double longitude = coordinateArray.get(0).getAsDouble();
		                double latitude = coordinateArray.get(1).getAsDouble();
		                coordenadas.add(new GeoPosition(latitude, longitude));
		            }
		            // Devolver lista de coordenadas parseadas
					return coordenadas;
		        }
		        return null;
        	}
	        return null;
		} catch (Exception e) {
			return null;
		}
	}

}