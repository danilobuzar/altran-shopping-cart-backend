package br.com.shopping.cart.backend.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.shopping.cart.backend.api.model.User;
import br.com.shopping.cart.backend.api.repository.UserRepository;

@RestController
@RequestMapping("users")
public class UserController {
	private static Logger logger = LogManager.getLogger(UserController.class);

	@Autowired
	private UserRepository repo;

	@GetMapping
	public ResponseEntity<List<User>> getAllUsers() {
		return ResponseEntity.ok(this.repo.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable String id) {
		final Optional<User> prod = this.repo.findById(id);

		if (!prod.isPresent()) {
			logger.debug("user not found for id " + id);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return ResponseEntity.ok(prod.get());
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User user) {
		final Optional<User> prod = this.repo.findById(id);

		if (!prod.isPresent()) {
			logger.debug("user not found for id " + id);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (user.getName().isEmpty() || user.getEmail().isEmpty()) {
			logger.debug("parameter name or email is null");
			final Map<String, String> err = new HashMap<String, String>();
			err.put("message", "All fields are required (name and email).");
			return ResponseEntity.unprocessableEntity().body(err);
		}

		user.setId(new ObjectId(id));
		this.repo.save(user);

		return ResponseEntity.ok(user);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable String id) {
		final Optional<User> user = this.repo.findById(id);

		if (!user.isPresent()) {
			logger.debug("user not found for id " + id);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		this.repo.delete(user.get());

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PostMapping
	public ResponseEntity<?> saveUser(@RequestBody User userReq) {
		final User user = this.repo.findByEmail(userReq.getEmail());

		if (user != null) {
			logger.debug("user already exists");
			final Map<String, String> err = new HashMap<String, String>();
			err.put("message", "User already exists.");
			return ResponseEntity.unprocessableEntity().body(err);
		}

		return ResponseEntity.ok(this.repo.save(userReq));
	}
}
