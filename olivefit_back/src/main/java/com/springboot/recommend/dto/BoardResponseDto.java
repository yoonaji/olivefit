package com.springboot.recommend.dto;

import java.time.LocalDateTime;

public class BoardResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;
}
