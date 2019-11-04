package br.com.shopping.cart.backend.api.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.shopping.cart.backend.api.model.BagItem;
import br.com.shopping.cart.backend.api.model.Product;
import br.com.shopping.cart.backend.api.model.ShoppingCart;
import br.com.shopping.cart.backend.api.model.User;
import br.com.shopping.cart.backend.api.repository.ProductRepository;
import br.com.shopping.cart.backend.api.repository.UserRepository;
import br.com.shopping.cart.backend.api.request.ShoppingCartRequest;
import br.com.shopping.cart.backend.api.response.BagItemResponse;
import br.com.shopping.cart.backend.api.response.ShoppingCartResponse;
import br.com.shopping.cart.backend.api.service.BagItemService;
import br.com.shopping.cart.backend.api.service.ShoppingCartService;

@RestController
@RequestMapping("users/{userId}/cart")
public class ShoppingCartController {
	private static Logger logger = LogManager.getLogger(ShoppingCartController.class);

	@Autowired
	private ShoppingCartService cartService;;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private BagItemService bagItemService;

	@GetMapping
	public ResponseEntity<?> getCartByUser(@PathVariable String userId) {
		final Optional<User> user = this.userRepo.findById(userId);

		if (!user.isPresent()) {
			logger.debug("user not found for id " + userId);
			final Map<String, String> err = new HashMap<String, String>();
			err.put("message", "User not found");
			return ResponseEntity.unprocessableEntity().body(err);
		}

		final ShoppingCart cart = this.cartService.findAllByUser(user.get());

		if (cart != null) {
			final List<BagItemResponse> listBagItemResponse = new ArrayList<BagItemResponse>();

			cart.getBagItems().stream().forEach(item -> {
				final BagItemResponse bir = new BagItemResponse();
				bir.setId(item.getId());
				bir.setProduct(item.getProduct());
				bir.setQuantity(item.getQuantity());
				final BigDecimal subtotal = item.getProduct().getValue().multiply(new BigDecimal(item.getQuantity()));
				bir.setSubtotal(subtotal);
				listBagItemResponse.add(bir);
			});

			final BigDecimal total = listBagItemResponse.stream().map(BagItemResponse::getSubtotal)
					.reduce(BigDecimal::add).get();

			final ShoppingCartResponse sbr = new ShoppingCartResponse();
			sbr.setBagItemResponse(listBagItemResponse);
			sbr.setTotal(total);

			return ResponseEntity.ok(sbr);
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	@PostMapping
	public ResponseEntity<?> addProductToCart(@PathVariable String userId, @RequestBody ShoppingCartRequest req) {
		final Optional<User> user = this.userRepo.findById(userId);

		if (!user.isPresent()) {
			logger.debug("user not found for id " + userId);
			final Map<String, String> err = new HashMap<String, String>();
			err.put("message", "User not found");
			return ResponseEntity.unprocessableEntity().body(err);
		}

		if (req.getProductId() == null) {
			logger.debug("parameter productId is null");
			final Map<String, String> err = new HashMap<String, String>();
			err.put("message", "Product Id is required");
			return ResponseEntity.unprocessableEntity().body(err);
		} else {
			final Optional<Product> p = this.productRepo.findById(req.getProductId());
			if (!p.isPresent()) {
				logger.debug("product doest not exist");
				final Map<String, String> err = new HashMap<String, String>();
				err.put("message", "Product does not exist");
				return ResponseEntity.unprocessableEntity().body(err);
			}

			return ResponseEntity.ok(this.cartService.save(user.get(), p.get()));
		}
	}

	@DeleteMapping("/item/{itemId}")
	public ResponseEntity<?> removeCartProduct(@PathVariable String userId, @PathVariable String itemId) {
		final Optional<User> user = this.userRepo.findById(userId);

		if (!user.isPresent()) {
			logger.debug("user not found for id " + userId);
			final Map<String, String> err = new HashMap<String, String>();
			err.put("message", "User not found");
			return ResponseEntity.unprocessableEntity().body(err);
		}

		if (itemId.isEmpty()) {
			logger.debug("parameter itemId is null");
			final Map<String, String> err = new HashMap<String, String>();
			err.put("message", "Cart Item Id is required");
			return ResponseEntity.unprocessableEntity().body(err);
		} else {
			final BagItem bi = this.bagItemService.findById(itemId);
			if (bi == null) {
				logger.debug("Item doest not exist");
				final Map<String, String> err = new HashMap<String, String>();
				err.put("message", "Item does not exist");
				return ResponseEntity.unprocessableEntity().body(err);
			}

			this.bagItemService.removeItem(itemId);
			return ResponseEntity.noContent().build();
		}
	}
}
