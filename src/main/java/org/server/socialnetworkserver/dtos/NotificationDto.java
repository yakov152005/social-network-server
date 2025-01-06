package org.server.socialnetworkserver.dtos;

import java.util.Date;

public class NotificationDto {
    private long id;
    private Long postId;
    private String postImg;
    private String recipient;
    private String initiator;
    private String initiatorProfilePicture;
    private String type;
    private Date date;
    private boolean isRead;


    public NotificationDto(long id, Long postId, String postImg, String recipient, String initiator, String initiatorProfilePicture, String type, Date date, boolean isRead) {
        this.id = id;
        this.postId = postId;
        this.postImg = postImg;
        this.recipient = recipient;
        this.initiator = initiator;
        this.initiatorProfilePicture = initiatorProfilePicture;
        this.type = type;
        this.date = date;
        this.isRead = isRead;
    }


    public NotificationDto(long id, String recipient, String initiator, String initiatorProfilePicture, String type, Date date, boolean isRead) {
        this.id = id;
        this.postId = -1L;
        this.postImg = "NONE";
        this.recipient = recipient;
        this.initiator = initiator;
        this.initiatorProfilePicture = initiatorProfilePicture;
        this.type = type;
        this.date = date;
        this.isRead = isRead;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getPostImg() {
        return postImg;
    }

    public void setPostImg(String postImg) {
        this.postImg = postImg;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public String getInitiatorProfilePicture() {
        return initiatorProfilePicture;
    }

    public void setInitiatorProfilePicture(String initiatorProfilePicture) {
        this.initiatorProfilePicture = initiatorProfilePicture;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @Override
    public String toString() {
        return "NotificationDto{" +
               "id=" + id +
               ", postId=" + postId +
               ", postImg='" + postImg + '\'' +
               ", recipient='" + recipient + '\'' +
               ", initiator='" + initiator + '\'' +
               ", initiatorProfilePicture='" + initiatorProfilePicture + '\'' +
               ", type='" + type + '\'' +
               ", date=" + date +
               ", isRead=" + isRead +
               '}';
    }
}

