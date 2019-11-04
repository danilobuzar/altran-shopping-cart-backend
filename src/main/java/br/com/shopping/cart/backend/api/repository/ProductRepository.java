package br.com.shopping.cart.backend.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.shopping.cart.backend.api.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

}
