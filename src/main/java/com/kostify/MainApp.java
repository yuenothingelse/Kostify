package com.kostify;

import com.kostify.bridge.FrontendBridge;
import com.kostify.service.KostService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MainApp extends Application {

    private HttpServer server;
    private String appUrl;

    private HttpServer startEmbeddedServer(FrontendBridge bridge) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);

        httpServer.createContext("/api/", exchange -> {
            try {
                handleApi(exchange, bridge);
            } catch (Exception e) {
                sendJson(exchange, 500, "{\"ok\":false,\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
            }
        });

        httpServer.createContext("/", exchange -> handleStatic(exchange));
        httpServer.start();
        return httpServer;
    }

    private void handleStatic(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path == null || path.equals("/") || path.equals("/index.html")) {
            path = "/ui/index.html";
        } else if (!path.startsWith("/ui/")) {
            path = "/ui" + (path.startsWith("/") ? path : "/" + path);
        }

        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                byte[] notFound = "Not Found".getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(404, notFound.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(notFound);
                }
                return;
            }
            byte[] bytes = is.readAllBytes();
            exchange.getResponseHeaders().set("Content-Type", contentTypeFor(path) + "; charset=utf-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    private void handleApi(HttpExchange exchange, FrontendBridge bridge) throws IOException {
        Map<String, String> q = parseQuery(exchange.getRequestURI().getRawQuery());
        String action = exchange.getRequestURI().getPath().replace("/api/", "");
        String resp;

        switch (action) {
            case "login" -> resp = bridge.login(q.getOrDefault("email", ""), q.getOrDefault("password", ""));
            case "logout" -> resp = bridge.logout();
            case "dashboard" -> resp = bridge.getDashboardData();
            case "kamar/list" -> resp = bridge.getDaftarKamar(q.getOrDefault("status", "Semua"), q.getOrDefault("keyword", ""));
            case "kamar/tambah" -> resp = bridge.tambahKamar(q.getOrDefault("nomor", ""), q.getOrDefault("tipe", ""), q.getOrDefault("harga", "0"));
            case "kamar/ubah" -> resp = bridge.ubahKamar(
                    Integer.parseInt(q.getOrDefault("id", "0")),
                    q.getOrDefault("nomor", ""),
                    q.getOrDefault("tipe", ""),
                    q.getOrDefault("harga", "0"),
                    q.getOrDefault("status", "Kosong")
            );
            case "kamar/hapus" -> resp = bridge.hapusKamar(Integer.parseInt(q.getOrDefault("id", "0")));
            case "penghuni/list" -> resp = bridge.getDaftarPenghuni(q.getOrDefault("status", "Semua"), q.getOrDefault("keyword", ""));
            case "penghuni/tambah" -> resp = bridge.tambahPenghuni(
                    q.getOrDefault("nama", ""),
                    q.getOrDefault("noHp", ""),
                    q.getOrDefault("alamat", ""),
                    Integer.parseInt(q.getOrDefault("idKamar", "0")),
                    q.getOrDefault("tanggalMasuk", ""),
                    q.getOrDefault("tanggalKeluar", "")
            );
            case "penghuni/ubah" -> resp = bridge.ubahPenghuni(
                    Integer.parseInt(q.getOrDefault("id", "0")),
                    q.getOrDefault("nama", ""),
                    q.getOrDefault("noHp", ""),
                    q.getOrDefault("alamat", ""),
                    Integer.parseInt(q.getOrDefault("idKamar", "0")),
                    q.getOrDefault("tanggalMasuk", ""),
                    q.getOrDefault("tanggalKeluar", ""),
                    q.getOrDefault("statusAktif", "Aktif")
            );
            case "penghuni/keluar" -> resp = bridge.keluarPenghuni(
                    Integer.parseInt(q.getOrDefault("id", "0")),
                    q.getOrDefault("tanggalKeluar", "")
            );
            case "penghuni/hapus" -> resp = bridge.hapusPenghuni(Integer.parseInt(q.getOrDefault("id", "0")));
            case "pembayaran/list" -> resp = bridge.getDaftarPembayaran(q.getOrDefault("status", "Semua"), q.getOrDefault("keyword", ""));
            case "pembayaran/bayar" -> resp = bridge.bayarTagihan(
                    Integer.parseInt(q.getOrDefault("id", "0")),
                    q.getOrDefault("tanggalBayar", "")
            );
            default -> resp = "{\"ok\":false,\"error\":\"Endpoint tidak dikenal: " + escapeJson(action) + "\"}";
        }

        sendJson(exchange, 200, resp);
    }

    private void sendJson(HttpExchange exchange, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private Map<String, String> parseQuery(String raw) {
        Map<String, String> map = new HashMap<>();
        if (raw == null || raw.isBlank()) return map;
        for (String pair : raw.split("&")) {
            int idx = pair.indexOf('=');
            if (idx > 0) {
                String k = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
                String v = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
                map.put(k, v);
            }
        }
        return map;
    }

    private String contentTypeFor(String path) {
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".svg")) return "image/svg+xml";
        return "text/html";
    }

    private String escapeJson(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        KostService kostService = new KostService();
        FrontendBridge bridge = new FrontendBridge(kostService);
        this.server = startEmbeddedServer(bridge);
        int port = this.server.getAddress().getPort();
        this.appUrl = "http://127.0.0.1:" + port + "/";

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI(this.appUrl));
        }

        Label title = new Label("Kostify Server Aktif");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Label info = new Label(this.appUrl);
        Button btnOpen = new Button("Buka di Browser");
        btnOpen.setOnAction(e -> {
            try {
                Desktop.getDesktop().browse(new URI(this.appUrl));
                } catch (Exception ignored) {
            }
        });

        VBox box = new VBox(12, title, info, btnOpen);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-padding: 20;");

        primaryStage.setTitle("Kostify — JavaFX & MySQL Runner");
        primaryStage.setScene(new Scene(box, 320, 160));
        primaryStage.show();
    }

    @Override
    public void stop() {
        if (this.server != null) {
            this.server.stop(0);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
