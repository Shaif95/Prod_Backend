package com.example.demo.repository;

import com.example.demo.domain.Award;
import com.example.demo.domain.TweetText;
import com.example.demo.domain.User;
import com.example.demo.enums.TweetStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

    List<User> findByUser(String user);

}
