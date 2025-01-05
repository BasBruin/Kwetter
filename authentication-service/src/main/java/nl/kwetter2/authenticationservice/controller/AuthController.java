package nl.kwetter2.authenticationservice.controller;

import lombok.RequiredArgsConstructor;
import nl.kwetter2.authenticationservice.exception.EmailAlreadyExistsException;
import nl.kwetter2.authenticationservice.exception.InvalidCredentialsException;
import nl.kwetter2.authenticationservice.model.AuthenticationRequest;
import nl.kwetter2.authenticationservice.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody AuthenticationRequest authRequest) {
        try {
            return ResponseEntity.ok(authenticationService.register(authRequest));
        } catch (EmailAlreadyExistsException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email bestaat al");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthenticationRequest authRequest) {
        try {
            return ResponseEntity.ok(authenticationService.login(authRequest));
        } catch (InvalidCredentialsException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ongeldige e-mail/wachtwoord combinatie.");
        }
    }
}
