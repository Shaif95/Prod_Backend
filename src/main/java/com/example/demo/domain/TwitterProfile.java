package com.example.demo.domain;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Data
@Document(collection = "TwitterProf")
public class TwitterProfile {

    @Id
    private String id;
    private String user;   // user name
    private String image;  //user image



}
