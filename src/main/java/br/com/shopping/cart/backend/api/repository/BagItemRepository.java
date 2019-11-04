package br.com.shopping.cart.backend.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.shopping.cart.backend.api.model.BagItem;
import br.com.shopping.cart.backend.api.model.Product;

public interface BagItemRepository extends MongoRepository<BagItem, String> {

	BagItem findByProduct(Product p);

}
