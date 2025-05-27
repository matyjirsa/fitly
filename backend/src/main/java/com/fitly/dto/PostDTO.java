package com.fitly.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private String author;
    private LocalDateTime createdAt;
}