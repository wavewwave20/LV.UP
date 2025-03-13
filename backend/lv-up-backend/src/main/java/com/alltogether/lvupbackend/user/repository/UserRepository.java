package com.alltogether.lvupbackend.user.repository;

import com.alltogether.lvupbackend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByLoginIdAndLoginType(String loginId, Integer loginType);

    User findByUserNanoId(String userNanoId);

    User findByLoginId(String loginId);

    Boolean existsByNickname(String Nickname);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<User> findByUserId(Integer userId);
}
