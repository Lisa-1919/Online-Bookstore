package org.onlineBookstore.repo;

import org.onlineBookstore.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {}
