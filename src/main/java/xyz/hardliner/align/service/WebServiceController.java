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

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WebServiceController {

	private final ProductHandler handler;
	private final XlsGenerator xlsGenerator;

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

	@RequestMapping(value = "/products")
	public List<Product> getProducts(@RequestParam(value = "name", required = false) String name,
	                                 @RequestParam(value = "brand", required = false) String brand) {
		return handler.find(name, brand);
	}

	@RequestMapping(value = "/products/xls")
	public ResponseEntity<byte[]> getProductsXls(@RequestParam(value = "name", required = false) String name,
	                                             @RequestParam(value = "brand", required = false) String brand) {
		List<Product> products = handler.find(name, brand);
		return ResponseEntity.ok()
				.header(CONTENT_DISPOSITION, "attachment; filename=\"" + XlsGenerator.fileName() + "\"")
				.body(xlsGenerator.create(products));
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
