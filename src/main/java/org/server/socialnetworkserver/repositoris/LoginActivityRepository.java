package org.server.socialnetworkserver.repositoris;

import org.server.socialnetworkserver.entitys.LoginActivity;
import org.server.socialnetworkserver.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface LoginActivityRepository extends JpaRepository<LoginActivity,Long> {
    LoginActivity findByUser(User user);

    @Query("SELECT l FROM LoginActivity l WHERE l.user.username = :username")
    LoginActivity findByUsername(@Param("username") String username);

    @Modifying
    @Transactional
    @Query("DELETE FROM LoginActivity l WHERE l.user.id = :userId")
    void deleteByUser(@Param("userId") Long userId);

}
