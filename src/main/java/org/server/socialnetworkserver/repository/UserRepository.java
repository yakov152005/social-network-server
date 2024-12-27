package org.server.socialnetworkserver.repository;

import org.hibernate.query.Page;
import org.server.socialnetworkserver.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String userName);
    User findByEmailIgnoreCase(String email);

    @Query("SELECT u.username FROM User u")
    List<String> findAllUsernames();

}
