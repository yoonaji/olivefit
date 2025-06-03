package com.springboot.recommend.entity;

import jakarta.persistence.*;
import lombok.Setter;

import java.util.List;

@Setter
@Entity
public class UserSkinInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String sensitivityLevel;
    private String skinType;
    private String skinConcerns;
}
