package nl.kwetter2.authenticationservice.repository;

import nl.kwetter2.authenticationservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);
    User findByEmailAndPassword(String email, String password);
    User findByEmail(String email);
}
