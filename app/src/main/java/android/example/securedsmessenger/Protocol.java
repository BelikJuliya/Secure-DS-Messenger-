package android.example.securedsmessenger;

import com.google.gson.Gson;

public class Protocol {
    public final static int USER_STATUS = 1;
    public final static int MESSAGE = 2;
    public final static int USER_NAME = 3;

    static class UserName{
        private String name;

        public UserName (String name){
            this.name = name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    static class Message {
        private long sender;
        private String encodedText;
        private long receiver;

        public long getSender() {
            return sender;
        }

        public String getEncodedText() {
            return encodedText;
        }

        public long getReceiver() {
            return receiver;
        }

        public void setSender(long sender) {
            this.sender = sender;
        }

        public void setEncodedText(String encodedText) {
            this.encodedText = encodedText;
        }

        public void setReceiver(long receiver) {
            this.receiver = receiver;
        }
    }

    public static Message unpackMessage(String json){
        Gson gson = new Gson();
        return gson.fromJson(json.substring(1), Message.class);
    }

    public static String packMessage(Message message){
        Gson gson = new Gson();
        return MESSAGE + gson.toJson(message);
    }

    public static String packNAme(UserName name){
        Gson gson = new Gson();
        return USER_NAME + gson.toJson(name);
    }
}
