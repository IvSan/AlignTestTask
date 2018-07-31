package xyz.hardliner.align;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.hardliner.align.domain.Product;
import xyz.hardliner.align.domain.ProductRepository;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class WebServiceController {

	private static final Integer LEFTOVER_TRIGGER = 5;
	private final ProductRepository productRepository;

	@RequestMapping(value = "/products", params = "name")
	public List<Product> getProductsByName(@RequestParam(value = "name") String name) {
		return productRepository.findAllByNameIgnoreCase(name);
	}

	@RequestMapping(value = "/products", params = "brand")
	public List<Product> getProductsByBrand(@RequestParam(value = "brand") String brand) {
		return productRepository.findAllByBrandIgnoreCase(brand);
	}

	@RequestMapping(value = "/products", params = {"name", "brand"})
	public List<Product> getProductsByBrand(@RequestParam(value = "name") String name,
	                                        @RequestParam(value = "brand") String brand) {
		return productRepository.findAllByNameIgnoreCaseAndBrandIgnoreCase(name, brand);
	}

	@RequestMapping("/products")
	public List<Product> getProducts() {
		Pageable limit = PageRequest.of(0, 1000);
		return productRepository.findAll(limit).getContent();
	}

	@PostMapping("/product")
	public Product addNewProduct(@RequestParam(value = "name") String name,
	                             @RequestParam(value = "brand", required = false) String brand,
	                             @RequestParam(value = "price") Double price,
	                             @RequestParam(value = "quantity") Integer quantity) {
		nameValidation(name);
		priceValidation(price);
		quantityValidation(quantity);
		return productRepository.save(new Product(name, brand, price, quantity));
	}

	@PutMapping("/product")
	public Product updateProduct(@RequestParam(value = "id") Long id,
	                             @RequestParam(value = "name", required = false) String name,
	                             @RequestParam(value = "brand", required = false) String brand,
	                             @RequestParam(value = "price", required = false) Double price,
	                             @RequestParam(value = "quantity", required = false) Integer quantity) {
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

	@Transactional
	@DeleteMapping("/product")
	public void removeProduct(@RequestParam(value = "id") Long id) {
		productRepository.removeById(id);
	}

	@RequestMapping("/leftovers")
	public List<Product> getLeftovers() {
		return productRepository.findAllByQuantityLessThan(LEFTOVER_TRIGGER);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleConflict(IllegalArgumentException e) {
		return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
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
