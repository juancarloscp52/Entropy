package me.juancarloscp52.entropy.client.integrations.youtube;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import net.minecraft.util.Util;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.client.EntropyClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class YoutubeApi {
    public static final Logger LOGGER = LogManager.getLogger();

    private static HttpServer _youtubeServer = null;
    private static SecureRandom _rng = new SecureRandom();
    private static Gson _gson = new Gson();

    public static void authorize(String clientId, String secret, BiConsumer<Boolean, MutableText> callback) {

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
                            var httpClient = HttpClients.createDefault();
                            var httpPost = new HttpPost("https://www.googleapis.com/oauth2/v4/token");
                            var params = new ArrayList<NameValuePair>(5);
                            params.add(new BasicNameValuePair("code", code));
                            params.add(new BasicNameValuePair("redirect_uri", redirectUri));
                            params.add(new BasicNameValuePair("client_id", clientId));
                            params.add(new BasicNameValuePair("code_verifier", codeVerifier));
                            params.add(new BasicNameValuePair("client_secret", secret));
                            params.add(new BasicNameValuePair("scope", "https://www.googleapis.com/auth/youtube"));
                            params.add(new BasicNameValuePair("grant_type", "authorization_code"));
                            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

                            var response = httpClient.execute(httpPost);
                            var entity = response.getEntity();

                            if (entity != null) {
                                try (var inStream = entity.getContent()) {

                                    var body = new String(inStream.readAllBytes());
                                    var gson = new Gson();
                                    var map = gson.fromJson(body, Map.class);
                                    var accessToken = map.get("access_token");
                                    var refreshToken = map.get("refresh_token");
                                    if (accessToken != null && refreshToken != null) {
                                        var integrationsSettings = EntropyClient.getInstance().integrationsSettings;
                                        integrationsSettings.youtubeAccessToken = accessToken.toString();
                                        integrationsSettings.youtubeRefreshToken = refreshToken.toString();
                                        EntropyClient.getInstance().saveSettings();
                                        Entropy.getInstance().saveSettings();

                                        isSuccessful = true;
                                    }
                                }
                            } else
                                throw new NullPointerException("entity was null");
                        } catch (Exception ex) {
                            LOGGER.error(ex);
                        }

                        var res = "<html><body>"
                                + I18n.translate(isSuccessful ? "entropy.options.integrations.youtube.returnToGame"
                                        : "entropy.options.integrations.youtube.error.auth")
                                + "</body></html>";
                        var bytes = res.getBytes();
                        req.getResponseHeaders().set("Content-Type", "text/html");
                        req.sendResponseHeaders(200, bytes.length);
                        var outStream = req.getResponseBody();
                        outStream.write(bytes);
                        outStream.close();

                        callback.accept(isSuccessful, isSuccessful ? null
                                : Text.translatable("entropy.options.integrations.youtube.error.auth"));
                    } catch (IOException ex) {
                        LOGGER.error(ex);

                        callback.accept(false, Text.translatable("entropy.options.integrations.youtube.error.auth"));
                    } finally {
                        stopHttpServer();
                    }
                }
            });
            _youtubeServer.setExecutor(null);
            _youtubeServer.start();

            LOGGER.info("[Youtube authorization] Http server has started. " + _youtubeServer.getAddress().toString());

            var uri = new URIBuilder("https://accounts.google.com/o/oauth2/v2/auth");
            uri.addParameter("response_type", "code");
            uri.addParameter("scope", "https://www.googleapis.com/auth/youtube");
            uri.addParameter("redirect_uri", redirectUri);
            uri.addParameter("client_id", clientId);
            uri.addParameter("state", state);
            uri.addParameter("code_challenge", codeChallenge);
            uri.addParameter("code_challenge_method", "S256");
            uri.addParameter("access_type", "offline");
            var url = uri.build().toString();

            LOGGER.info("[Youtube authorization] Opening browser authorization");
            Util.getOperatingSystem().open(url);

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            stopHttpServer();

            callback.accept(false, Text.translatable("entropy.options.integrations.youtube.error.auth"));
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
            var httpClient = HttpClients.createDefault();
            var httpGet = new HttpGet("https://www.googleapis.com/oauth2/v1/tokeninfo?access_token=" + accessToken);
            var response = httpClient.execute(httpGet);
            return response.getStatusLine().getStatusCode() == 200;
        } catch (Exception ex) {
            LOGGER.error(ex);
            return false;
        }
    }

    public static boolean refreshAccessToken(String clientId, String secret, String refreshToken) {
        LOGGER.info("[Youtube authorization] Trying to refresh token");

        try {
            var httpClient = HttpClients.createDefault();
            var httpPost = new HttpPost("https://www.googleapis.com/oauth2/v4/token");
            var params = new ArrayList<NameValuePair>(4);
            params.add(new BasicNameValuePair("client_id", clientId));
            params.add(new BasicNameValuePair("client_secret", secret));
            params.add(new BasicNameValuePair("refresh_token", refreshToken));
            params.add(new BasicNameValuePair("grant_type", "refresh_token"));
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            var response = httpClient.execute(httpPost);
            var statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() != 200) {
                LOGGER.error("[Youtube authorization] Failed to refresh google access token. "
                        + statusLine.getStatusCode() + " "
                        + statusLine.getReasonPhrase());
                return false;
            }

            var entity = response.getEntity();

            if (entity != null) {
                try (var inStream = entity.getContent()) {

                    var body = new String(inStream.readAllBytes());
                    var gson = new Gson();
                    var map = gson.fromJson(body, Map.class);
                    var accessToken = map.get("access_token");
                    if (accessToken == null)
                        throw new NullPointerException("access token or refresh token was null");

                    var integrationsSettings = EntropyClient.getInstance().integrationsSettings;
                    integrationsSettings.youtubeAccessToken = accessToken.toString();
                    EntropyClient.getInstance().saveSettings();
                    Entropy.getInstance().saveSettings();

                    return true;
                }
            }

            return false;
        } catch (Exception ex) {
            LOGGER.error("[Youtube authorization] Failed to refresh google access token.\n" + ex);
            return false;
        }
    }

    public static LiveBroadcast getLiveBroadcasts(String accessToken) {
        try {
            var httpClient = HttpClients.createDefault();

            var uri = new URIBuilder("https://youtube.googleapis.com/youtube/v3/liveBroadcasts");
            uri.addParameter("part", "snippet");
            uri.addParameter("broadcastType", "all");
            uri.addParameter("broadcastStatus", "active");

            var httpGet = new HttpGet(uri.build());
            httpGet.addHeader("Authorization", "Bearer " + accessToken);
            var response = httpClient.execute(httpGet);
            var statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() != 200) {
                LOGGER.error("[Youtube authorization] Failed to get live broadcasts. "
                        + statusLine.getStatusCode() + " "
                        + statusLine.getReasonPhrase());
                return null;
            }

            var entity = response.getEntity();
            if (entity != null) {
                try (var inStream = entity.getContent()) {

                    var body = new String(inStream.readAllBytes());
                    var data = _gson.fromJson(body, LiveBroadcast.class);
                    return data;
                }
            }
            return null;

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
            var httpClient = HttpClients.createDefault();

            var uri = new URIBuilder("https://youtube.googleapis.com/youtube/v3/liveChat/messages");
            uri.addParameter("part", "id,snippet");
            uri.addParameter("liveChatId", liveChatId);
            if (page != null)
                uri.addParameter("pageToken", page);

            var httpGet = new HttpGet(uri.build());
            httpGet.addHeader("Authorization", "Bearer " + accessToken);
            var response = httpClient.execute(httpGet);
            var statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() != 200) {
                LOGGER.error("[Youtube authorization] Failed to get chat messages. "
                        + statusLine.getStatusCode() + " "
                        + statusLine.getReasonPhrase());
                return null;
            }

            var entity = response.getEntity();
            if (entity != null) {
                try (var inStream = entity.getContent()) {
                    var body = new String(inStream.readAllBytes());
                    var data = _gson.fromJson(body, ChatMessage.class);
                    return data;
                }
            }
            return null;

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
            var httpClient = HttpClients.createDefault();
            var httpPost = new HttpPost("https://youtube.googleapis.com/youtube/v3/liveChat/messages?part=snippet");
            httpPost.addHeader("Authorization", "Bearer " + accessToken);

            var json = """
                    {
                        "snippet":
                        {
                            "liveChatId": "%s",
                            "type": "textMessageEvent",
                            "textMessageDetails":
                            {
                                "messageText": "%s"
                            }
                        }
                    }
                    """.formatted(liveChatId, message);

            httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
            httpClient.execute(httpPost);
        } catch (Exception ex) {
            LOGGER.error(ex);
        }
    }

    private static String generateRandomDataBase64url(int length) {
        var bytes = new byte[length];
        _rng.nextBytes(bytes);
        return base64UrlEncodeNoPadding(bytes);
    }

    private static Map<String, String> queryToMap(String query) {
        if (query == null) {
            return null;
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
        var base64 = new String(Base64.getEncoder().encode(buffer));

        // Converts base64 to base64url.
        base64 = base64.replace("+", "-");
        base64 = base64.replace("/", "_");
        // Strips padding.
        base64 = base64.replace("=", "");

        return base64;
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
