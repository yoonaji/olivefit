package com.springboot.recommend.repository;

import com.springboot.recommend.entity.LotionProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LotionProductRepository extends JpaRepository<LotionProduct, Long> {
    List<LotionProduct> findBySkinTypeContaining(String skinType);
}
