package org.server.socialnetworkserver.repositoris;

import org.server.socialnetworkserver.dtos.NotificationDto;
import org.server.socialnetworkserver.entitys.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
                    n.type,
                    n.date,
                    n.isRead
                )
                FROM Notification n
                WHERE n.recipient.username = :username
                ORDER BY n.date DESC
            """)
    List<NotificationDto> findUnreadNotifications(@Param("username") String username);





    /*
     @Query("""
    SELECT new org.server.socialnetworkserver.dtos.NotificationDto(
        n.id,
        n.post.id,
        n.post.imageUrl,
        n.recipient.username,
        n.initiator.username,
        n.initiator.profilePicture,
        n.type,
        n.date,
        n.isRead
    )
    FROM Notification n
    WHERE n.recipient.username = :username
    ORDER BY n.date DESC
    """)
    List<NotificationDto> findUnreadNotifications(@Param("username") String username);
     */


}
