package nl.kwetter2.authenticationservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import nl.kwetter2.authenticationservice.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private User user;

    private static final String SECRET_KEY = "your-secret-key";
    private static final String TEST_TOKEN = "test-token";

    @BeforeEach
    void setUp() {
        // Mock the user object
        user = new User("test@example.com", "encodedPassword");
        user.setUserId(1);
    }

    @Test
    void testGenerateToken() {
        String accessToken = jwtService.generate(user, "ACCESS");
        assertNotNull(accessToken);
        assertFalse(accessToken.isEmpty());
    }

    @Test
    void testValidateToken_Valid() {
        // Mock a valid token
        String validToken = Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + 100000))
                .compact();

        Boolean isValid = jwtService.validateToken(validToken);
        assertTrue(isValid);
    }

    @Test
    void testValidateToken_Expired() {
        // Mock an expired token
        String expiredToken = Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() - 100000))
                .compact();

        Boolean isValid = jwtService.validateToken(expiredToken);
        assertFalse(isValid);
    }

    @Test
    void testGetClaimsFromToken() {
        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + 100000))
                .compact();

        Claims claims = jwtService.getAllClaimsFromToken(token);

        assertNotNull(claims);
        assertEquals(user.getEmail(), claims.getSubject());
    }

    @Test
    void testGetExpirationDateFromToken() {
        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + 100000))
                .compact();

        Date expirationDate = jwtService.getExpirationDateFromToken(token);

        assertNotNull(expirationDate);
        assertTrue(expirationDate.after(new Date()));
    }
}
