package org.server.socialnetworkserver.repositoris;

import org.server.socialnetworkserver.dtos.*;
import org.server.socialnetworkserver.entitys.Follow;
import org.server.socialnetworkserver.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface FollowRepository extends JpaRepository<Follow, Long> {

    /**
     * @param username
     * @return מחזיר כמה משתמשים עוקבים אחרי המשתמש הנוכחי
     */
    @Query("""
            SELECT COUNT(f.id)
            FROM Follow  f 
            WHERE f.following.username = :username
            """)
    int countFollowers(@Param("username") String username);


    /**
     * @param username
     * @return מחזיר אחרי כמה משתמשים המשתמש הנוכחי עוקב (מספר)
     */
    @Query("""
            SELECT COUNT(f.id)
            FROM Follow  f 
            WHERE f.follower.username = :username
            """)
    int countFollowing(@Param("username") String username);


    @Query("SELECT new org.server.socialnetworkserver.dtos.FollowDto(f.follower.username, f.follower.profilePicture) FROM Follow f WHERE f.following.username = :username")
    List<FollowDto> getAllFollowers(@Param("username") String username);

    @Query("SELECT new org.server.socialnetworkserver.dtos.FollowDto(f.following.username, f.following.profilePicture) FROM Follow f WHERE f.follower.username = :username")
    List<FollowDto> getAllFollowing(@Param("username") String username);


    /**
     * @param currentUsername
     * @param username
     * @return מחזיר אמת אם המשתמש הנוכחי עוקב אחרי המשתמש שנכנסתי אליו לחיפוש
     */
    @Query("""
            SELECT CASE WHEN COUNT(f) > 0 THEN TRUE ELSE FALSE END
            FROM Follow f
            WHERE f.follower.username = :currentUsername
            AND f.following.username = :username
            """)
    boolean isFollowing(
            @Param("currentUsername") String currentUsername,
            @Param("username") String username
    );

    @Query("""
    SELECT new org.server.socialnetworkserver.dtos.ProfileStatsDto(
        CAST((SELECT COUNT(f.id) FROM Follow f WHERE f.following.id = u.id) AS long),
        CAST((SELECT COUNT(f.id) FROM Follow f WHERE f.follower.id = u.id) AS long),
        CASE
            WHEN EXISTS (SELECT 1 FROM Follow f WHERE f.follower.id = :currentUserId AND f.following.id = u.id)
            THEN true ELSE false
        END
         )
    FROM User u
    WHERE u.username = :username
    """)
    List<ProfileStatsDto> getProfileStats(@Param("username") String username, @Param("currentUserId") Long currentUserId);


    @Query("""
    SELECT new org.server.socialnetworkserver.dtos.SuggestedFriendsDto(
        u.id, u.username, u.profilePicture,
        CASE WHEN f.id IS NOT NULL THEN true ELSE false END
    )
    FROM User u
    LEFT JOIN Follow f ON f.follower.username = :currentUsername AND f.following = u
    WHERE u.username != :currentUsername
    AND f.id IS NULL
    """)
    List<SuggestedFriendsDto> getSuggestedFriends(@Param("currentUsername") String currentUsername);



    @Modifying
    @Query("""
            DELETE FROM Follow f
            WHERE f.follower = :follower AND f.following = :following
            """)
    void deleteByFollowerAndFollowing(@Param("follower") User follower, @Param("following") User following);

    @Query("SELECT f.following.id FROM Follow f WHERE f.follower.id = :userId")
    List<Long> findFollowingIdsByUserId(@Param("userId") Long userId);

    @Query("""
    SELECT new org.server.socialnetworkserver.dtos.OnlineFriendsDto
    (u.id, u.username, u.profilePicture)
    FROM Follow f
    JOIN f.following u
    WHERE f.follower.id = :userId
    """)
    List<OnlineFriendsDto> findFollowingUsersByUserId(@Param("userId") Long userId);

}