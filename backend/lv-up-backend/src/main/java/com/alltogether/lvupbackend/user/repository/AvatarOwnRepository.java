package com.alltogether.lvupbackend.user.repository;


import com.alltogether.lvupbackend.user.domain.AvatarOwn;
import com.alltogether.lvupbackend.user.domain.AvatarOwnId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvatarOwnRepository extends JpaRepository<AvatarOwn, AvatarOwnId> {
}