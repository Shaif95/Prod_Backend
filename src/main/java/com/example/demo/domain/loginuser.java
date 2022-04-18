package com.example.demo.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@Data
@Document(collection = "login_user")

public class loginuser {

    @Id
    private String id;
    private String user_email;
    private String password;
    private String user;
    private String status;
    private String Timezone;
    private String Timezone_status;

    private List<String> followSet;

    private List<String> GrowthHub;

    private String sub_status;
    private String reg_date;
    private String end_date;
    private String trial;
    private String sub_type;

    private long plan_id;
    private String cancel_id;

    private List<String> nicheSet;

    private List<String> userSet;

}