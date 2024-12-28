package org.server.socialnetworkserver.repository;


import org.server.socialnetworkserver.entitys.User;
import org.server.socialnetworkserver.dto.UsernameWithPicDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;


public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String userName);

    User findByEmailIgnoreCase(String email);

    @Query("SELECT u.username FROM User u")
    List<String> findAllUsernames();

    @Query("""
            SELECT 
            new org.server.socialnetworkserver.dto.UsernameWithPicDTO
            (u.username, u.profilePicture) FROM User u
            """)
    List<UsernameWithPicDTO> findAllUsernamesWithPic();


}
