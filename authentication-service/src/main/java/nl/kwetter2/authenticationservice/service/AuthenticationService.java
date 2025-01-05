package nl.kwetter2.authenticationservice.service;

import lombok.RequiredArgsConstructor;
import nl.kwetter2.authenticationservice.exception.InvalidCredentialsException;
import nl.kwetter2.authenticationservice.model.AuthenticationRequest;
import nl.kwetter2.authenticationservice.model.AuthenticationResponse;
import nl.kwetter2.authenticationservice.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse register(AuthenticationRequest authRequest) {
        authRequest.setPassword(passwordEncoder.encode(authRequest.getPassword()));

        User newUser = new User();
        newUser.setEmail(authRequest.getEmail());
        newUser.setPassword(authRequest.getPassword());

        User user = customUserDetailsService.save(newUser);
        Assert.notNull(user, "Failed to register user. Please try again later");

        String accessToken = jwtService.generate(user, "ACCESS");
        String refreshToken = jwtService.generate(user, "REFRESH");

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public AuthenticationResponse login(AuthenticationRequest authRequest) {
        User user = customUserDetailsService.findByEmail(authRequest.getEmail());
        if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            String accessToken = jwtService.generate(user, "ACCESS");
            String refreshToken = jwtService.generate(user, "REFRESH");

            return new AuthenticationResponse(accessToken, refreshToken);
        }
        throw new InvalidCredentialsException("Ongeldige e-mail/wachtwoord combinatie.");
    }
}