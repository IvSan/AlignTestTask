package xyz.hardliner.align;

import lombok.RequiredArgsConstructor;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.hardliner.align.domain.Product;
import xyz.hardliner.align.domain.ProductRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WebServiceController {

	private final ProductRepository productRepository;

	@RequestMapping("/products")
	public List<Product> greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return productRepository.findAll();
	}

	@RequestMapping(value = "/product", method = RequestMethod.POST)
	public Product showLoginPage(@RequestParam(value = "name") String name,
	                             @RequestParam(value = "brand", required = false) String brand,
	                             @RequestParam(value = "price") Double price,
	                             @RequestParam(value = "quantity") Integer quantity) {
		return productRepository.save(new Product(name, brand, price, quantity));
	}

}
