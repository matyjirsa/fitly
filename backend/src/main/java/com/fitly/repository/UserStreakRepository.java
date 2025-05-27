package com.fitly.repository;

import com.fitly.model.UserStreak;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStreakRepository extends JpaRepository<UserStreak, Long> {
    UserStreak findByUserId(Long userId);
}