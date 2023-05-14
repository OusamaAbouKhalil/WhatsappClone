package com.example.whatsappclone.Models;

public class Message {

    String userId,message,messageId;
    Long time;

    public Message(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public Message(){

    }

    public Message(String userId, String message, Long time) {
        this.userId = userId;
        this.message = message;
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

}
