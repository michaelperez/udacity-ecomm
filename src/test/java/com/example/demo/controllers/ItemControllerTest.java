package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    private Item item;

    @Before
    public void setUp() {
        itemController = new ItemController(itemRepository);
        TestUtils.injectObjects(itemController,"itemRepository", itemRepository);

        item = new Item();
        item.setId(1L);
        item.setName("Hammer");
        item.setPrice(BigDecimal.valueOf(10.99));
        item.setDescription("16oz Claw Hammer");
    }

    @Test
    public void getItems() throws Exception {
        when(itemRepository.findAll()).thenReturn(Arrays.asList(item));
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void getItemById() throws Exception {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Item savedItem = response.getBody();
        assertNotNull(savedItem);
        assertEquals("Hammer", savedItem.getName());
        assertEquals("16oz Claw Hammer", savedItem.getDescription());
        assertEquals(BigDecimal.valueOf(10.99), savedItem.getPrice());
    }

    @Test
    public void getItemByName() throws Exception {
        when(itemRepository.findByName("Hammer")).thenReturn(Arrays.asList(item));
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Hammer");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Hammer",response.getBody().get(0).getName());
    }
}
