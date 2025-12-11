package tp3.dao;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonWriter;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JsonClient {
	
	public String get(String url) throws IOException{
		HttpURLConnection conn = createConnection(url, "GET");
		return readResponse(conn);
	}
	
	public String post(String url, String jsonBody) throws IOException{
		HttpURLConnection conn = createConnection(url, "POST");
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		
		writeRequestBody(conn, jsonBody);
		return readResponse(conn);
	}
	
	public String put(String url, String jsonBody) throws IOException{
		HttpURLConnection conn = createConnection(url, "PUT");
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		
		writeRequestBody(conn, jsonBody);
		return readResponse(conn);
	}
	
	public String delete(String url) throws IOException{
		HttpURLConnection conn = createConnection(url, "DELETE");
		return readResponse(conn);
	}
	
	private HttpURLConnection createConnection(String urlString, String method) throws IOException{
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(method);
		conn.setRequestProperty("Accept", "application/json");
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(5000);
		return conn;
	}
	
	private void writeRequestBody(HttpURLConnection conn, String jsonBody) throws IOException{
		try(OutputStream os = conn.getOutputStream();
	             OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
			writer.write(jsonBody);
			writer.flush();
		}
	}
	
	private String readResponse (HttpURLConnection conn) throws IOException{
		int responseCode = conn.getResponseCode();
		
		InputStream inputStream;
		if(responseCode >= 200 && responseCode < 300) {
			inputStream = conn.getInputStream();
		}else {
			inputStream = conn.getErrorStream();
		}
		
		if(inputStream == null) {
			throw new IOException("Pas de reponce au Serveur");
		}
		
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))){
			StringBuilder response = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null) {
				response.append(line);
			}
			return response.toString();
		}
		
		
	}
	
}

















