package org.server.socialnetworkserver.repositoris;

import org.server.socialnetworkserver.entitys.Follow;
import org.server.socialnetworkserver.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


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


    @Modifying
    @Query("""
            DELETE FROM Follow f
            WHERE f.follower = :follower AND f.following = :following
            """)
    void deleteByFollowerAndFollowing(@Param("follower") User follower, @Param("following") User following);


}