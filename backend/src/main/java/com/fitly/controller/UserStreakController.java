package com.fitly.controller;

import com.fitly.model.UserStreak;
import com.fitly.service.UserStreakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserStreakController {
    @Autowired
    private UserStreakService userStreakService;

    @GetMapping("/{userId}/streak")
    public UserStreak getUserStreak(@PathVariable Long userId) {
        return userStreakService.updateStreak(userId);
    }

    @GetMapping("/{userId}/motivation")
    public String getDailyMotivation() {
        return userStreakService.getDailyMotivation();
    }
}