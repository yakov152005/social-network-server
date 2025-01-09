package org.server.socialnetworkserver.repositoris;

import org.server.socialnetworkserver.entitys.LoginActivity;
import org.server.socialnetworkserver.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface LoginActivityRepository extends JpaRepository<LoginActivity,Long> {
}
