package xyz.hardliner.align.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import xyz.hardliner.align.domain.Product;
import xyz.hardliner.align.domain.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@SuppressWarnings("WeakerAccess")
public class ProductHandler {

	@Value("${leftover}")
	private Integer leftoverTrigger;
	private final ProductRepository productRepository;

	public List<Product> find(String name, String brand) {
		if (name == null && brand == null) {
			Pageable limit = PageRequest.of(0, 1000);
			return productRepository.findAll(limit).getContent();
		} else if (name != null && brand == null) {
			return productRepository.findAllByNameIgnoreCase(name);
		} else if (name == null) {
			return productRepository.findAllByBrandIgnoreCase(brand);
		} else {
			return productRepository.findAllByNameIgnoreCaseAndBrandIgnoreCase(name, brand);
		}
	}

	public Product save(String name, String brand, Double price, Integer quantity) {
		nameValidation(name);
		priceValidation(price);
		quantityValidation(quantity);
		return productRepository.save(new Product(name, brand, price, quantity));
	}

	public Product save(Long id, String name, String brand, Double price, Integer quantity) {
		Product product = productRepository.findById(id).orElseThrow(()
				-> new IllegalArgumentException("Cannot find product with specified id"));
		if (name != null) {
			nameValidation(name);
			product.setName(name);
		}
		if (brand != null) {
			product.setBrand(brand);
		}
		if (price != null) {
			priceValidation(price);
			product.setPrice(price);
		}
		if (quantity != null) {
			quantityValidation(quantity);
			product.setQuantity(quantity);
		}
		return productRepository.save(product);
	}

	public void removeById(Long id) {
		productRepository.removeById(id);
	}

	public List<Product> getLeftovers() {
		return productRepository.findAllByQuantityLessThan(leftoverTrigger);
	}

	private void nameValidation(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Name is missing");
		} else if (name.isEmpty()) {
			throw new IllegalArgumentException("Name cannot be empty");
		}
	}

	private void priceValidation(Double price) {
		if (price == null) {
			throw new IllegalArgumentException("Price is missing");
		} else if (price < 0) {
			throw new IllegalArgumentException("Price cannot be negative");
		}
	}

	private void quantityValidation(Integer quantity) {
		if (quantity == null) {
			throw new IllegalArgumentException("Quantity is missing");
		} else if (quantity < 0) {
			throw new IllegalArgumentException("Quantity cannot be negative");
		}
	}
}


