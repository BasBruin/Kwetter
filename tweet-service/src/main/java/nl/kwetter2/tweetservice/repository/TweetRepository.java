package nl.kwetter2.tweetservice.repository;


import nl.kwetter2.tweetservice.model.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
    Optional<Tweet> findByTitle(String title);
}