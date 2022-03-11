package com.example.demo.repository;

import com.example.demo.domain.Tweet;
import com.example.demo.enums.TweetStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TweetRepository extends MongoRepository<Tweet, String> {

    Page<Tweet> findByNiche(String niche, PageRequest pageRequest, Sort tweetedAt);


}
