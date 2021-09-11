package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController(userRepository, cartRepository, encoder);
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController,"bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createValidUser() throws Exception {
        when(encoder.encode("TestPassword1")).thenReturn("StubbedPassword");
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("user1");
        userRequest.setPassword("TestPassword1");
        userRequest.setConfirmPassword("TestPassword1");

        final ResponseEntity<User> response = userController.createUser(userRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0,user.getId());
        assertEquals("user1", user.getUsername());
        assertEquals("StubbedPassword", user.getPassword());
    }

    @Test
    public void createUserPasswordLengthRequirement() throws Exception {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("user2");
        userRequest.setPassword("pw");
        userRequest.setConfirmPassword("pw");
        final ResponseEntity<User> userResponseEntity = userController.createUser(userRequest);
        assertEquals(400, userResponseEntity.getStatusCodeValue());
    }

    @Test
    public void createUserPasswordMatchRequirement() throws Exception {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("user3");
        userRequest.setPassword("TestPassword3");
        userRequest.setConfirmPassword("TestPasswrd3");
        final ResponseEntity<User> userResponseEntity = userController.createUser(userRequest);
        assertEquals(400, userResponseEntity.getStatusCodeValue());
    }

    @Test
    public void findByIdNotFound() throws Exception {
        final ResponseEntity<User> response = userController.findById(1L);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void findByUserNameNotFound() throws Exception {
        final ResponseEntity<User> response = userController.findByUserName("user100");
        assertEquals(404, response.getStatusCodeValue());
    }
}
