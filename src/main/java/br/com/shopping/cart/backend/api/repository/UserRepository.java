package br.com.shopping.cart.backend.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.shopping.cart.backend.api.model.User;

public interface UserRepository extends MongoRepository<User, String> {

	User findByEmail(String email);

}
