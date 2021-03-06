package xyz.hardliner.align.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "products")
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Product {
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private String brand;
	private Double price;
	private Integer quantity;

	public Product(String name, String brand, Double price, Integer quantity) {
		this.name = name;
		this.brand = brand;
		this.price = price;
		this.quantity = quantity;
	}
}
