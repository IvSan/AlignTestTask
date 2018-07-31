package xyz.hardliner.align.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductRepository extends JpaRepository<Product, String> {

	Optional<Product> findById(Long id);

	List<Product> findAllByNameIgnoreCase(String name);

	List<Product> findAllByBrandIgnoreCase(String brand);

	List<Product> findAllByNameIgnoreCaseAndBrandIgnoreCase(String name, String brand);

	List<Product> findAllByQuantityLessThan(Integer leftover);

	void removeById(Long id);

}
