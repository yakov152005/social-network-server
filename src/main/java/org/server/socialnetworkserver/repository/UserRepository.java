package org.server.socialnetworkserver.repository;

import org.server.socialnetworkserver.service.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}