package nl.kwetter2.tweetservice.controller;

import lombok.AllArgsConstructor;
import nl.kwetter2.tweetservice.mappers.TweetMapper;
import nl.kwetter2.tweetservice.model.CreateRequest;
import nl.kwetter2.tweetservice.model.Tweet;
import nl.kwetter2.tweetservice.model.TweetDTO;
import nl.kwetter2.tweetservice.repository.TweetRepository;
import nl.kwetter2.tweetservice.service.TweetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tweets")
public class TweetController {

    public TweetController(TweetService tweetService, TweetRepository tweetRepository, TweetMapper tweetMapper){
        this.tweetMapper = tweetMapper;
        this.tweetService = tweetService;
        this.tweetRepository = tweetRepository;
    }
    private final TweetService tweetService;
    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;

    @GetMapping("/all")
    public List<TweetDTO> getAllTweets() {
        List<Tweet> allTweets = tweetRepository.findAll();
        return tweetMapper.toTweetDTOList(allTweets);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody CreateRequest createRequest) {
        try {
            return ResponseEntity.ok(tweetService.create(createRequest));
        } catch(Exception ee) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
