package com.springboot.recommend.repository;

import com.springboot.recommend.entity.EssenceProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EssenceProductRepository extends JpaRepository<EssenceProduct, Long> {
    List<EssenceProduct> findBySkinType(String skinType);
}
