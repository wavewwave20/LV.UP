package com.alltogether.lvupbackend.user.repository;

import com.alltogether.lvupbackend.user.domain.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InterestRepository extends JpaRepository<Interest, Integer> {
}
