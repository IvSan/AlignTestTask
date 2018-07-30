package xyz.hardliner.align.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface ProductRepository extends JpaRepository<Product, String> {
}
