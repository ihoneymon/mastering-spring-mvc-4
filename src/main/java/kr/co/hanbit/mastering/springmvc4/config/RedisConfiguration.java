package kr.co.hanbit.mastering.springmvc4.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@Profile("redis")
@EnableRedisHttpSession
public class RedisConfiguration {

    @Bean(name = "objectRedisTemplate")
    public RedisTemplate objectRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Primary
    @Bean
    public CacheManager cacheManager(@Qualifier("objectRedisTemplate") RedisTemplate template) {
        RedisCacheManager cacheManager = new RedisCacheManager(template);
        cacheManager.setCacheNames(Arrays.asList("searches"));
        cacheManager.setDefaultExpiration(36_000);
        return cacheManager;
    }

    @Bean
    @Profile("heroku")
    public RedisConnectionFactory redisConnectionFactory() throws URISyntaxException {
        JedisConnectionFactory redis = new JedisConnectionFactory();
        String redisUrl = System.getenv("REDIS_URL");
        URI redisUri = new URI(redisUrl);
        redis.setHostName(redisUri.getHost());
        redis.setPort(redisUri.getPort());
        redis.setPassword(redisUri.getUserInfo().split(":", 2)[1]);
        return redis;
    }

    @Bean
    @Profile({ "cloud", "heroku" })
    public static ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }
}
