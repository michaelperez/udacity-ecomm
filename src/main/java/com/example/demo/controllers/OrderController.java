package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);

	private UserRepository userRepository;

	private OrderRepository orderRepository;

	public OrderController(UserRepository userRepository, OrderRepository orderRepository) {
		this.userRepository = userRepository;
		this.orderRepository = orderRepository;
	}
	
	
	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		LOG.info("Submit order for user {}.",username);

		User user = userRepository.findByUsername(username);
		if(user == null) {
			LOG.error("User {} not found.", username);
			return ResponseEntity.notFound().build();
		}

		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);
		LOG.info("User order {} has been successfully saved.", order.getId());
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		LOG.info("Get orders for user {}.",username);

		User user = userRepository.findByUsername(username);
		if(user == null) {
			LOG.error("User {} not found.", username);
			return ResponseEntity.notFound().build();
		}

		List<UserOrder> orders = orderRepository.findByUser(user);
		LOG.info("{} orders were found.", orders.size());
		return ResponseEntity.ok(orders);
	}
}
