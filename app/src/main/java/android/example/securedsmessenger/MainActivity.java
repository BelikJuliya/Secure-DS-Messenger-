package android.example.securedsmessenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Button sendButton;
    EditText userInput;
    RecyclerView chatWindow;
    MessageController controller;
    Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendButton = findViewById(R.id.send_btn);
        userInput = findViewById(R.id.message_text);
        chatWindow = findViewById(R.id.chat_window);

        controller = new MessageController();
        controller.setIncomingLayout(R.layout.message);
        controller.setOutgoingLayout(R.layout.outgoing_message);
        controller.setMessageTextId(R.id.message_text);
        controller.setUserNameId(R.id.user_name);
        controller.setMessageTimeId(R.id.message_date);
        controller.appendTo(chatWindow, this);

        controller.addMessage(
                new MessageController.Message("Hello, Denis! I am writing a secure messenger for our super secret conversations", "Dark Side", true));

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = userInput.getText().toString();
                controller.addMessage(
                        new MessageController.Message(text, "Рептилоид", false)
                );
                userInput.setText("");
            }
        });

        server = new Server();
        server.connect();
    }
}
