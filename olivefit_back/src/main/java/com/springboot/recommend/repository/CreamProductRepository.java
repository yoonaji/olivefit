package com.springboot.recommend.repository;

import com.springboot.recommend.entity.CreamProduct;
import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreamProductRepository extends JpaRepository<CreamProduct, Long> {
    List<CreamProduct> findBySkinTypeContaining(String skinType);
}
