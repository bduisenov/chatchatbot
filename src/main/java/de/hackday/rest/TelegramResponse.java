package de.hackday.rest;

import java.util.List;

public class TelegramResponse {

    public static class Chat {
        Long id;

        String first_name;

        String last_name;

        public Chat() {
        }

        public Chat(Long id, String first_name, String last_name) {
            this.id = id;
            this.first_name = first_name;
            this.last_name = last_name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        @Override public String toString() {
            return "Chat{" +
                    "id=" + id +
                    ", first_name='" + first_name + '\'' +
                    ", last_name='" + last_name + '\'' +
                    '}';
        }
    }

    public static class Message {
        Long message_id;

        String text;

        Chat chat;

        public Message() {
        }

        public Message(Long message_id, String text) {
            this.message_id = message_id;
            this.text = text;
        }

        public Long getMessage_id() {
            return message_id;
        }

        @Override public String toString() {
            return "Message{" +
                    "message_id=" + message_id +
                    ", text='" + text + '\'' +
                    ", chat=" + chat +
                    '}';
        }

        public Chat getChat() {
            return chat;
        }

        public void setChat(Chat chat) {
            this.chat = chat;
        }

        public void setMessage_id(Long message_id) {
            this.message_id = message_id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

    }

    public static class Payload {
        Long update_id;

        Message message;

        public Payload() {
        }

        public Payload(Long update_id, Message message) {
            this.update_id = update_id;
            this.message = message;
        }

        public Long getUpdate_id() {
            return update_id;
        }

        public void setUpdate_id(Long update_id) {
            this.update_id = update_id;
        }

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }

        @Override public String toString() {
            return "Payload{" +
                    "update_id=" + update_id +
                    ", message=" + message +
                    '}';
        }
    }

    Boolean ok;

    List<Payload> result;

    public TelegramResponse() {
    }

    public TelegramResponse(Boolean ok, List<Payload> result) {
        this.ok = ok;
        this.result = result;
    }

    public Boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<Payload> getResult() {
        return result;
    }

    public void setResult(List<Payload> result) {
        this.result = result;
    }

    @Override public String toString() {
        return "TelegramResponse{" +
                "ok=" + ok +
                ", result=" + result +
                '}';
    }
}
