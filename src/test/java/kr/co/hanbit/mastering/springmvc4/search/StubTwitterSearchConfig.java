package kr.co.hanbit.mastering.springmvc4.search;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import kr.co.hanbit.mastering.springmvc4.search.LightTweet;
import kr.co.hanbit.mastering.springmvc4.search.TwitterSearch;

@Configuration
public class StubTwitterSearchConfig {

    @Bean
    public TwitterSearch twitterSearch() {
        return (searchType, keywords) -> Arrays.asList(new LightTweet("tweetText"), new LightTweet("secondTweet"));
    }
}
