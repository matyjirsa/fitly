package com.fitly.service;

import com.fitly.model.MotivationalQuote;
import com.fitly.model.UserStreak;
import com.fitly.repository.MotivationalQuoteRepository;
import com.fitly.repository.UserStreakRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class UserStreakService {
    @Autowired
    private UserStreakRepository userStreakRepository;

    @Autowired
    private MotivationalQuoteRepository motivationalQuoteRepository;


    public UserStreak updateStreak(Long userId) {
        UserStreak streak = userStreakRepository.findByUserId(userId);
        LocalDate today = LocalDate.now();

        if (streak == null) {
            streak = new UserStreak();
            streak.setUserId(userId);
            streak.setLastLoginDate(today);
            streak.setStreakCount(1);
        } else {
            LocalDate lastLogin = streak.getLastLoginDate();
            if (lastLogin.isEqual(today.minusDays(1))) {
                streak.setStreakCount(streak.getStreakCount() + 1);
            } else if (!lastLogin.isEqual(today)) {
                streak.setStreakCount(1);
            }
            streak.setLastLoginDate(today);
        }
        streak.setUpdatedAt(LocalDateTime.now());
        return userStreakRepository.save(streak);
    }

    public String getDailyMotivation() {
        List<MotivationalQuote> quotes = motivationalQuoteRepository.findAll();
        System.out.println("Načteno citátů: " + quotes.size());
        if (quotes.isEmpty()) {
            System.out.println("Žádné citáty v databázi!");
            return "Dnes je den, kdy můžeš začít znovu!";
        }
        Random random = new Random();
        return quotes.get(random.nextInt(quotes.size())).getQuote();
    }
}