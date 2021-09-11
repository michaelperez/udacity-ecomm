package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/item")
public class ItemController {

	private static final Logger LOG = LoggerFactory.getLogger(ItemController.class);

	private ItemRepository itemRepository;

	public ItemController(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}
	
	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		LOG.info("Get all items.");
		List<Item> items = itemRepository.findAll();
		LOG.info("{} items were found.", items.size());
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		LOG.info("Get item by id {}.", id);
		return ResponseEntity.of(itemRepository.findById(id));
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		LOG.info("Get items by item name {}.", name);
		List<Item> items = itemRepository.findByName(name);

		if (items == null || items.isEmpty()) {
			LOG.error("item is not valid or no items were found.");
		} else {
			LOG.info("{} items found for name.", items.size());
		}

		return items == null || items.isEmpty() ? ResponseEntity.notFound().build()
				: ResponseEntity.ok(items);
			
	}
	
}
