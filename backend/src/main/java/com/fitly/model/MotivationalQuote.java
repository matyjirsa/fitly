package com.fitly.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "motivational_quotes")
public class MotivationalQuote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quote", nullable = false)
    private String quote;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getQuote() { return quote; }
    public void setQuote(String quote) { this.quote = quote; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}