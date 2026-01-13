package dev.hidein;

import io.javalin.Javalin;

public class App {
    public static void main(String[] args) {
        var port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));

        var app = Javalin.create(config -> {
            config.http.maxRequestSize = 10_000_000L; // 10 MB
        });

        app.get("/health", ctx -> ctx.result("ok"));

        app.start(port);
    }
}
