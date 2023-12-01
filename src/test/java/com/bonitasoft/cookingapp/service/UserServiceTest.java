package com.bonitasoft.cookingapp.service;

import com.bonitasoft.cookingapp.entity.User;
import com.bonitasoft.cookingapp.respository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testSaveUser() {
        User mockUser = new User(); // Create a mock user

        // Mocking userRepository.save() method
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        // Call the UserService save method
        User savedUser = userService.save(mockUser);

        // Verify that userRepository.save() was called and returned the mock user
        verify(userRepository, times(1)).save(mockUser);
        assertEquals(mockUser, savedUser);
    }

    @Test
    public void testFindUser() {
        String username = "testUser"; // Mocking a username
        User mockUser = new User(); // Create a mock user

        // Mocking userRepository.getByUsername() method
        when(userRepository.getByUsername(username)).thenReturn(mockUser);

        // Call the UserService findUser method
        User foundUser = userService.findUser(username);

        // Verify that userRepository.getByUsername() was called and returned the mock user
        verify(userRepository, times(1)).getByUsername(username);
        assertEquals(mockUser, foundUser);
    }
}
