package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	private static final Logger LOG = LoggerFactory.getLogger(CartController.class);

	private UserRepository userRepository;

	private CartRepository cartRepository;

	private ItemRepository itemRepository;

	public CartController(UserRepository userRepository, CartRepository cartRepository, ItemRepository itemRepository) {
		this.userRepository = userRepository;
		this.cartRepository = cartRepository;
		this.itemRepository = itemRepository;
	}

	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addToCart(@RequestBody ModifyCartRequest request) {
		LOG.info("Add item {} to cart.",request.getItemId());

		User user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			LOG.error("User {} not found.", request.getUsername());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			LOG.error("Item {} not found.", request.getItemId());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
				.forEach(i -> cart.addItem(item.get()));
		cartRepository.save(cart);
		LOG.info("Total quantity of {} item(s) saved to cart.", request.getQuantity());
		return ResponseEntity.ok(cart);
	}
	
	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromCart(@RequestBody ModifyCartRequest request) {
		LOG.info("Remove item {} from cart.",request.getItemId());

		User user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			LOG.error("User {} not found.", request.getUsername());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			LOG.error("Item {} not found.", request.getItemId());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
				.forEach(i -> cart.removeItem(item.get()));
		cartRepository.save(cart);
		LOG.info("Total quantity of {} item(s) removed from cart.", request.getQuantity());
		return ResponseEntity.ok(cart);
	}
		
}
