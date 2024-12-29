package org.server.socialnetworkserver.repositoris;

import org.server.socialnetworkserver.entitys.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {

}
