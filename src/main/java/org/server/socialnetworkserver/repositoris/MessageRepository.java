package org.server.socialnetworkserver.repositoris;

import org.server.socialnetworkserver.entitys.Message;
import org.server.socialnetworkserver.dtos.ChatUserDto;
import org.server.socialnetworkserver.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {


    @Query(""" 
            SELECT m FROM Message m
            WHERE m.sender.username = :username
            OR m.receiver.username = :username
            ORDER BY m.sentAt DESC
            """)
    List<Message> findMessagesByUser(@Param("username") String username);

    /*
    @Query("""
        SELECT DISTINCT new org.server.socialnetworkserver.dtos.ChatUserDto(
            CASE
                WHEN m.sender.username = :username THEN m.receiver.username
                ELSE m.sender.username
            END,
            CASE
                WHEN m.sender.username = :username THEN m.receiver.profilePicture
                ELSE m.sender.profilePicture
            END
        )
        FROM Message m
        WHERE (m.sender.username = :username OR m.receiver.username = :username)
        """)
    List<ChatUserDto> findChatUsers(@Param("username") String username);
     */

    @Query("""
    SELECT new org.server.socialnetworkserver.dtos.ChatUserDto(
        CASE
            WHEN m.sender.username = :username THEN m.receiver.username
            ELSE m.sender.username
        END,
        CASE
            WHEN m.sender.username = :username THEN m.receiver.profilePicture
            ELSE m.sender.profilePicture
        END,
        m.content,
        m.sentAt
    )
    FROM Message m
    WHERE m.id IN (
        SELECT MAX(m2.id)
        FROM Message m2
        WHERE m2.sender.username = :username OR m2.receiver.username = :username
        GROUP BY
            CASE
                WHEN m2.sender.username = :username THEN m2.receiver.username
                ELSE m2.sender.username
            END
    )
    ORDER BY m.sentAt DESC
    """)
    List<ChatUserDto> findChatUsers(@Param("username") String username);



    @Query("""
               SELECT m FROM Message m
               WHERE
               (m.sender.username = :sender AND m.receiver.username = :receiver)
               OR
               (m.sender.username = :receiver AND m.receiver.username = :sender)
               ORDER BY m.sentAt ASC
            """)
    List<Message> findMessagesBetweenUsers(@Param("sender") String sender, @Param("receiver") String receiver);


    @Modifying
    @Transactional
    @Query("DELETE FROM Message m WHERE m.receiver = :user OR m.sender = :user")
    void deleteBySenderOrReceiver(@Param("user") User user);

}
