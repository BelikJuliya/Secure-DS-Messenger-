package android.example.securedsmessenger;

import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class Server {

    WebSocket client;

    public void connect() {
        URI address;
        try {
            address = new URI("ws://35.214.3.133:8881");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;

        }

        client = new WebSocketClient(address) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                //При подлючении
                Log.i("SERVER", "Connection to server is open");
            }

            @Override
            public void onMessage(String message) {
                //при сообщении с сервера
                Log.i("SERVER", "Connection to server is open" + message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                //При закрытиии соединения
                Log.i("SERVER", "Connection closed");
            }

            @Override
            public void onError(Exception ex) {
                //При ошибке
                Log.i("SERVER", "Error occurred");
            }
        };
        connect();


    }
}
