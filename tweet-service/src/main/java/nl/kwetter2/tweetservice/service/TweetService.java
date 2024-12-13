package nl.kwetter2.tweetservice.service;

import lombok.RequiredArgsConstructor;
import nl.kwetter2.tweetservice.model.CreateRequest;
import nl.kwetter2.tweetservice.model.Tweet;
import nl.kwetter2.tweetservice.publisher.TweetPublisher;
import nl.kwetter2.tweetservice.repository.TweetRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class TweetService {

    public TweetService(TweetRepository tweetRepository, TweetPublisher tweetPublisher){
        this.tweetRepository = tweetRepository;
        this.tweetPublisher = tweetPublisher;
    }

    private final TweetRepository tweetRepository;
    private final TweetPublisher tweetPublisher;



    public Tweet create(CreateRequest request) {
        Tweet tweet = new Tweet();
        tweet.setTitle(request.getTitle());
        tweet.setBody(request.getBody());


        Tweet savedTweet = tweetRepository.save(tweet);
        Assert.notNull(savedTweet, "Error creating Tweet.");

        tweetPublisher.publishTweet("Tweet created: Title: " + tweet.getTitle() + " Body: " + tweet.getBody());

        return savedTweet;
    }

    public Tweet findByTitle(String title) {
        return tweetRepository.findByTitle(title).orElseThrow(() -> new RuntimeException("Tweet not found"));
    }

}
