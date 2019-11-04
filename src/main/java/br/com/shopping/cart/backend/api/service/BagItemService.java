package br.com.shopping.cart.backend.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.shopping.cart.backend.api.model.BagItem;
import br.com.shopping.cart.backend.api.repository.BagItemRepository;

@Service
public class BagItemService {

	@Autowired
	private BagItemRepository bagRepo;

	public BagItem findById(String itemId) {
		final Optional<BagItem> bi = this.bagRepo.findById(itemId);

		if (bi.isPresent()) {
			return bi.get();
		}

		return null;
	}

	public void removeItem(String itemId) {
		final Optional<BagItem> bi = this.bagRepo.findById(itemId);

		if (bi.isPresent()) {
			this.bagRepo.delete(bi.get());
		}
	}
}
