package com.springboot.recommend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BoardRequestDto {
    private String title;
    private String content;
    private String author;
}
