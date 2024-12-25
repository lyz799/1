package jsu.lyz.teahouse_ks.Repository;

import jsu.lyz.teahouse_ks.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
}

