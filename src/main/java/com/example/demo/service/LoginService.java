package com.example.demo.service;


import com.example.demo.config.TwitterConfig;
import com.example.demo.domain.Tweet;
import com.example.demo.domain.User;
import com.example.demo.domain.loginuser;
import com.example.demo.repository.Login_Repository;
import com.example.demo.repository.TweetRepository;
import com.example.demo.repository.TweetTextRepository;
import com.example.demo.utils.CustomBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import twitter4j.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class LoginService {

    @Autowired
    private Login_Repository login_repository;

    @Autowired
    private TwitterConfig twitterConfig;

    @Autowired
    private TweetRepository tweetRepository;

    @Value("${twitter.consumer.key}")
    private String consumerKey;

    @Value("${twitter.consumer.secret}")
    private String consumerSecret;

    public loginuser add(loginuser user) {
        return login_repository.save(user);
    }

    public Map<String, Object> findWithPaging(Integer page, Integer size) {
        Page<loginuser> users = login_repository.findAll(PageRequest.of(page, size));

        Map<String, Object> usersMap = new HashMap<>();
        usersMap.put("users", users.getContent());
        usersMap.put("pages", users.getTotalPages());

        return usersMap;
    }

    public List<loginuser> findByEmail(String id) {
        return  login_repository.findByUser(id);
    }


    public loginuser updateByEmail(String id, loginuser updatedUser) {

        loginuser user = login_repository.findByUser(id).get(0);

        CustomBeanUtils.copyNonNullProperties(updatedUser, user);

        return login_repository.save(user);

    }

    public loginuser update(String id, loginuser updatedUser) {

        loginuser user = login_repository.findById(id).get();

        CustomBeanUtils.copyNonNullProperties(updatedUser, user);

        return login_repository.save(user);

    }

    public loginuser updateFollowSet(String id, String acs) throws TwitterException {

        loginuser user = login_repository.findById(id).get();

        String [] fset = acs.split(",");

        user.setFollowSet(Arrays.asList(fset));


        Instant now = Instant.now();
        Instant yesterday = now.minus(2, ChronoUnit.DAYS);
        //System.out.println(now);
        String date = now.toString().substring(0,10);

        String[] accounts = acs.split(",");


        List<Tweet> tweets = new ArrayList<Tweet>();

        for (int i =0;i<accounts.length;i++) {

            System.out.println(accounts[i]);

            Twitter twitter = twitterConfig.getTwitterInstance();
            Query query = new Query("from:" + accounts[i]+ " +exclude:replies"+ " +exclude:retweets").since(date);

            query.setCount(1000);

            QueryResult result = twitter.search(query);

            for (Status status : result.getTweets()) {


                Tweet tweet = Tweet.builder()
                        .text(status.getText().toString())
                        .url_id(String.valueOf(status.getId()))
                        .user(status.getUser().getScreenName())
                        .userImage(status.getUser().getProfileImageURL())
                        .niche(user.getId()+"engage")
                        .RtCount(status.getRetweetCount())
                        .Fav_Count(status.getFavoriteCount())
                        .build();

                tweets.add(tweet);



            }
        }

        Collections.shuffle(tweets);
        tweetRepository.saveAll(tweets);

        return login_repository.save(user);
    }

    public loginuser findById(String id) {
        return login_repository.findById(id).get();
    }

    public void delete(String id) {
        login_repository.deleteById(id);
    }

    public String reset() {

        List<loginuser> users = login_repository.findAll();

        for(int i =0; i<users.size();i++)
        {
            loginuser u = users.get(i);
            u.setStatus("false");
           login_repository.save(u);
        }

        return "Done";
    }
}
