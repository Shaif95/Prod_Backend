package com.example.demo.repository;

import com.example.demo.domain.Award;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AwardRepository extends MongoRepository<Award, String> {

}
