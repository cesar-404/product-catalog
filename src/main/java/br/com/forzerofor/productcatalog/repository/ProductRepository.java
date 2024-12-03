package br.com.forzerofor.productcatalog.repository;

import br.com.forzerofor.productcatalog.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
