package org.server.socialnetworkserver.repositoris;

import org.server.socialnetworkserver.dtos.CommentDto;
import org.server.socialnetworkserver.entitys.Comment;
import org.server.socialnetworkserver.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("""
              SELECT DISTINCT new org.server.socialnetworkserver.dtos.CommentDto(
                    c.id,
                    c.post.id,
                    u.username,
                    c.content,
                    c.date,
                    c.user.profilePicture
                )
                FROM Comment c
                JOIN c.user u
                WHERE c.post.id = :postId
            """)
    List<CommentDto> findAllCommentByPostId(@Param("postId") Long postId);

    @Query("SELECT COUNT(c.id) FROM Comment c WHERE c.post.id = :postId")
    int countCommentByPostId(@Param("postId") Long postId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Comment c WHERE c.post.id IN (SELECT p.id FROM Post p WHERE p.user = :user)")
    void deleteByPostUser(@Param("user") User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM Comment c WHERE c.user = :user")
    void deleteByUser(@Param("user") User user);
}
