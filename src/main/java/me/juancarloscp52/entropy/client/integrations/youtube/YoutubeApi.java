package me.juancarloscp52.entropy.client.integrations.youtube;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.client.EntropyClient;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class YoutubeApi {
    public static final Logger LOGGER = LogManager.getLogger();

    private static HttpServer _youtubeServer = null;
    private static SecureRandom _rng = new SecureRandom();
    private static Gson _gson = new Gson();
    private static final HttpClient _httpClient = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    public static void authorize(String clientId, String secret, BiConsumer<Boolean, MutableComponent> callback) {

        stopHttpServer();

        try {
            _youtubeServer = HttpServer.create(new InetSocketAddress(0), 0);

            var redirectUri = "http://localhost:" + _youtubeServer.getAddress().getPort() + "/";
            var state = generateRandomDataBase64url(32);
            var codeVerifier = generateRandomDataBase64url(32);
            var codeChallenge = base64UrlEncodeNoPadding(
                    MessageDigest.getInstance("SHA-256").digest(codeVerifier.getBytes(StandardCharsets.US_ASCII)));

            _youtubeServer.createContext("/", new HttpHandler() {

                @Override
                public void handle(HttpExchange req) throws IOException {
                    try {
                        var query = queryToMap(req.getRequestURI().getQuery());
                        var code = query.get("code");
                        var incomingState = query.get("state");

                        LOGGER.info("[Youtube authorization] Exchanging code for tokens");

                        boolean isSuccessful = false;
                        try {
                            if (code == null || incomingState == null)
                                throw new NullPointerException("code or state was null");

                            Map<String, String> params = new HashMap<>();
                            params.put("code", code);
                            params.put("redirect_uri", redirectUri);
                            params.put("client_id", clientId);
                            params.put("code_verifier", codeVerifier);
                            params.put("client_secret", secret);
                            params.put("scope", "https://www.googleapis.com/auth/youtube");
                            params.put("grant_type", "authorization_code");

                            HttpRequest request = HttpRequest.newBuilder()
                                    .uri(URI.create("https://www.googleapis.com/oauth2/v4/token"))
                                    .header("Content-Type", "application/x-www-form-urlencoded")
                                    .POST(buildFormData(params))
                                    .build();

                            HttpResponse<String> response = _httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                            if (response.statusCode() == 200) {
                                var body = response.body();
                                var map = _gson.fromJson(body, Map.class);
                                var accessToken = map.get("access_token");
                                var refreshToken = map.get("refresh_token");
                                if (accessToken != null && refreshToken != null) {
                                    var integrationsSettings = EntropyClient.getInstance().integrationsSettings;
                                    integrationsSettings.youtube.accessToken = accessToken.toString();
                                    integrationsSettings.youtube.refreshToken = refreshToken.toString();
                                    EntropyClient.getInstance().saveSettings();
                                    Entropy.getInstance().saveSettings();

                                    isSuccessful = true;
                                }
                            } else {
                                throw new IOException("Unexpected response code: " + response.statusCode());
                            }
                        } catch (Exception ex) {
                            LOGGER.error(ex);
                        }

                        var res = "<html><body>"
                                + I18n.get(isSuccessful ? "entropy.options.integrations.youtube.returnToGame"
                                : "entropy.options.integrations.youtube.error.auth")
                                + "</body></html>";
                        var bytes = res.getBytes(StandardCharsets.UTF_8);
                        req.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                        req.sendResponseHeaders(200, bytes.length);
                        try (var outStream = req.getResponseBody()) {
                            outStream.write(bytes);
                        }

                        callback.accept(isSuccessful, isSuccessful ? null
                                : Component.translatable("entropy.options.integrations.youtube.error.auth"));
                    } catch (Exception ex) {
                        LOGGER.error(ex);

                        callback.accept(false, Component.translatable("entropy.options.integrations.youtube.error.auth"));
                    } finally {
                        stopHttpServer();
                    }
                }
            });
            _youtubeServer.setExecutor(null);
            _youtubeServer.start();

            LOGGER.info("[Youtube authorization] Http server has started. " + _youtubeServer.getAddress().toString());

            String authUrl = "https://accounts.google.com/o/oauth2/v2/auth" +
                    "?response_type=code" +
                    "&scope=https://www.googleapis.com/auth/youtube" +
                    "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
                    "&client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
                    "&state=" + state +
                    "&code_challenge=" + codeChallenge +
                    "&code_challenge_method=S256" +
                    "&access_type=offline";

            LOGGER.info("[Youtube authorization] Opening browser authorization");
            Util.getPlatform().openUri(authUrl);

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            stopHttpServer();

            callback.accept(false, Component.translatable("entropy.options.integrations.youtube.error.auth"));
        }
    }

    public static void stopHttpServer() {
        if (_youtubeServer != null) {
            _youtubeServer.stop(0);
            _youtubeServer = null;
            LOGGER.info("[Youtube authorization] Http server has stopped");
        }
    }

    public static boolean validateAccessToken(String accessToken) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://www.googleapis.com/oauth2/v1/tokeninfo?access_token=" + accessToken))
                    .GET()
                    .build();
            HttpResponse<Void> response = _httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            return response.statusCode() == 200;
        } catch (Exception ex) {
            LOGGER.error(ex);
            return false;
        }
    }

    public static boolean refreshAccessToken(String clientId, String secret, String refreshToken) {
        LOGGER.info("[Youtube authorization] Trying to refresh token");

        try {
            Map<String, String> params = new HashMap<>();
            params.put("client_id", clientId);
            params.put("client_secret", secret);
            params.put("refresh_token", refreshToken);
            params.put("grant_type", "refresh_token");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://www.googleapis.com/oauth2/v4/token"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(buildFormData(params))
                    .build();

            HttpResponse<String> response = _httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                LOGGER.error("[Youtube authorization] Failed to refresh google access token. "
                        + response.statusCode() + " "
                        + response.body());
                return false;
            }

            var body = response.body();
            var map = _gson.fromJson(body, Map.class);
            var accessToken = map.get("access_token");
            if (accessToken == null)
                throw new NullPointerException("access token was null");

            var integrationsSettings = EntropyClient.getInstance().integrationsSettings;
            integrationsSettings.youtube.accessToken = accessToken.toString();
            EntropyClient.getInstance().saveSettings();
            Entropy.getInstance().saveSettings();

            return true;
        } catch (Exception ex) {
            LOGGER.error("[Youtube authorization] Failed to refresh google access token.\n" + ex);
            return false;
        }
    }

    public static LiveBroadcast getLiveBroadcasts(String accessToken) {
        try {
            String url = "https://youtube.googleapis.com/youtube/v3/liveBroadcasts?part=snippet&broadcastType=all&broadcastStatus=active";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + accessToken)
                    .GET()
                    .build();

            HttpResponse<String> response = _httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                LOGGER.error("[Youtube authorization] Failed to get live broadcasts. "
                        + response.statusCode() + " "
                        + response.body());
                return null;
            }

            return _gson.fromJson(response.body(), LiveBroadcast.class);

        } catch (Exception ex) {
            LOGGER.error(ex);
            return null;
        }
    }

    public static ChatMessage getChatMessages(String accessToken, String liveChatId) {
        return getChatMessages(accessToken, liveChatId, null);
    }

    public static ChatMessage getChatMessages(String accessToken, String liveChatId, String page) {
        try {
            String url = "https://youtube.googleapis.com/youtube/v3/liveChat/messages?part=id,snippet&liveChatId=" + liveChatId;
            if (page != null)
                url += "&pageToken=" + page;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + accessToken)
                    .GET()
                    .build();

            HttpResponse<String> response = _httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                LOGGER.error("[Youtube authorization] Failed to get chat messages. "
                        + response.statusCode() + " "
                        + response.body());
                return null;
            }

            return _gson.fromJson(response.body(), ChatMessage.class);

        } catch (Exception ex) {
            LOGGER.error(ex);
            return null;
        }
    }

    public static String getChatMessagesLastPage(String accessToken, String liveChatId) {
        var chatMessage = getChatMessages(accessToken, liveChatId);
        if (chatMessage == null)
            return null;
        while (chatMessage.items.length != 0) {
            chatMessage = getChatMessages(accessToken, liveChatId, chatMessage.nextPageToken);
            if (chatMessage == null)
                return null;
        }
        return chatMessage.nextPageToken;
    }

    public static void sendChatMessage(String accessToken, String liveChatId, String message) {
        try {
            var json = _gson.toJson(Map.of("snippet", Map.of(
                    "liveChatId", liveChatId,
                    "type", "textMessageEvent",
                    "textMessageDetails", Map.of("messageText", message)
            )));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://youtube.googleapis.com/youtube/v3/liveChat/messages?part=snippet"))
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            _httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        } catch (Exception ex) {
            LOGGER.error(ex);
        }
    }

    private static HttpRequest.BodyPublisher buildFormData(Map<String, String> data) {
        var res = data.entrySet().stream()
                .map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
        return HttpRequest.BodyPublishers.ofString(res);
    }

    private static String generateRandomDataBase64url(int length) {
        var bytes = new byte[length];
        _rng.nextBytes(bytes);
        return base64UrlEncodeNoPadding(bytes);
    }

    private static Map<String, String> queryToMap(String query) {
        if (query == null) {
            return new HashMap<>();
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }

    private static String base64UrlEncodeNoPadding(byte[] buffer) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buffer);
    }
}

class ChatMessage {
    public int pollingIntervalMillis;
    public String nextPageToken;
    public ChatMessageItem[] items;
}

class ChatMessageItem {
    public String id;
    public ChatMessageSnippet snippet;
}

class ChatMessageSnippet {
    public String authorChannelId;
    public String displayMessage;
}

class LiveBroadcast {
    public String nextPageToken;
    public LiveBroadcastItem[] items;
}

class LiveBroadcastItem {
    public LiveBroadcastSnippet snippet;
}

class LiveBroadcastSnippet {
    public String title;
    public String channelId;
    public String liveChatId;
}
