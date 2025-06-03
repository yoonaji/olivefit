package com.springboot.recommend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MyPageResponseDto {
    private String skinType;
    private List<BoardResponseDto> myPosts;
}
