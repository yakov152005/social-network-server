package org.server.socialnetworkserver.repositoris;


import org.server.socialnetworkserver.entitys.User;
import org.server.socialnetworkserver.dtos.UsernameWithPicDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Date;
import java.util.List;


public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String userName);

    User findByEmailIgnoreCase(String email);

    @Query("SELECT u.username FROM User u")
    List<String> findAllUsernames();

    @Query("""
            SELECT
            new org.server.socialnetworkserver.dtos.UsernameWithPicDto
            (u.username, u.profilePicture) FROM User u
            """)
    List<UsernameWithPicDto> findAllUsernamesWithPic();

    @Query("SELECT u FROM User u WHERE u NOT IN " +
           "(SELECT l.user FROM LoginActivity l WHERE l.date >= :lastWeek)")
    List<User> findUsersNotLoggedInLastWeek(@Param("lastWeek") Date lastWeek);

    @Query("SELECT u.username FROM User u WHERE u.email = :email")
    String findUsernameByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.resetToken = :token")
    User findByResetToken(@Param("token") String token);

}
