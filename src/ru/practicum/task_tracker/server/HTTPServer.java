package ru.practicum.task_tracker.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.practicum.task_tracker.manager.HistoryManager;
import ru.practicum.task_tracker.manager.Managers;
import ru.practicum.task_tracker.task.Epic;
import ru.practicum.task_tracker.task.Subtask;
import ru.practicum.task_tracker.task.Task;

//КОД РАБОТАЕТ СО ВСЕМИ КЛАССАМИ, У КОТОРЫХ НЕТ LOCALDATETIME, Я НЕ ПОНИМАЮ ПОЧЕМУ Не преобразовывается LocalDateTime
//в

public class HTTPServer {
    public static final int PORT = 8080;

    private final HttpServer server;
    private final HttpTaskManager httpTaskManager;
    public static final String TASK = "task";
    public static final String EPIC = "epic";
    public static final String SUBTASK = "subtask";
    public HTTPServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/data", this::handle);
        httpTaskManager = new HttpTaskManager();
    }

    public void start() {
        System.out.println("Запускаем TASK сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        server.start();
    }

    public void handle(HttpExchange exchange) {
        try {
            String path = extractPath(exchange);
            String key;
            Task task = null;
            Epic epic;
            Subtask subtask;
            switch (exchange.getRequestMethod()) {
                case "GET":
                    key = extractKey(exchange);
                    if (TASK.equals(path)) {
                        task = httpTaskManager.getTask(Long.parseLong(key));
                        sendData(exchange, task);
                    } else if (EPIC.equals(path)) {
                        epic = httpTaskManager.getEpic(Long.parseLong(key));
                        sendDataEpic(exchange, epic);
                    }else if(SUBTASK.equals(path)){
                        subtask = httpTaskManager.getSubtask(Long.parseLong(key));
                       sendDataSubtask(exchange, subtask);
                    }else if("history".equals(path)){
                        sendHistory(exchange, httpTaskManager.getHistoryManager());
                    }
                    else {
                        exchange.sendResponseHeaders(404, 0);
                    }
                    break;
                case "POST":
                    if (TASK.equals(path)) {
                        task = extractData(exchange);
                        httpTaskManager.createTask(task);
                        exchange.sendResponseHeaders(201,  0);
                    } else if (EPIC.equals(path)) {
                         epic = extractDataEpic(exchange);
                       httpTaskManager.createEpic(epic);
                        exchange.sendResponseHeaders(201,  0);
                    } else if (SUBTASK.equals(path)) {
                        subtask = extractDataSubtask(exchange);
                        httpTaskManager.addNewSubtask(subtask);
                        exchange.sendResponseHeaders(201,  0);
                    } else {
                        exchange.sendResponseHeaders(404, 0);
                        return;
                    }
                    exchange.sendResponseHeaders(201,  0);
                    break;
                case "DELETE":
                    task = extractData(exchange);
                    if (TASK.equals(path)) {
                        if(extractKey(exchange) != null){
                            httpTaskManager.deleteTask(Long.parseLong(extractKey(exchange)));
                        }
                        exchange.sendResponseHeaders(201,  0);
                    } else if (EPIC.equals(path)) {
                        if(extractKey(exchange) != null){
                            httpTaskManager.deleteEpic(Long.parseLong(extractKey(exchange)));
                        }
                    } else if (SUBTASK.equals(path)) {
                        if(extractKey(exchange) != null){
                            httpTaskManager.deleteSubtaskById(Long.parseLong(extractKey(exchange)));
                        }else {
                            httpTaskManager.deleteAllSubtasks(Long.parseLong(extractKey(exchange)));
                        }
                    } else {
                        exchange.sendResponseHeaders(404, 0);
                        return;
                    }
                    exchange.sendResponseHeaders(200,  0);
                    break;
                default:
                    exchange.sendResponseHeaders(404, 3);
            }
        } catch (IOException e) {
           throw new RuntimeException("ошибка");
        } finally {
            exchange.close();
        }
    }

    private void sendHistory(HttpExchange exchange, HistoryManager historyManager) throws IOException {
        if (historyManager.getHistory() == null || historyManager.getHistory().isEmpty()) {
            exchange.sendResponseHeaders(404, 0);
            return;
        }
        String json = new Gson().toJson(historyManager.getHistory());
        byte[] resp = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
    }

    private Task extractData(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        return new Gson().fromJson(body, Task.class);
    }

    private Epic extractDataEpic(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        return new Gson().fromJson(body, Epic.class);
    }

    private Subtask extractDataSubtask(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        return new Gson().fromJson(body, Subtask.class);
    }
    private String extractKey(HttpExchange exchange) {
        if(exchange.getRequestURI().getQuery().substring(4) != null){
            return exchange.getRequestURI().getQuery().substring(4);
        }
        else {
            return null;
        }
    }

    private void sendData(HttpExchange exchange, Task task) throws IOException {
        if (task == null) {
            exchange.sendResponseHeaders(404, 0);
            return;
        }

        String json = new Gson().toJson(task);
        byte[] resp = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
    }


    private void sendDataEpic(HttpExchange exchange, Epic epic) throws IOException {
        if (epic == null) {
            exchange.sendResponseHeaders(404, 0);
            return;
        }

        String json = new Gson().toJson(epic);
        byte[] resp = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
    }

    private void sendDataSubtask(HttpExchange exchange, Subtask subtask) throws IOException {
        if (subtask == null) {
            exchange.sendResponseHeaders(404, 0);
            return;
        }

        String json = new Gson().toJson(subtask);
        byte[] resp = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
    }

    private String extractPath(HttpExchange exchange) {
        return exchange.getRequestURI().getPath().substring(6);
    }
}