package org.server.socialnetworkserver.repositoris;

import org.server.socialnetworkserver.entitys.Post;
import org.server.socialnetworkserver.entitys.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(""" 
            SELECT p FROM Post p
            WHERE p.user.username = :currentUsername
             ORDER BY p.id DESC
             """)
    List<Post> findPostsByUsername(@Param("currentUsername") String username);


    @Query("""
    SELECT p,
           COUNT(DISTINCT l.id) AS likeCount,
           COUNT(DISTINCT c.id) AS commentCount,
           CASE WHEN EXISTS (
               SELECT 1 FROM Like l WHERE l.post.id = p.id AND l.user.id = :currentUserId
           ) THEN true ELSE false END AS isLiked
    FROM Post p
    JOIN FETCH p.user u
    LEFT JOIN Like l ON l.post.id = p.id
    LEFT JOIN Comment c ON c.post.id = p.id
    WHERE u.username = :username
    GROUP BY p.id
    ORDER BY p.date DESC
    """)
    List<Object[]> findProfilePosts(@Param("username") String username, @Param("currentUserId") Long currentUserId);


    @Query("""
    SELECT p,
           COUNT(DISTINCT l.id) AS likeCount,
           COUNT(DISTINCT c.id) AS commentCount,
           CASE WHEN EXISTS (
               SELECT 1 FROM Like l WHERE l.post.id = p.id AND l.user.id = :userId
           ) THEN true ELSE false END AS isLiked
    FROM Post p
    JOIN FETCH p.user u
    LEFT JOIN Like l ON l.post.id = p.id
    LEFT JOIN Comment c ON c.post.id = p.id
    WHERE u.username = :currentUsername
    GROUP BY p.id
    ORDER BY p.date DESC
    """)
    List<Object[]> findPostsWithDetails(@Param("currentUsername") String username, @Param("userId") Long userId);

    /*
    @Query("""
                SELECT p FROM Post p
                WHERE p.user.username = :username
                OR p.user IN (
                    SELECT f.following FROM Follow f WHERE f.follower.username = :username
                )
                ORDER BY p.date DESC
            """)
    Page<Post> findHomeFeedPosts(String username, Pageable pageable);
     */

    @Query("""
    SELECT DISTINCT p
    FROM Post p
    JOIN p.user u
    LEFT JOIN Follow f ON f.following = u AND f.follower.username = :username
    WHERE u.username = :username OR f.id IS NOT NULL
    ORDER BY p.date DESC
    """)
    Page<Post> findHomeFeedPosts(String username, Pageable pageable);


    @Query("SELECT p FROM Post p WHERE p.content = :content")
    Post findPostByContent(@PathVariable String content);

    @Modifying
    @Transactional
    @Query("DELETE FROM Post p WHERE p.user = :user")
    void deleteByUser(@Param("user") User u);
}
