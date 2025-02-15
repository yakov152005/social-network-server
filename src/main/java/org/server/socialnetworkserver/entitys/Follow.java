package org.server.socialnetworkserver.entitys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "FOLLOWERS",
        uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "following_id"})
)
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    @JsonIgnore
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    @JsonIgnore
    private User following;

    public Follow() {}

    public Follow(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Follow follow = (Follow) o;
        return Objects.equals(follower, follow.follower) &&
                Objects.equals(following, follow.following);
    }

    @Override
    public int hashCode() {
        return Objects.hash(follower, following);
    }

    @Override
    public String toString() {
        return "Follow{" +
                "id=" + id +
                ", follower=" + (follower != null ? follower.getUsername() : "null") +
                ", following=" + (following != null ? following.getUsername() : "null") +
                '}';
    }
}
