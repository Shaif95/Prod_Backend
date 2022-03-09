package com.example.demo.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "niche")
public class Niche {

    @Id
    private String id;
    private String names;      //description
}
