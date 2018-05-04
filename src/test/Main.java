package test;

import com.ericwyn.ezerver.SimpleHttpServer;

import java.util.Scanner;

/**
 * Created by Ericwyn on 18-5-1.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        SimpleHttpServer server = new SimpleHttpServer();
//        server.setWebRoot("/media/ericwyn/Work/Chaos/meetwhy主页/meetwhy主页_2.0");
        server.setServerPort(8080);
        server.start();
        Scanner in = new Scanner(System.in);
        while (true){
            if (in.nextInt() == 404){
                System.out.println("收到关闭请求");
                server.close();
                break;
            }
        }
    }

}
