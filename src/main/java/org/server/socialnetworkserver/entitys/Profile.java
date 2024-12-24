package org.server.socialnetworkserver.entitys;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "PROFILE")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = true)
    private String profilePicture;


    public Profile(long id, User user, String profilePicture) {
        this.id = id;
        this.user = user;
        this.profilePicture = profilePicture;
    }

    public Profile() {}



    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }


    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", user=" + user.getUsername() +
                ", profilePicture='" + profilePicture  +
                '}';
    }
}
