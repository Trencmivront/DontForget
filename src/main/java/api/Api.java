package main.java.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// This general file is used to send API requests to local API endpoints.
// It is the best solution for such project like mine.
public class Api{

	private static final Logger logger = LoggerFactory.getLogger(Api.class.getName());

	private final HttpClient client = HttpClient.newHttpClient();
	private final HeadUrl headUrl = new HeadUrl();
	private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

	// It will only get the api part of url: /api/entity/action/value
	public String get(String end) {
		String url = headUrl.generateApi(end);
		logger.info("Executing GET {} for url: {}", this.getClass(), url);
		HttpRequest req = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.GET()
				.build();
		try {
			String response = client.send(req, HttpResponse.BodyHandlers.ofString()).body();

			if (response == null || response.isEmpty()) {
				throw new EntityNotFoundException("Entity not found in " + end);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	public String get(String end, Long id) {
		String url = headUrl.generateApi(end, id);
		logger.info("Executing GET {} for url: {}", this.getClass(), url);
		HttpRequest req = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.GET()
				.build();
		try {
			String response = client.send(req, HttpResponse.BodyHandlers.ofString()).body();

			if (response == null || response.isEmpty()) {
				throw new EntityNotFoundException("Entity not found in " + end + " with id " + id);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public int post(String end, String body) {
		if (!isJson(body)) {
			logger.error("Body is not valid JSON: {}", body);
			throw new IllegalArgumentException("Body is not valid JSON: " + body);
		}
		String url = headUrl.generateApi(end);
		logger.info("Executing POST {} for url: {}", this.getClass(), url);

		HttpRequest req = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(body))
				.build();
		try {
			HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
			int statusCode = res.statusCode();
			if (statusCode >= 400) {
				throw new RuntimeException("POST request to " + end + " failed with status code " + statusCode);
			}
			return statusCode;
		} catch (Exception e) {
			e.printStackTrace();
			return 500;
		}
	}

	public int put(String end, String body, Long id) {
		if (!isJson(body)) {
			logger.error("Body is not valid JSON: {}", body);
			throw new IllegalArgumentException("Body is not valid JSON: " + body);
		}
		String url = headUrl.generateApi(end, id);
		logger.info("Executing PUT {} for url: {}", this.getClass(), url);

		HttpRequest req = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.header("Content-Type", "application/json")
				.PUT(HttpRequest.BodyPublishers.ofString(body))
				.build();
		try {
			HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
			int statusCode = res.statusCode();
			if (statusCode == 404) {
				throw new EntityNotFoundException("Entity not found for PUT in " + end + " with id " + id);
			} else if (statusCode >= 400) {
				throw new RuntimeException("PUT request to " + end + " with id " + id + " failed with status code " + statusCode);
			}
			return statusCode;
		} catch (Exception e) {
			e.printStackTrace();
			return 500;
		}
	}

	public int delete(String end, Long id) {
		String url = headUrl.generateApi(end, id);
		logger.info("Executing DELETE {} for url: {}", this.getClass(), url);

		HttpRequest req = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.DELETE()
				.build();
		try {
			HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
			int statusCode = res.statusCode();
			if (statusCode == 404) {
				throw new EntityNotFoundException("Entity not found for DELETE in " + end + " with id " + id);
			} else if (statusCode >= 400) {
				throw new RuntimeException("DELETE request to " + end + " with id " + id + " failed with status code " + statusCode);
			}
			return statusCode;
		} catch (Exception e) {
			e.printStackTrace();
			return 500;
		}
	}

	private boolean isJson(String str) {
		if (str == null || str.trim().isEmpty()) {
			return false;
		}
		// if it can't read it, then it is not json
		try {
			objectMapper.readTree(str);
			return true;
		} catch (Exception _) {
			return false;
		}
	}
}
