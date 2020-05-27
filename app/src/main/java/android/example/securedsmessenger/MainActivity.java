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

    Button mSendButton;
    EditText mUserInput;
    RecyclerView mChatWindow;
    MessageController mController;
    Server mServer;
    TextView mCounter;
    public static int mCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSendButton = findViewById(R.id.send_btn);
        mUserInput = findViewById(R.id.message_text);
        mChatWindow = findViewById(R.id.chat_window);
        mCounter = findViewById(R.id.counter_text);

        mController = new MessageController();
        mController.setIncomingLayout(R.layout.message);
        mController.setOutgoingLayout(R.layout.outgoing_message);
        mController.setMessageTextId(R.id.message_text);
        mController.setUserNameId(R.id.user_name);
        mController.setMessageTimeId(R.id.message_date);
        mController.appendTo(mChatWindow, this);

        mController.addMessage(
                new MessageController.Message("I am writing a secure messenger for our super secret conversations", "Dark Side", true));

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mUserInput.getText().toString();
                mController.addMessage(
                        new MessageController.Message(text, "Рептилоид", false)
                );
                mServer.sendMessage(text);
                mUserInput.setText("");
            }
        });

        mServer = new Server(new Consumer<Pair<String, String>>() {
            @Override
            public void accept(final Pair<String, String> pair) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mController.addMessage(
                                new MessageController.Message(pair.second, pair.first, false)
                        );
                    }
                });

            }
        }, this);
        mServer.connect();

    }

    public void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void increaseUsers() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCount++;
                mCounter.setText("Пользователей онлайн: " + mCount);
            }
        });

    }


}
