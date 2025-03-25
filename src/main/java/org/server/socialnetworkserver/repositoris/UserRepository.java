package org.server.socialnetworkserver.repositoris;


import org.server.socialnetworkserver.dtos.OnlineFriendsDto;
import org.server.socialnetworkserver.dtos.UserSettingsDto;
import org.server.socialnetworkserver.entitys.User;
import org.server.socialnetworkserver.dtos.UsernameWithPicDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Date;
import java.util.List;


public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String userName);

    boolean existsByUsername(String username);

    User findByEmailIgnoreCase(String email);

    @Query("SELECT u FROM User u WHERE u.id = :userId")
    User findUsersById(long userId);

    @Query("""
    SELECT
    new org.server.socialnetworkserver.dtos.UserSettingsDto
    (u.username, u.email, u.profilePicture, u.bio, u.gender, u.relationship, u.fullName) FROM User u
    """)
    UserSettingsDto findUserSettingsByUsername(@Param("username") String username);

    @Query("SELECT u.username FROM User u")
    List<String> findAllUsernames();

    @Query("""
            SELECT
            new org.server.socialnetworkserver.dtos.UsernameWithPicDto
            (u.username, u.profilePicture, u.bio) FROM User u
            """)
    List<UsernameWithPicDto> findAllUsernamesWithPic();

    @Query("SELECT u FROM User u WHERE u NOT IN " +
           "(SELECT l.user FROM LoginActivity l WHERE l.date >= :lastMonth)")
    List<User> findUsersNotLoggedInLastMonth(@Param("lastMonth") Date lastMonth);

    @Query("SELECT u.username FROM User u WHERE u.email = :email")
    String findUsernameByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.resetToken = :token")
    User findByResetToken(@Param("token") String token);

    @Query("""
    SELECT new org.server.socialnetworkserver.dtos.OnlineFriendsDto
    ( u.id, u.username,u.profilePicture) FROM User u WHERE u.id IN :ids
    """)
    List<OnlineFriendsDto> findUsersByIds(@Param("ids") List<Long> ids);

}
