package com.springboot.recommend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String brand;
    private String name;
    private String link;
    private int price;
    private String image;
    private String skinType;
    private double score;
    private String skinConcern;
    private String irritationLevel;
}
