package ru.practicum.task_tracker.server;

import java.io.IOException;

public class KVServerStart {

    public static void main(String[] args) throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();
    }
}
