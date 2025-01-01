package org.server.socialnetworkserver.repositoris;

import org.server.socialnetworkserver.dtos.CommentDto;
import org.server.socialnetworkserver.entitys.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
    public List<CommentDto> findAllCommentByPostId(@Param("postId") Long postId);

    @Query("SELECT COUNT(c.id) FROM Comment c WHERE c.post.id = :postId")
    public int countCommentByPostId(@Param("postId") Long postId);

}
