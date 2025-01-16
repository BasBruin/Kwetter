package nl.kwetter2.authenticationservice.service;

import nl.kwetter2.authenticationservice.exception.InvalidCredentialsException;
import nl.kwetter2.authenticationservice.model.AuthenticationRequest;
import nl.kwetter2.authenticationservice.model.AuthenticationResponse;
import nl.kwetter2.authenticationservice.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_Success() {
        AuthenticationRequest authRequest = new AuthenticationRequest("test@example.com", "password123");
        User savedUser = new User("test@example.com", "encodedPassword");
        savedUser.setUserId(1);

        when(passwordEncoder.encode(authRequest.getPassword())).thenReturn("encodedPassword");
        when(customUserDetailsService.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generate(savedUser, "ACCESS")).thenReturn("accessToken");
        when(jwtService.generate(savedUser, "REFRESH")).thenReturn("refreshToken");

        AuthenticationResponse response = authenticationService.register(authRequest);

        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());

        verify(passwordEncoder).encode("password123");
        verify(customUserDetailsService).save(any(User.class));
    }

    @Test
    void testRegister_Fail_UserNotSaved() {
        AuthenticationRequest authRequest = new AuthenticationRequest("test@example.com", "password123");

        when(passwordEncoder.encode(authRequest.getPassword())).thenReturn("encodedPassword");
        when(customUserDetailsService.save(any(User.class))).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            authenticationService.register(authRequest);
        });

        assertEquals("Failed to register user. Please try again later", exception.getMessage());
    }

    @Test
    void testLogin_Success() {
        AuthenticationRequest authRequest = new AuthenticationRequest("test@example.com", "password123");
        User user = new User("test@example.com", "encodedPassword");

        when(customUserDetailsService.findByEmail(authRequest.getEmail())).thenReturn(user);
        when(passwordEncoder.matches(authRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtService.generate(user, "ACCESS")).thenReturn("accessToken");
        when(jwtService.generate(user, "REFRESH")).thenReturn("refreshToken");

        AuthenticationResponse response = authenticationService.login(authRequest);

        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());

        verify(customUserDetailsService).findByEmail("test@example.com");
        verify(passwordEncoder).matches("password123", "encodedPassword");
    }

    @Test
    void testLogin_Fail_InvalidCredentials() {
        AuthenticationRequest authRequest = new AuthenticationRequest("test@example.com", "wrongPassword");
        User user = new User("test@example.com", "encodedPassword");

        when(customUserDetailsService.findByEmail(authRequest.getEmail())).thenReturn(user);
        when(passwordEncoder.matches(authRequest.getPassword(), user.getPassword())).thenReturn(false);

        Exception exception = assertThrows(InvalidCredentialsException.class, () -> {
            authenticationService.login(authRequest);
        });

        assertEquals("Ongeldige e-mail/wachtwoord combinatie.", exception.getMessage());

        verify(customUserDetailsService).findByEmail("test@example.com");
        verify(passwordEncoder).matches("wrongPassword", "encodedPassword");
    }
}

