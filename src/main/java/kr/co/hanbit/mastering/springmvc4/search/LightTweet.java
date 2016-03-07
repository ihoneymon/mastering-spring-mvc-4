package kr.co.hanbit.mastering.springmvc4.search;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;

import lombok.Data;

@Data
public class LightTweet {
    private String profileImageUrl;
    private String user;
    private String text;
    private LocalDateTime date;
    private String lang;
    private Integer retweetCount;

    public LightTweet(String text) {
        super();
        this.text = text;
    }

    public static LightTweet ofTweet(Tweet tweet) {
        LightTweet lightTweet = new LightTweet(tweet.getText());
        Date createdAt = tweet.getCreatedAt();

        if (createdAt != null) {
            lightTweet.date = LocalDateTime.ofInstant(createdAt.toInstant(), ZoneId.systemDefault());
        }

        TwitterProfile tweetUser = tweet.getUser();
        if (tweetUser != null) {
            lightTweet.user = tweetUser.getName();
            lightTweet.profileImageUrl = tweetUser.getProfileBannerUrl();
        }

        lightTweet.lang = tweet.getLanguageCode();
        lightTweet.retweetCount = tweet.getRetweetCount();
        return lightTweet;
    }

    // 접근자getter 생성하는 것을 잊지 마세요.
    // 접근자는 잭슨Jackson이 객체를 직렬화하는데 사용합니다.
}
