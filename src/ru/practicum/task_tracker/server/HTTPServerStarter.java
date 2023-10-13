package ru.practicum.task_tracker.server;

import java.io.IOException;

public class HTTPServerStarter {
    public static void main(String[] args) throws IOException {
        HTTPServer dataServer = new HTTPServer();
        dataServer.start();
    }
}