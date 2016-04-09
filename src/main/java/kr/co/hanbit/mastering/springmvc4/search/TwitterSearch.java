package kr.co.hanbit.mastering.springmvc4.search;

import java.util.List;

public interface TwitterSearch {

    List<LightTweet> search(String searchType, List<String> keywords);

}