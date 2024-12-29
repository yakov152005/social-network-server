package org.server.socialnetworkserver.repositoris;

import org.server.socialnetworkserver.entitys.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
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
                SELECT p FROM Post p 
                WHERE p.user.username = :username 
                OR p.user IN (
                    SELECT f.following FROM Follow f WHERE f.follower.username = :username
                )
                ORDER BY p.date DESC
            """)
    Page<Post> findHomeFeedPosts(String username, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.content = :content")
    Post findPostByContent(@PathVariable String content);
}
