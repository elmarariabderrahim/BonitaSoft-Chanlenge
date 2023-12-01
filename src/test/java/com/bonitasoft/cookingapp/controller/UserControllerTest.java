package com.bonitasoft.cookingapp.controller;


import com.bonitasoft.cookingapp.entity.AuthRequest;
import com.bonitasoft.cookingapp.entity.User;
import com.bonitasoft.cookingapp.service.UserService;
import com.bonitasoft.cookingapp.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.bonitasoft.cookingapp.controller.UserController;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserController userController;

    @Test
    public void testAddUser_Success() {
        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setPassword("password123"); // Assuming plain password is set

        // Mocking behavior for userService.findUser() to return null, indicating user doesn't exist
        when(userService.findUser("newUser")).thenReturn(null);

        // Mock password encoding behavior
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        // Mock userService.save() method to return the created user
        when(userService.save(any(User.class))).thenReturn(newUser);

        // Test the addUser method
        ResponseEntity<?> responseEntity = userController.welcome(newUser);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(newUser, responseEntity.getBody());

        // Verify that userService.save() is called once with the newUser object
        verify(userService, times(1)).save(newUser);
    }

    @Test
    public void testAddUser_UserAlreadyExists() {
        User existingUser = new User();
        existingUser.setUsername("existingUser");

        // Mocking behavior for userService.findUser() to return an existing user
        when(userService.findUser("existingUser")).thenReturn(existingUser);

        // Test the addUser method when the user already exists
        ResponseEntity<?> responseEntity = userController.welcome(existingUser);

        // Assertions
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("Unable to create User, already exist !", responseEntity.getBody());

        // Verify that userService.findUser() is called once with the existingUser's username
        verify(userService, times(1)).findUser("existingUser");
    }


    @Test
    public void testGenerateToken_ValidCredentials() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("testUser");
        authRequest.setPassword("password123");

        // Mock successful authentication (no exception thrown)
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        )).thenReturn(null);

        // Mock JWT token generation
        when(jwtUtil.generateToken("testUser")).thenReturn("generatedToken");

        // Test the generateToken method
        String generatedToken = userController.generateToken(authRequest);

        // Assertions
        assertEquals("generatedToken", generatedToken);

        // Verify that authenticationManager.authenticate is called once with the authRequest
        verify(authenticationManager, times(1)).authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        // Verify that jwtUtil.generateToken is called once with the authRequest's username
        verify(jwtUtil, times(1)).generateToken("testUser");
    }

    @Test
    public void testGenerateToken_InvalidCredentials() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("testUser");
        authRequest.setPassword("invalidPassword");

        // Mock unsuccessful authentication (throw an exception)
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        )).thenThrow(new IllegalArgumentException("invalid username or password"));

        // Test the generateToken method when authentication fails
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(Exception.class,
                () -> userController.generateToken(authRequest));

        // Assertions
        assertEquals("invalid username or password", exception.getMessage());
        //assertThrows(Exception.class, () -> userController.generateToken(authRequest));
        // Verify that authenticationManager.authenticate is called once with the authRequest
        verify(authenticationManager, times(1)).authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
    }
}
