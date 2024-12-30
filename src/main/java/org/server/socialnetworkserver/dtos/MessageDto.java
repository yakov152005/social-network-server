package org.server.socialnetworkserver.dtos;

import java.util.Date;

public class MessageDto {
    private long id;
    private String sender;
    private String receiver;
    private String content;
    private boolean isRead;
    private Date sentAt;

    public MessageDto(long id, String sender, String receiver, String content, boolean isRead, Date sentAt) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.isRead = isRead;
        this.sentAt = sentAt;
    }

    public MessageDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public Date getSentAt() {
        return sentAt;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }

}
