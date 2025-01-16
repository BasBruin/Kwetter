package nl.kwetter2.authenticationservice.service;

import nl.kwetter2.authenticationservice.exception.EmailAlreadyExistsException;
import nl.kwetter2.authenticationservice.exception.InvalidCredentialsException;
import nl.kwetter2.authenticationservice.model.User;
import nl.kwetter2.authenticationservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class CustomUserDetailsServiceTest {

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User("test@example.com", "encodedPassword");
        testUser.setUserId(1);
    }

    @Test
    void testSave_Success() {
        when(userRepository.existsByEmail(testUser.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User savedUser = customUserDetailsService.save(testUser);

        assertNotNull(savedUser);
        assertEquals(testUser.getEmail(), savedUser.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testSave_Fail_EmailAlreadyExists() {
        when(userRepository.existsByEmail(testUser.getEmail())).thenReturn(true);

        Exception exception = assertThrows(EmailAlreadyExistsException.class, () -> {
            customUserDetailsService.save(testUser);
        });

        assertEquals("Email address already exists.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testFindByEmailAndPassword_Success() {
        when(userRepository.findByEmailAndPassword(testUser.getEmail(), testUser.getPassword())).thenReturn(testUser);

        User foundUser = customUserDetailsService.findByEmailAndPassword(testUser.getEmail(), testUser.getPassword());

        assertNotNull(foundUser);
        assertEquals(testUser.getEmail(), foundUser.getEmail());
    }

    @Test
    void testFindByEmailAndPassword_Fail_InvalidCredentials() {
        when(userRepository.findByEmailAndPassword(testUser.getEmail(), "wrongPassword")).thenReturn(null);

        Exception exception = assertThrows(InvalidCredentialsException.class, () -> {
            customUserDetailsService.findByEmailAndPassword(testUser.getEmail(), "wrongPassword");
        });

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void testFindById_Success() throws IllegalAccessException {
        when(userRepository.findById(testUser.getUserId())).thenReturn(java.util.Optional.of(testUser));

        User foundUser = customUserDetailsService.findById(testUser.getUserId());

        assertNotNull(foundUser);
        assertEquals(testUser.getUserId(), foundUser.getUserId());
    }

    @Test
    void testFindById_Fail_UserNotFound() {
        when(userRepository.findById(testUser.getUserId())).thenReturn(java.util.Optional.empty());

        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            customUserDetailsService.findById(testUser.getUserId());
        });

        assertEquals("User not found", exception.getMessage());
    }
}
