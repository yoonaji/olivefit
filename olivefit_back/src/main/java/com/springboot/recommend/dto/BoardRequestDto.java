package com.springboot.recommend.dto;

import lombok.Getter;

@Getter
public class BoardRequestDto {
    private String title;
    private String content;
    private String author;
}
