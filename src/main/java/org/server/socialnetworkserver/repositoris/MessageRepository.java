package org.server.socialnetworkserver.repositoris;

import org.server.socialnetworkserver.entitys.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    @Query("""
            SELECT DISTINCT CASE
                WHEN m.sender.username = :username THEN m.receiver.username
                ELSE m.sender.username
            END AS chatWith
            FROM Message m
            WHERE m.sender.username = :username OR m.receiver.username = :username
            """)
    List<String> findChatUsers(@Param("username") String username);

    @Query("""
               SELECT m FROM Message m
               WHERE
               (m.sender.username = :sender AND m.receiver.username = :receiver)
               OR
               (m.sender.username = :receiver AND m.receiver.username = :sender)
               ORDER BY m.sentAt ASC
            """)
    List<Message> findMessagesBetweenUsers(@Param("sender") String sender, @Param("receiver") String receiver);


}
