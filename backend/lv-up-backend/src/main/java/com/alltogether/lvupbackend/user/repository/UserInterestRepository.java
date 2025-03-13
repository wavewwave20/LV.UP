package com.alltogether.lvupbackend.user.repository;

import com.alltogether.lvupbackend.user.domain.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserInterestRepository extends JpaRepository<UserInterest, Integer> {

    List<UserInterest> findByUserUserId(Integer userId);
}
