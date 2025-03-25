package org.server.socialnetworkserver.entitys;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@Entity
@Table(name = "STORIES")
public class Stories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false, length = 2048)
    private String imageStories;
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Date date;

    public Stories(User user, String imageStories) {
        this.user = user;
        this.imageStories = imageStories;
    }

    public Stories() {

    }
}
