package com.springboot.recommend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "cream_products")
public class CreamProduct extends ProductBase {
}
