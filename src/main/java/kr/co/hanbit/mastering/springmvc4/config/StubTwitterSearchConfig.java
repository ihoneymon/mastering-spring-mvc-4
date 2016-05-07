package kr.co.hanbit.mastering.springmvc4.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import kr.co.hanbit.mastering.springmvc4.search.LightTweet;
import kr.co.hanbit.mastering.springmvc4.search.TwitterSearch;

@Configuration
@Profile("test")
public class StubTwitterSearchConfig {

    @Bean
    public TwitterSearch searchService() {
        return (searchType, keywords) -> Arrays.asList(new LightTweet("tweetText"), new LightTweet("secondTweet"));
    }
}
