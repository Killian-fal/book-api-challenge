package fr.killiandev.book.scraping.support;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.function.Consumer;

public class TestAirbnbServer implements AutoCloseable {
    private final HttpServer server;
    private final int port;

    private TestAirbnbServer(HttpServer server) {
        this.server = server;
        this.port = server.getAddress().getPort();
    }

    public static TestAirbnbServer start(int status, Consumer<Headers> headersCustomizer, byte[] body)
            throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        server.createContext("/", exchange -> {
            headersCustomizer.accept(exchange.getResponseHeaders());
            exchange.sendResponseHeaders(status, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        });
        server.start();
        return new TestAirbnbServer(server);
    }

    public String getAirbnbUrl() {
        return "http://localhost:%d/airbnb/listing".formatted(port);
    }

    @Override
    public void close() {
        server.stop(0);
    }
}
