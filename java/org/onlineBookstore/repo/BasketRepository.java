package org.onlineBookstore.repo;

import org.onlineBookstore.entities.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    List<Basket> findByProductsId(Long id);
    Basket findByUserId(Long userId);
}
