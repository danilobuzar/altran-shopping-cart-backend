package br.com.shopping.cart.backend.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.shopping.cart.backend.api.model.ShoppingCart;
import br.com.shopping.cart.backend.api.model.User;

public interface ShoppingCartRepository extends MongoRepository<ShoppingCart, String> {

	ShoppingCart findByUser(User user);

}
