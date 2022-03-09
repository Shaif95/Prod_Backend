package com.example.demo.repository;

import com.example.demo.domain.User;
import com.example.demo.domain.loginuser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface Login_Repository extends MongoRepository<loginuser, String>  {

    List<loginuser> findByUser(String user);
}
