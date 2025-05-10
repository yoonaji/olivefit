package com.springboot.recommend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "essence_products")
public class EssenceProduct extends ProductBase {
    // 아무 것도 안 넣어도 됨 (필드는 상속됨)
}
