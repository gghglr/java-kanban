package ru.practicum.task_tracker.server;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Scanner;

public class HTTPServerStarter {
    public static void main(String[] args) throws IOException {
        HTTPServer dataServer = new HTTPServer();
        dataServer.start();
        Scanner scanner = new Scanner(System.in);
        String s = scanner.next();
    }
}