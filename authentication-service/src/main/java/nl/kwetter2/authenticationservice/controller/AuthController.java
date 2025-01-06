package nl.kwetter2.authenticationservice.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import nl.kwetter2.authenticationservice.exception.EmailAlreadyExistsException;
import nl.kwetter2.authenticationservice.exception.InvalidCredentialsException;
import nl.kwetter2.authenticationservice.model.AuthenticationRequest;
import nl.kwetter2.authenticationservice.model.AuthenticationResponse;
import nl.kwetter2.authenticationservice.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody AuthenticationRequest authRequest, HttpServletResponse response) {
        try {
            AuthenticationResponse authResponse = authenticationService.register(authRequest);

            // Stel een HttpOnly cookie in voor het access token
            Cookie accessToken = new Cookie("accessToken", authResponse.getAccessToken());
            accessToken.setHttpOnly(true); // Maakt het ontoegankelijk voor JavaScript

            response.addCookie(accessToken);
            accessToken.setMaxAge(60 * 60);

            return ResponseEntity.ok().body("Registratie succesvol");
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email bestaat al");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthenticationRequest authRequest, HttpServletResponse response) {
        try {
            AuthenticationResponse authResponse = authenticationService.login(authRequest);

            Cookie accessToken = new Cookie("accessToken", authResponse.getAccessToken());
            accessToken.setHttpOnly(true); // Maakt het ontoegankelijk voor JavaScript
            accessToken.setMaxAge(60 * 60);
            response.addCookie(accessToken);

            return ResponseEntity.ok().body("Login succesvol");
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ongeldige e-mail/wachtwoord combinatie.");
        }
    }
}
