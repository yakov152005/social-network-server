package org.server.socialnetworkserver.repositoris;

import org.server.socialnetworkserver.dtos.CommentDto;
import org.server.socialnetworkserver.dtos.LikeDto;
import org.server.socialnetworkserver.entitys.Like;
import org.server.socialnetworkserver.entitys.Post;
import org.server.socialnetworkserver.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    @Query("SELECT COUNT(l) FROM Like l WHERE l.post.id = :postId")
    int countLikeByPost(@Param("postId") Long postId);

    @Query("""
              SELECT DISTINCT new org.server.socialnetworkserver.dtos.LikeDto(
              l.id,
              l.post.id,
              l.user.username,
              l.user.profilePicture
              )
              FROM Like l
              WHERE l.post.id = :postId
            """)
    public List<LikeDto> findAllLikesByPostId(@Param("postId") Long postId);

    @Query("""
            SELECT CASE WHEN COUNT(l) > 0 THEN TRUE ELSE FALSE END
            FROM Like l
            WHERE l.post.id = :postId
            AND l.user.id = :userId
            """)
    boolean isLikedByUser(@Param("postId") Long postId, @Param("userId") Long userId);

    @Modifying
    @Transactional
    void deleteByPostAndUser(Post post, User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM Like l WHERE l.user = :user")
    void deleteByUser(@Param("user") User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM Like l WHERE l.post.user = :user")
    void deleteByPostUser(@Param("user") User user);

}
