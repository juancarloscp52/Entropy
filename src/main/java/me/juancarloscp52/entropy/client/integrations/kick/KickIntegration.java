package me.juancarloscp52.entropy.client.integrations.kick;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import me.juancarloscp52.entropy.client.EntropyClient;
import me.juancarloscp52.entropy.client.EntropyIntegrationsSettings;
import me.juancarloscp52.entropy.client.integrations.Integration;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class KickIntegration implements Integration {
	private WebSocket webSocket;
	private final ObjectMapper mapper = new ObjectMapper();
	private final HttpClient client = HttpClient.newHttpClient();
	private final EntropyIntegrationsSettings settings = EntropyClient.getInstance().integrationsSettings;
	private ScheduledExecutorService pingScheduler;

	public KickIntegration() {
		this.start();
	}

	@Override
	public void start() {
		EntropyClient.LOGGER.info("Starting KickIntegration");

		client.newWebSocketBuilder()
				.buildAsync(URI.create("wss://ws-us2.pusher.com/app/32cbd69e4b950bf97679?protocol=7&client=js&version=8.4.0&flash=false"), new WebSocket.Listener() {
					@Override
					public void onOpen(WebSocket webSocket) {
						EntropyClient.LOGGER.info("Websocket connection established");

						try {
							final ObjectNode root = mapper.createObjectNode();
							root.put("event", "pusher:subscribe");

							final ObjectNode data = mapper.createObjectNode();
							data.put("auth", "");
							data.put("channel", "chatrooms." + settings.kick.chatroomId + ".v2");
							root.set("data", data);

							webSocket.sendText(mapper.writeValueAsString(root), true);
						} catch (JsonProcessingException e) {
							EntropyClient.LOGGER.error("JSON processing error: " + e.getMessage());
						}

						pingScheduler = Executors.newSingleThreadScheduledExecutor();
						pingScheduler.scheduleAtFixedRate(() -> {
							if (!webSocket.isOutputClosed()) {
								webSocket.sendText("{\"event\":\"pusher:ping\",\"data\":{}}", true);
							}
						}, 20, 20, TimeUnit.SECONDS);

						webSocket.request(1);
					}

					@Override
					public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
						final String message = data.toString();

						try {
							final JsonNode root = mapper.readTree(message);
							final String event = root.path("event").asText();

							if ("App\\Events\\ChatMessageEvent".equals(event)) {
								final JsonNode dataNode = mapper.readTree(root.path("data").asText());
								final String onlyDigits = dataNode.path("content").asText().replaceAll("[^0-9]", "");

								if (onlyDigits.length() == 1) {
									final int vote = Integer.parseInt(onlyDigits);

									if (vote >= 1 && vote <= 9) {
										final String username = dataNode.path("sender").path("username").asText();

										if (!username.isEmpty()) {
											EntropyClient.getInstance().clientEventHandler.votingClient.processVote(String.valueOf(vote), username);
										}
									}
								}
							}
						} catch (Exception e) {
							EntropyClient.LOGGER.error("Failed to parse message: " + e.getMessage());
						}

						webSocket.request(1);
						return null;
					}

					@Override
					public void onError(WebSocket webSocket, Throwable error) {
						EntropyClient.LOGGER.error("WebSocket connection error: " + error.getMessage());
					}

					@Override
					public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
						EntropyClient.LOGGER.info("WebSocket closed: {} (code: {})", reason, statusCode);
						return null;
					}
				}).thenAccept(ws -> this.webSocket = ws);
	}

	@Override
	public void stop() {
		EntropyClient.LOGGER.info("Stopping KickIntegration");

		if (pingScheduler != null) {
			pingScheduler.shutdownNow();
		}

		if (webSocket != null) {
			webSocket.abort();
		}

		client.close();
	}

	@Override
	public void sendPoll(int voteID, List<Component> events) {
		EntropyClient.LOGGER.info("Sending poll events to KickIntegration");

		int altOffset = voteID % 2 == 0 ? 4 : 0;
		StringBuilder stringBuilder = new StringBuilder("Current poll:");
		for (int i = 0; i < events.size(); i++)
			stringBuilder.append(String.format("[ %d - %s ] ", 1 + i + altOffset, events.get(i).getString()));

		sendMessage(stringBuilder.toString());
	}

	@Override
	public void sendMessage(String message) {
		EntropyClient.LOGGER.info("Sending message: " + message);

		final Map<String, String> data = Map.of(
				"content", "[Entropy Bot] " + message,
				"type", "message"
		);

		try {
			final String jsonBody = mapper.writeValueAsString(data);
			final HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("https://kick.com/api/v2/messages/send/" + settings.kick.chatroomId))
					.header("Authorization", "Bearer " + settings.kick.clientBearerToken)
					.header("Content-Type", "application/json")
					.header("Accept", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(jsonBody))
					.build();

			client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
					.thenAccept(response -> {
						if (response.statusCode() >= 200 && response.statusCode() < 300) {
							EntropyClient.LOGGER.info("Message sent successfully! Status: " + response.statusCode());
						} else {
							EntropyClient.LOGGER.error("Send error! Status: " + response.statusCode() + " Body: " + response.body());
						}
					})
					.exceptionally(e -> {
						EntropyClient.LOGGER.error("Network error (async send): " + e.getMessage());
						return null;
					});
		} catch (JsonProcessingException e) {
			EntropyClient.LOGGER.error("JSON error: " + e.getMessage());
		}
	}

	@Override
	public int getColor(int alpha) {
		return ARGB.color(alpha,83, 252, 24);
	}
}
