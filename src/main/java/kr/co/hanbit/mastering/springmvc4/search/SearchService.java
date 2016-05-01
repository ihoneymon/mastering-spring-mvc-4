package kr.co.hanbit.mastering.springmvc4.search;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import kr.co.hanbit.mastering.springmvc4.search.cache.SearchCache;

@Service
@Profile("!async")
public class SearchService implements TwitterSearch {

    private SearchCache searchCache;

    @Autowired
    public void setSearchCache(SearchCache searchCache) {
        this.searchCache = searchCache;
    }

    @Override
    public List<LightTweet> search(String searchType, List<String> keywords) {
        return keywords.stream().flatMap(keyword -> searchCache.fetch(searchType, keyword).stream())
                .collect(Collectors.toList());
    }
}
