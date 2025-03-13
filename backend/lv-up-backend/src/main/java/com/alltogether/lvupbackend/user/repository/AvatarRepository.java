package com.alltogether.lvupbackend.user.repository;


import com.alltogether.lvupbackend.user.domain.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Integer> {
    @Query("SELECT ao.avatar FROM AvatarOwn ao WHERE ao.user.userId = :userId")
    List<Avatar> findAvatarsByUser(@Param("userId") Integer userId);

    @Query("SELECT COUNT(ao) > 0 FROM AvatarOwn ao WHERE ao.user.userId = :userId AND ao.avatar.avatarId = :avatarId")
    boolean existsByUserAndAvatar(@Param("userId") Integer userId, @Param("avatarId") Integer avatarId);
}
