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

import br.com.shopping.cart.backend.api.model.Product;
import br.com.shopping.cart.backend.api.repository.ProductRepository;

@RestController
@RequestMapping("products")
public class ProductController {

	private static Logger logger = LogManager.getLogger(ProductController.class);

	@Autowired
	private ProductRepository repo;

	@GetMapping
	public ResponseEntity<List<Product>> getAllProducts() {
		return ResponseEntity.ok(this.repo.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable String id) {
		final Optional<Product> prod = this.repo.findById(id);

		if (!prod.isPresent()) {
			logger.debug("product not found for id " + id);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return ResponseEntity.ok(prod.get());
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable String id, @RequestBody Product product) {
		final Optional<Product> prod = this.repo.findById(id);

		if (!prod.isPresent()) {
			logger.debug("product not found for id " + id);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (product.getName().isEmpty() || product.getValue() == null) {
			logger.debug("parameter name is null or value equals zero");
			final Map<String, String> err = new HashMap<String, String>();
			err.put("message", "All fields are required (name and value).");
			return ResponseEntity.unprocessableEntity().body(err);
		}

		product.setId(new ObjectId(id));
		this.repo.save(product);

		return ResponseEntity.ok(product);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable String id) {
		final Optional<Product> prod = this.repo.findById(id);

		if (!prod.isPresent()) {
			logger.debug("product not found for id " + id);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		this.repo.delete(prod.get());

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PostMapping
	public Product saveProduct(@RequestBody Product product) {
		return this.repo.save(product);
	}
}
