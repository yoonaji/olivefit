package com.springboot.recommend.repository;

import com.springboot.recommend.entity.SkinProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkinProductRepository extends JpaRepository<SkinProduct, Long> {
    List<SkinProduct> findBySkinType(String skinType);
}
