package com.springboot.recommend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@MappedSuperclass  // 진짜 테이블 X, 상속용 클래스
public abstract class ProductBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_brand")
    private String brand;

    @Column(name = "product_name")
    private String name;

    @Column(name = "product_link")
    private String link;

    @Column(name = "product_price")
    private int price;

    @Column(name = "product_image")
    private String image;

    @Column(name = "skin_type")
    private String skinType;

    @Column(name = "product_score")
    private double score;

    @Column(name = "skin_concern")
    private String skinConcern;

    @Column(name = "irritation_level")
    private String irritationLevel;
}
