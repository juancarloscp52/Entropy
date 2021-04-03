package me.juancarloscp52.entropy.client.integrations;

public interface Integration {

    void start();

    void stop();

    void sendChatMessage(String message);

    int getColor();

}
