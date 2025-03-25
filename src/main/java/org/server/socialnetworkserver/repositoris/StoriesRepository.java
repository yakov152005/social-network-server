package org.server.socialnetworkserver.repositoris;


import org.server.socialnetworkserver.dtos.StoriesDto;
import org.server.socialnetworkserver.entitys.Stories;
import org.server.socialnetworkserver.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface StoriesRepository extends JpaRepository<Stories, Long> {
    List<Stories> findByUserInOrderByDateDesc(List<User> users);

    @Query(""" 
            SELECT new org.server.socialnetworkserver.dtos.StoriesDto
                 (s.id,
                  u.username,
                  u.profilePicture,
                  s.imageStories,
                  s.date)
                FROM Stories s
                JOIN s.user u WHERE u IN :users ORDER BY s.date DESC
            """)
    List<StoriesDto> findStoriesDtoByUserIn(@Param("users") List<User> users);

    @Query("""
    SELECT new org.server.socialnetworkserver.dtos.StoriesDto(
    s.id, u.username, u.profilePicture, s.imageStories, s.date)
    FROM Stories s
    JOIN s.user u
    WHERE u.username = :username
   OR u IN (
       SELECT f.following FROM Follow f WHERE f.follower.username = :username
   )
   ORDER BY s.date DESC
   """)
    List<StoriesDto> findStoriesOfUsersIFollow(@Param("username") String username);



    @Modifying
    @Query("DELETE FROM Stories s WHERE s.date < :cutoff")
    int deleteOldStoriesBefore(@Param("cutoff") Date cutoff);

}
