package br.com.shopping.cart.backend.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.shopping.cart.backend.api.model.BagItem;
import br.com.shopping.cart.backend.api.model.Product;
import br.com.shopping.cart.backend.api.model.ShoppingCart;
import br.com.shopping.cart.backend.api.model.User;
import br.com.shopping.cart.backend.api.repository.BagItemRepository;
import br.com.shopping.cart.backend.api.repository.ProductRepository;
import br.com.shopping.cart.backend.api.repository.ShoppingCartRepository;

@Service
public class ShoppingCartService {

	@Autowired
	private ShoppingCartRepository cartRepo;

	@Autowired
	private BagItemRepository bagRepo;

	@Autowired
	private ProductRepository productRepo;

	@Transactional
	public Optional<ShoppingCart> findOne(String id) {
		return this.cartRepo.findById(id);
	}

	@Transactional
	public ShoppingCart findAllByUser(User user) {
		return this.cartRepo.findByUser(user);
	}

	@Transactional
	public ShoppingCart save(User user, final Product p) {
		ShoppingCart userSc = this.cartRepo.findByUser(user);
		final Product product = this.productRepo.findById(p.getId()).get();

		if (userSc != null) {
			final Optional<BagItem> bi = userSc.getBagItems().stream().filter(item -> {
				return item.getProduct().equals(product);
			}).findFirst();

			if (bi.isPresent()) {
				bi.get().setQuantity(bi.get().getQuantity() + 1);
				this.bagRepo.save(bi.get());

			} else {
				final BagItem bagItem = new BagItem();
				bagItem.setProduct(product);
				bagItem.setQuantity(1);
				userSc.getBagItems().add(this.bagRepo.save(bagItem));
			}

		} else {
			final List<BagItem> items = new ArrayList<BagItem>();
			final BagItem bi = new BagItem();
			bi.setProduct(product);
			bi.setQuantity(1);
			items.add(this.bagRepo.save(bi));

			userSc = new ShoppingCart(user, items);
		}

		return this.cartRepo.save(userSc);
	}

	public void removeProduct(User user, final Product p) {
		final Product product = this.productRepo.findById(p.getId()).get();
		final BagItem bi = this.bagRepo.findByProduct(product);

		this.bagRepo.delete(bi);
	}

	public void removeAllProducts(User user) {
		final ShoppingCart userSc = this.cartRepo.findByUser(user);
		this.cartRepo.delete(userSc);
	}
}
