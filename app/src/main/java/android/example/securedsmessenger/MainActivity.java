package android.example.securedsmessenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button sendButton;
    EditText userInput;
    RecyclerView chatWindow;
    MessageController controller;
    Server server;
    TextView counter;
    public static int count;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendButton = findViewById(R.id.send_btn);
        userInput = findViewById(R.id.message_text);
        chatWindow = findViewById(R.id.chat_window);
        counter = findViewById(R.id.counter_text);

        controller = new MessageController();
        controller.setIncomingLayout(R.layout.message);
        controller.setOutgoingLayout(R.layout.outgoing_message);
        controller.setMessageTextId(R.id.message_text);
        controller.setUserNameId(R.id.user_name);
        controller.setMessageTimeId(R.id.message_date);
        controller.appendTo(chatWindow, this);

        controller.addMessage(
                new MessageController.Message("I am writing a secure messenger for our super secret conversations", "Dark Side", true));

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = userInput.getText().toString();
                controller.addMessage(
                        new MessageController.Message(text, "Рептилоид", false)
                );
                server.sendMessage(text);
                userInput.setText("");
            }
        });

        server = new Server(new Consumer<Pair<String, String>>() {
            @Override
            public void accept(final Pair<String, String> pair) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        controller.addMessage(
                                new MessageController.Message(pair.second, pair.first, false)
                        );
                    }
                });

            }
        },this);
        server.connect();

    }

    public void showToast(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void increaseUsers (){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                count++;
                counter.setText("Пользователей онлайн: " + count);
            }
        });

    }


}
