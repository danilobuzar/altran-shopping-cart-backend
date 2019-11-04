package br.com.shopping.cart.backend.api.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "bag_item")
public class BagItem {

	@Id
	@Getter(AccessLevel.NONE)
	private ObjectId id;

	private Product product;

	private int quantity;

	public String getId() {
		return this.id.toHexString();
	}
}
