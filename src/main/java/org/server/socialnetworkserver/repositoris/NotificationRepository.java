package org.server.socialnetworkserver.repositoris;

import org.server.socialnetworkserver.dtos.NotificationDto;
import org.server.socialnetworkserver.entitys.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("""
                SELECT new org.server.socialnetworkserver.dtos.NotificationDto(
                    n.id,
                    n.postId,
                    n.postImg,
                    n.recipient.username,
                    n.initiator.username,
                    n.initiator.profilePicture,
                    n.content,
                    n.type,
                    n.date,
                    n.isRead
                )
                FROM Notification n
                WHERE n.recipient.username = :username
                ORDER BY n.date DESC
            """)
    List<NotificationDto> findUnreadNotifications(@Param("username") String username);

    @Modifying
    @Transactional
    @Query("""
    DELETE FROM Notification n
    WHERE n.recipient.id = :recipientId
    AND n.initiator.id = :initiatorId
    AND n.type = :type
    AND (:postId IS NULL OR n.postId = :postId)
""")
    void deleteExistingNotification(@Param("recipientId") Long recipientId,
                                    @Param("initiatorId") Long initiatorId,
                                    @Param("type") String type,
                                    @Param("postId") Long postId);


    @Modifying
    @Transactional
    @Query("DELETE FROM Notification n WHERE n.recipient.id = :userId")
    void deleteByRecipient(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Notification n WHERE n.initiator.id = :userId")
    void deleteByInitiator(@Param("userId") Long userId);

}
