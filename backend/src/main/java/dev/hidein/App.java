package dev.hidein;

import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.ContentType;

public class App {
    public static void main(String[] args) {
        var port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        var injector = new PdfInjector();

        var allowedOrigins = System.getenv().getOrDefault("CORS_ORIGINS", "http://localhost:5173,http://localhost:4173");

        var app = Javalin.create(config -> {
            config.http.maxRequestSize = 10_000_000L; // 10 MB
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(rule -> {
                    for (var origin : allowedOrigins.split(",")) {
                        rule.allowHost(origin.trim());
                    }
                });
            });
        });

        app.get("/health", ctx -> ctx.result("ok"));

        app.post("/api/inject", ctx -> {
            var uploadedFile = ctx.uploadedFile("file");
            var text = ctx.formParam("text");

            if (uploadedFile == null) {
                throw new BadRequestResponse("Missing 'file' parameter");
            }
            if (text == null || text.isBlank()) {
                throw new BadRequestResponse("Missing or empty 'text' parameter");
            }

            byte[] pdfBytes;
            try (var is = uploadedFile.content()) {
                pdfBytes = is.readAllBytes();
            }

            byte[] result;
            try {
                result = injector.inject(pdfBytes, text);
            } catch (IllegalArgumentException e) {
                throw new BadRequestResponse(e.getMessage());
            } catch (Exception e) {
                throw new BadRequestResponse("Invalid or corrupted PDF");
            }

            ctx.contentType(ContentType.APPLICATION_PDF);
            ctx.header("Content-Disposition", "attachment; filename=\"injected.pdf\"");
            ctx.result(result);
        });

        app.start(port);
    }
}
