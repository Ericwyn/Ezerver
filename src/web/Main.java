package web;

/**
 * Created by Ericwyn on 18-5-1.
 */
public class Main {
    public static void main(String[] args) throws WebServerException {
        SimpleHttpServer server = new SimpleHttpServer();
        server.await();
    }

}
