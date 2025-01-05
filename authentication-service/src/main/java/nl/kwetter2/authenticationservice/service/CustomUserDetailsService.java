package nl.kwetter2.authenticationservice.service;

import lombok.RequiredArgsConstructor;
import nl.kwetter2.authenticationservice.exception.EmailAlreadyExistsException;
import nl.kwetter2.authenticationservice.exception.InvalidCredentialsException;
import nl.kwetter2.authenticationservice.model.User;

import nl.kwetter2.authenticationservice.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public User save(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {

            throw new EmailAlreadyExistsException("Email address already exists.");
        }
        return userRepository.save(user);
    }

    public User findByEmailAndPassword(String email, String password) {
        var user = userRepository.findByEmailAndPassword(email, password);
        if (user == null) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        return user;
    }

    public User findById(Integer id) throws IllegalAccessException {
        var user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new IllegalAccessException("User not found");
        }
    }

    public User findByEmail(String email) {
        var user = userRepository.findByEmail(email);
        if (user == null) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        return user;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
