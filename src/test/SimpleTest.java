package test;

import com.ericwyn.ezerver.SimpleHttpServer;
import com.ericwyn.ezerver.expection.WebServerException;

import java.io.IOException;

/**
 * Created by Ericwyn on 18-5-4.
 */
public class SimpleTest {
    public static void main(String[] args) throws IOException, WebServerException {
        SimpleHttpServer server = new SimpleHttpServer();
        server.simpleServerTest();
    }
}
