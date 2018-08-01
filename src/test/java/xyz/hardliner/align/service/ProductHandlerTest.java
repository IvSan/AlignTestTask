package xyz.hardliner.align.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.hardliner.align.domain.Product;
import xyz.hardliner.align.domain.ProductRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ProductHandlerTest {

	private static final String TEST = "test";

	@Mock
	private ProductRepository repository;
	private ProductHandler productHandler;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
		productHandler = new ProductHandler(repository);
	}

	@Test
	public void findAll() {
		when(repository.findAll(any(Pageable.class))).thenReturn(Page.empty());
		productHandler.findAll();
		verify(repository, times(1)).findAll(any(Pageable.class));
		verifyNoMoreInteractions(repository);
	}

	@Test
	public void findAllByName() {
		productHandler.findAllByName(TEST);
		verify(repository, times(1)).findAllByNameIgnoreCase(eq(TEST));
		verifyNoMoreInteractions(repository);
	}

	@Test
	public void findAllByBrand() {
		productHandler.findAllByBrand(TEST);
		verify(repository, times(1)).findAllByBrandIgnoreCase(eq(TEST));
		verifyNoMoreInteractions(repository);
	}

	@Test
	public void findAllByNameAndBrand() {
		productHandler.findAllByNameAndBrand(TEST, TEST);
		verify(repository, times(1)).findAllByNameIgnoreCaseAndBrandIgnoreCase(eq(TEST), eq(TEST));
		verifyNoMoreInteractions(repository);
	}

	@Test
	public void save() {
		productHandler.save(TEST, TEST, 1.0, 1);
		verify(repository, times(1)).save(eq(new Product(TEST, TEST, 1.0, 1)));
		verifyNoMoreInteractions(repository);
	}

	@Test
	public void saveWithWrongName() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("Name cannot be empty");
		productHandler.save("", TEST, 1.0, 1);
	}

	@Test
	public void saveWithWrongPrice() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("Price cannot be negative");
		productHandler.save(TEST, TEST, -1.0, 1);
	}

	@Test
	public void saveWithWrongQuantity() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("Quantity cannot be negative");
		productHandler.save(TEST, TEST, 1.0, -1);
	}

	@Test
	public void update() {
		when(repository.findById(eq(1L))).thenReturn(Optional.of(new Product()));
		productHandler.save(1L, TEST, TEST, 1.0, 1);
		verify(repository, times(1)).findById(eq(1L));
		verify(repository, times(1)).save(eq(new Product(TEST, TEST, 1.0, 1)));
		verifyNoMoreInteractions(repository);
	}

	@Test
	public void removeById() {
		productHandler.removeById(1L);
		verify(repository, times(1)).removeById(eq(1L));
		verifyNoMoreInteractions(repository);
	}

	@Test
	public void getLeftovers() {
		productHandler.getLeftovers();
		verify(repository, times(1)).findAllByQuantityLessThan(any());
		verifyNoMoreInteractions(repository);
	}
}