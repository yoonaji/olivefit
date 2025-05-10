package com.springboot.recommend.repository;

import com.springboot.recommend.entity.AllInOneProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AllInOneProductRepository extends JpaRepository<AllInOneProduct, Long> {
    List<AllInOneProduct> findBySkinType(String skinType);
}
