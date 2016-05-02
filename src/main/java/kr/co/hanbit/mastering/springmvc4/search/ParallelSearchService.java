package kr.co.hanbit.mastering.springmvc4.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import kr.co.hanbit.mastering.springmvc4.search.cache.SearchCache;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Profile("async")
public class ParallelSearchService implements TwitterSearch {
    private final AsyncSearch asyncSearch;

    @Autowired
    public ParallelSearchService(AsyncSearch asyncSearch) {
        this.asyncSearch = asyncSearch;
    }

    @Override
    public List<LightTweet> search(String searchType, List<String> keywords) {
        CountDownLatch latch = new CountDownLatch(keywords.size());
        List<LightTweet> allTweets = Collections.synchronizedList(new ArrayList<>());
        keywords.stream().forEach(keyword -> asyncFetch(latch, allTweets, searchType, keyword));
        await(latch);
        return allTweets;
    }

    private void asyncFetch(CountDownLatch latch, List<LightTweet> allTweets, String searchType, String keyword) {
        asyncSearch.asyncFetch(searchType, keyword).addCallback(tweets -> onSuccess(allTweets, latch, tweets),
                ex -> onError(latch, ex));
    }

    private void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void onSuccess(List<LightTweet> results, CountDownLatch latch, List<LightTweet> tweets) {
        results.addAll(tweets);
        latch.countDown();
    }

    private static void onError(CountDownLatch latch, Throwable ex) {
        log.error("Occur Exception: {}", ex);
        latch.countDown();
    }

    @Slf4j
    @Component
    private static class AsyncSearch {
        private SearchCache searchCache;

        @Autowired
        public AsyncSearch(SearchCache searchCache) {
            this.searchCache = searchCache;
        }

        @Async
        public ListenableFuture<List<LightTweet>> asyncFetch(String searchType, String keyword) {
            log.info("{} - Searching for {}", Thread.currentThread().getName(), keyword);
            return new AsyncResult<>(searchCache.fetch(searchType, keyword));
        }
    }
}
