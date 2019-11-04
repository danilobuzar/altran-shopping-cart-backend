package br.com.shopping.cart.backend.api.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class ShoppingCartResponse {

	private List<BagItemResponse> bagItemResponse;

	private BigDecimal total;

}
