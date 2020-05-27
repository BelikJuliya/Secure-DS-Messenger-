package android.example.securedsmessenger;

import android.util.Log;
import android.util.Pair;

import androidx.core.util.Consumer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    WebSocketClient mClient;
    private Consumer<Pair<String, String>> onMessageReceived;
    Map<Long, String> names = new ConcurrentHashMap<>();
    private MainActivity activity;


    public Server(Consumer<Pair<String, String>> onMessageReceived, MainActivity activity) {
        this.onMessageReceived = onMessageReceived;
        this.activity = activity;
    }

    public void connect() {
        final URI address;
        try {
            address = new URI("ws://35.214.3.133:8881");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;

        }

        mClient = new WebSocketClient(address) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                //При подлючении
                Log.i("SERVER", "Connection to server is open");

                String myName = Protocol.packNAme(new Protocol.UserName("Dark Side"));
                Log.i("SERVER", "Sending my name to server");
//                String myName = "3{ name: \"Мишаня\" }";
                mClient.send(myName);

                Protocol.Message m = new Protocol.Message("Проверка связи");
                m.setReceiver(Protocol.GROUP_CHAT);
                String packedMessage = Protocol.packMessage(m);
                Log.i("SERVER", "Sending message" + packedMessage);

                MainActivity.count++;

                //1 - статус пользоваетеля
                //2 - текст сообщения
                //3 - имя пользователя
            }

            @Override
            public void onMessage(String message) {
                //при сообщении с сервера
                Log.i("SERVER", "Server sending message" + message);
                int type = Protocol.getType(message);
                if (type == Protocol.USER_STATUS){
                    //обработать факт подключения или отключения пользователя

                    userStatusChanged(message);
                    activity.increaseUsers();
                }
                if (type == Protocol.MESSAGE){
                    //Показать сообющение на 'rhfyt
                    displayIncomingMessage(message);
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                //При закрытиии соединения
                Log.i("SERVER", "Connection closed");
            }

            @Override
            public void onError(Exception ex) {
                //При ошибке
                Log.i("SERVER", "Error occurred" + ex.getMessage());
            }
        };
        mClient.connect();
    }

    private void displayIncomingMessage(String json){
        Protocol.Message m = Protocol.unpackMessage(json);
        String name = names.get(m.getSender());
        if (name == null){
            name = "Аноним";
        }
        m.getEncodedText();
        m.getSender();
        onMessageReceived.accept(new Pair<String, String>(name, m.getEncodedText()));
    }

    private void userStatusChanged(String json){
        Protocol.UserStatus status = Protocol.unpackStatus(json);
        if (status.isConnected()){
            names.put(status.getUser().getId(), status.getUser().getName());
            activity.showToast(status.getUser().getName() + " пдключился к чату" );
            MainActivity.count++;
        } else {
            names.remove(status.getUser().getId());
        }
    }

    public void sendMessage (String message){
        if (mClient == null || !mClient.isOpen()){
            return;
        }
        Protocol.Message m = new Protocol.Message(message);
        m.setReceiver(Protocol.GROUP_CHAT);
        String packedMessage = Protocol.packMessage(m);
        Log.i("SERVER", "SendingMessage" + packedMessage);
    }

}
