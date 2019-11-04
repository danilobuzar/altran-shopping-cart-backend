package br.com.shopping.cart.backend.api.model;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
@Document(collection = "shopping_cart")
public class ShoppingCart {

	@Id
	@Getter(AccessLevel.NONE)
	private ObjectId id;

	@DBRef
	private User user;

	@DBRef
	private List<BagItem> bagItems;

	public ShoppingCart(User user, List<BagItem> bagItems) {
		this.user = user;
		this.bagItems = bagItems;
	}

	public String getId() {
		return this.id.toHexString();
	}
}
