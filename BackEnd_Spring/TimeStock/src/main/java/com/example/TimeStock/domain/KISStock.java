package com.example.TimeStock.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "KIS_stock")
@Getter
public class KISStock {
    @Id
    @Column(length = 6)
    private String code;
}
