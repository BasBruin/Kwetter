package nl.kwetter2.tweetservice.mappers;

import nl.kwetter2.tweetservice.model.Tweet;
import nl.kwetter2.tweetservice.model.TweetDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TweetMapper {

    public List<TweetDTO> toTweetDTOList(List<Tweet> tweets) {
        List<TweetDTO> tweetDTOList = new ArrayList<>();
        for (Tweet tweet : tweets) {
            tweetDTOList.add(toTweetDTO(tweet));
        }
        return tweetDTOList;
    }

    public List<Tweet> toTweetList(List<TweetDTO> tweetDTOList) {
        List<Tweet> tweetList = new ArrayList<>();
        for (TweetDTO tweetDTO : tweetDTOList) {
            tweetList.add(toTweet(tweetDTO));
        }
        return tweetList;
    }

    public TweetDTO toTweetDTO(Tweet tweet) {
        TweetDTO tweetDTO = new TweetDTO();
        tweetDTO.setId(tweet.getId());
        tweetDTO.setTitle(tweet.getTitle());
        tweetDTO.setBody(tweet.getBody());
        return tweetDTO;
    }

    public Tweet toTweet(TweetDTO tweetDTO) {
        Tweet tweet = new Tweet();
        tweet.setId(tweetDTO.getId());
        tweet.setTitle(tweetDTO.getTitle());
        tweet.setBody(tweetDTO.getBody());
        return tweet;
    }

}
