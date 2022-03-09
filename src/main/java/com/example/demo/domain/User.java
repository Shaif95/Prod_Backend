package com.example.demo.domain;

import lombok.Data;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "user")
public class User {

    @Id
    private String id;
    private String user;   // email from login_user
    private String userid;  //username
    private Long count;


    private String token;
    private String token_secret;

    private String status;

    private String screen_api;
    private String prof_image;

    private String top_weekly;
    private String top_monthly;

    private Integer rt_weekly;
    private Integer rt_monthly;

    private Integer fav_weekly;
    private Integer fav_monthly;

}
