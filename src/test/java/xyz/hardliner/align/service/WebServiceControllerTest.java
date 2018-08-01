package xyz.hardliner.align.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import xyz.hardliner.align.SecurityConfig;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(WebServiceController.class)
@ContextConfiguration(classes = {SecurityConfig.class})
@ComponentScan(basePackages = "xyz.hardliner.align")
public class WebServiceControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ProductHandler handler;

	/**
	 * Test for denying unauthorized user.
	 */
	@Test
	public void testUnauthorized() throws Exception {
		this.mockMvc.perform(get("/products")).andExpect(status().isUnauthorized());
	}

	/**
	 * Test for success user authorization.
	 */
	@Test
	@WithMockUser(username = "admin", password = "secret", roles = "CRUD")
	public void testBasicAuth() throws Exception {
		this.mockMvc.perform(get("/products")).andExpect(authenticated().withRoles("CRUD"));
	}

	/**
	 * Test for adding new product by user.
	 */
	@Test
	@WithMockUser(username = "user", roles = "READ")
	public void testAddNewProductByUser() throws Exception {
		this.mockMvc.perform(post("/product")).andExpect(status().isForbidden());
	}

	/**
	 * Test for adding new product by admin.
	 */
	@Test
	@WithMockUser(username = "admin", password = "secret", roles = "CRUD")
	public void testAddNewProductByAdmin() throws Exception {
		this.mockMvc.perform(post("/product")
				.param("name", "pencil")
				.param("brand", "BIC")
				.param("price", "2.0")
				.param("quantity", "20"))
				.andExpect(status().isOk());
		verify(handler, times(1))
				.save("pencil", "BIC", 2.0, 20);
		verifyNoMoreInteractions(handler);
	}

	/**
	 * Test for adding new product by admin with wrong quantity.
	 */
	@Test
	@WithMockUser(username = "admin", password = "secret", roles = "CRUD")
	public void testAddNewProductByAdminWithoutName() throws Exception {
		when(handler.save(anyString(), anyString(), anyDouble(), eq(-20)))
				.thenThrow(new IllegalArgumentException("test"));
		this.mockMvc.perform(post("/product")
				.param("name", "pencil")
				.param("brand", "BIC")
				.param("price", "2.0")
				.param("quantity", "-20"))
				.andExpect(status().isBadRequest());
		verify(handler, times(1))
				.save("pencil", "BIC", 2.0, -20);
		verifyNoMoreInteractions(handler);
	}

	/**
	 * Test for editing a product by user.
	 */
	@Test
	@WithMockUser(username = "user", roles = "READ")
	public void testUpdateProductByUser() throws Exception {
		this.mockMvc.perform(put("/product")).andExpect(status().isForbidden());
	}

	/**
	 * Test for editing a product by admin.
	 */
	@Test
	@WithMockUser(username = "admin", password = "secret", roles = "CRUD")
	public void testUpdateProductByAdmin() throws Exception {
		this.mockMvc.perform(put("/product")
				.param("id", "1")
				.param("name", "pencil")
				.param("brand", "BIC")
				.param("price", "2.0")
				.param("quantity", "20"))
				.andExpect(status().isOk());
		verify(handler, times(1))
				.save(1L, "pencil", "BIC", 2.0, 20);
		verifyNoMoreInteractions(handler);
	}

	/**
	 * Test for deleting a product by user.
	 */
	@Test
	@WithMockUser(username = "user", roles = "READ")
	public void testDeleteProductByUser() throws Exception {
		this.mockMvc.perform(delete("/product")).andExpect(status().isForbidden());
	}

	/**
	 * Test for deleting a product by admin.
	 */
	@Test
	@WithMockUser(username = "admin", password = "secret", roles = "CRUD")
	public void testDeleteProductByAdmin() throws Exception {
		this.mockMvc.perform(delete("/product")
				.param("id", "1"))
				.andExpect(status().isOk());
		verify(handler, times(1))
				.removeById(1L);
		verifyNoMoreInteractions(handler);
	}

	/**
	 * Test for getting products.
	 */
	@Test
	@WithMockUser(username = "user", roles = "READ")
	public void testGetProducts() throws Exception {
		this.mockMvc.perform(get("/products")).andExpect(status().isOk());
		verify(handler, times(1)).find(eq(null), eq(null));
		this.mockMvc.perform(get("/products")
				.param("name", "pencil")).andExpect(status().isOk());
		verify(handler, times(1)).find(eq("pencil"), eq(null));
		this.mockMvc.perform(get("/products")
				.param("brand", "BIC")).andExpect(status().isOk());
		verify(handler, times(1)).find(eq(null), eq("BIC"));
		this.mockMvc.perform(get("/products")
				.param("name", "pencil")
				.param("brand", "BIC")).andExpect(status().isOk());
		verify(handler, times(1)).find(eq("pencil"), eq("BIC"));
		verifyNoMoreInteractions(handler);
	}

	/**
	 * Test for getting leftovers.
	 */
	@Test
	@WithMockUser(username = "user", roles = "READ")
	public void testGetLeftovers() throws Exception {
		this.mockMvc.perform(get("/leftovers")).andExpect(status().isOk());
		verify(handler, times(1)).getLeftovers();
		verifyNoMoreInteractions(handler);
	}
}