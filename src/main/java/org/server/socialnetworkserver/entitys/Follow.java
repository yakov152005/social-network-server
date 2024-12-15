package org.server.socialnetworkserver.entitys;

import jakarta.persistence.*;

@Entity
@Table(name = "FOLLOWERS")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "follower_id")
    private User follower;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "following_id")
    private User following;

    public Follow() {}

    public Follow(long id, User follower, User following) {
        this.id = id;
        this.follower = follower;
        this.following = following;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollowing() {
        return following;
    }

    public void setFollowing(User following) {
        this.following = following;
    }
}