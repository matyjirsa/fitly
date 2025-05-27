package com.fitly.repository;

import com.fitly.model.MotivationalQuote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MotivationalQuoteRepository extends JpaRepository<MotivationalQuote, Long> {
}