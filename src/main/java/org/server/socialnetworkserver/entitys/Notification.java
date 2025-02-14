package org.server.socialnetworkserver.entitys;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "NOTIFICATION")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "post_id", nullable = true)
    private Long postId;

    @Column(nullable = true)
    private String postImg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;//שייכת ליוזר הספציפי הזה

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator; // יצר את ההתראה

    @Column(nullable = true)
    private String initiatorProfilePicture;

    @Column(nullable = true, length = 200)
    private String content;


    @Column(nullable = false)
    private String type;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Date date;

    @Column(nullable = false)
    private boolean isRead = false;

    public Notification(Long postId, String postImg, User recipient, User initiator, String initiatorProfilePicture, String type) {
        this.postId = postId;
        this.postImg = postImg;
        this.recipient = recipient;
        this.initiator = initiator;
        this.initiatorProfilePicture = initiatorProfilePicture;
        this.type = type;
    }

    public Notification(Long postId, String postImg, User recipient, User initiator, String initiatorProfilePicture,String content, String type) {
        this.postId = postId;
        this.postImg = postImg;
        this.recipient = recipient;
        this.initiator = initiator;
        this.initiatorProfilePicture = initiatorProfilePicture;
        this.content = content;
        this.type = type;
    }

    public Notification(User recipient, User initiator, String initiatorProfilePicture, String type) {
        this.postId = -1L;
        this.postImg = "NONE";
        this.recipient = recipient;
        this.initiator = initiator;
        this.initiatorProfilePicture = initiatorProfilePicture;
        this.type = type;
    }

    public Notification() {

    }

    @Override
    public String toString() {
        return "Notification{" +
               "id=" + id +
               ", post=" + postId +
               ", postImg='" + postImg + '\'' +
               ", recipient=" + recipient +
               ", initiator=" + initiator +
               ", initiatorProfilePicture='" + initiatorProfilePicture + '\'' +
               ", type='" + type + '\'' +
               ", date=" + date +
               ", isRead=" + isRead +
               '}';
    }
}
