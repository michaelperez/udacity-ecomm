package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);

    private User user;
    private Cart cart;
    private Item item;

    @Before
    public void setUp() {
        orderController = new OrderController(userRepository, orderRepository);
        TestUtils.injectObjects(orderController,"userRepository", userRepository);
        TestUtils.injectObjects(orderController,"orderRepository", orderRepository);

        user = new User();
        user.setId(1L);
        user.setUsername("user1");
        user.setPassword("TestPassword1");

        item = new Item();
        item.setId(1L);
        item.setName("Hammer");
        item.setPrice(BigDecimal.valueOf(10.99));
        item.setDescription("16oz Claw Hammer");

        cart = new Cart();
        cart.setId(1L);
        cart.setItems(Arrays.asList(item));
        cart.setUser(user);
        cart.setTotal(BigDecimal.valueOf(10.99));

        user.setCart(cart);
    }

    @Test
    public void submitOrder() throws Exception {
        when(userRepository.findByUsername("user1")).thenReturn(user);
        ResponseEntity<UserOrder> response = orderController.submit("user1");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        UserOrder order = response.getBody();
        assertNotNull(order);
        assertEquals(BigDecimal.valueOf(10.99), order.getTotal());
        assertEquals("user1", order.getUser().getUsername());
    }

    @Test
    public void getOrdersForUser() throws Exception {
        when(userRepository.findByUsername("user1")).thenReturn(user);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("user1");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }
}
