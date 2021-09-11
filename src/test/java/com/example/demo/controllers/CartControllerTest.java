package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    private User user;
    private Cart cart;
    private Item item;

    @Before
    public void setUp() {
        cartController = new CartController(userRepository, cartRepository, itemRepository);
        TestUtils.injectObjects(cartController,"userRepository", userRepository);
        TestUtils.injectObjects(cartController,"cartRepository", cartRepository);
        TestUtils.injectObjects(cartController,"itemRepository", itemRepository);

        user = new User();
        user.setId(1L);
        user.setUsername("user1");
        user.setPassword("TestPassword1");

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(new ArrayList<Item>());
        cart.setTotal(BigDecimal.valueOf(0.00));

        user.setCart(cart);

        item = new Item();
        item.setId(1L);
        item.setName("Hammer");
        item.setPrice(BigDecimal.valueOf(10.99));
        item.setDescription("16oz Claw Hammer");
    }

    @Test
    public void addToCart() throws Exception {
        when(userRepository.findByUsername("user1")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("user1");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(5);

        ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart savedCart = response.getBody();
        assertNotNull(savedCart);
        assertNotNull(savedCart.getItems());
        assertEquals(5, savedCart.getItems().size());
        assertEquals(BigDecimal.valueOf(54.95), response.getBody().getTotal());
    }

    @Test
    public void removeFromCart() throws Exception {
        when(userRepository.findByUsername("user1")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(5);
        modifyCartRequest.setUsername(user.getUsername());
        ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);
        assertEquals(200, response.getStatusCodeValue());

        ModifyCartRequest cartRequestModified = new ModifyCartRequest();
        cartRequestModified.setItemId(1L);
        cartRequestModified.setQuantity(1);
        cartRequestModified.setUsername(user.getUsername());
        response = cartController.removeFromCart(cartRequestModified);
        Cart savedCart = response.getBody();
        assertNotNull(savedCart);
        assertNotNull(savedCart.getItems());
        assertEquals(4, savedCart.getItems().size());
        assertEquals(BigDecimal.valueOf(43.96), response.getBody().getTotal());
    }
}
