package com.example.TimeStock.repasitory;

import com.example.TimeStock.domain.KISStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KISStockRepository extends JpaRepository<KISStock, String> {

}
