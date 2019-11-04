package br.com.shopping.cart.backend.api.model;

import java.math.BigDecimal;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "product")
public class Product {

	@Id
	@Getter(AccessLevel.NONE)
	private ObjectId id;

	private String name;

	private BigDecimal value;

	public String getId() {
		return this.id.toHexString();
	}
}
