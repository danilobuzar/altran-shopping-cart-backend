package br.com.shopping.cart.backend.api.response;

import java.math.BigDecimal;

import br.com.shopping.cart.backend.api.model.Product;
import lombok.Data;

@Data
public class BagItemResponse {

	private String id;

	private Product product;

	private int quantity;

	private BigDecimal subtotal;

}
