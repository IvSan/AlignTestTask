package xyz.hardliner.align.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WebServiceController {

	private final ProductHandler handler;

	@RequestMapping(value = "/products", params = "name")
	public List<Product> getProductsByName(@RequestParam(value = "name") String name) {
		return handler.findAllByName(name);
	}

	@RequestMapping(value = "/products", params = "brand")
	public List<Product> getProductsByNameAndBrand(@RequestParam(value = "brand") String brand) {
		return handler.findAllByBrand(brand);
	}

	@RequestMapping(value = "/products", params = {"name", "brand"})
	public List<Product> getProductsByNameAndBrand(@RequestParam(value = "name") String name,
	                                               @RequestParam(value = "brand") String brand) {
		return handler.findAllByNameAndBrand(name, brand);
	}

	@RequestMapping("/products")
	public List<Product> getProducts() {
		return handler.findAll();
	}

	@PostMapping("/product")
	public Product addNewProduct(@RequestParam(value = "name") String name,
	                             @RequestParam(value = "brand", required = false) String brand,
	                             @RequestParam(value = "price") Double price,
	                             @RequestParam(value = "quantity") Integer quantity) {
		return handler.save(name, brand, price, quantity);
	}

	@PutMapping("/product")
	public Product updateProduct(@RequestParam(value = "id") Long id,
	                             @RequestParam(value = "name", required = false) String name,
	                             @RequestParam(value = "brand", required = false) String brand,
	                             @RequestParam(value = "price", required = false) Double price,
	                             @RequestParam(value = "quantity", required = false) Integer quantity) {
		return handler.save(id, name, brand, price, quantity);
	}

	@Transactional
	@DeleteMapping("/product")
	public void removeProduct(@RequestParam(value = "id") Long id) {
		handler.removeById(id);
	}

	@RequestMapping("/leftovers")
	public List<Product> getLeftovers() {
		return handler.getLeftovers();
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleConflict(IllegalArgumentException e) {
		log.error("Bad request:", e);
		return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
	}


}
