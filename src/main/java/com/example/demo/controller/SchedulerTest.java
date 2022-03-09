package com.example.demo.controller;

import com.example.demo.config.TwitterConfig;
import com.example.demo.domain.Tweet;
import com.example.demo.domain.TweetText;
import com.example.demo.domain.loginuser;
import com.example.demo.enums.TweetStatus;
import com.example.demo.repository.Login_Repository;
import com.example.demo.repository.TweetRepository;
import com.example.demo.repository.TweetTextRepository;
import com.example.demo.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;
import twitter4j.*;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class SchedulerTest {


    @Autowired
    private Login_Repository login_repository;

    @Autowired
    private TweetService tweetService;

    @Autowired
    private TwitterStream twitterStream;

    @Autowired
    private TwitterConfig twitterConfig;

    @Autowired
    private com.example.demo.service.usrService userService;

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private com.example.demo.service.TweetTextService tweetTextService;

    @Autowired
    private TweetTextRepository tweetTextRepository;

    @org.springframework.beans.factory.annotation.Value("${people.engage.names}")
    private String acs;


    // Scheduler

    @Scheduled(cron ="0 */1 * * * ?")
   public void schl() throws TwitterException, ServletException, IOException, InterruptedException {

        System.out.println("HI......................................");

       Instant now = Instant.now();

       //System.out.println(now);
       Instant min_1 = now.plus(1,ChronoUnit.MINUTES);
       Instant min_2 = now.plus(2,ChronoUnit.MINUTES);
       Instant min_3 = now.plus(3,ChronoUnit.MINUTES);
       Instant min_4 = now.plus(4,ChronoUnit.MINUTES);

       String date = now.toString().substring(2,19);
       String date1 = min_1.toString().substring(2,19);
       String date2 = min_2.toString().substring(2,19);
       String date3 = min_3.toString().substring(2,19);
       String date4 = min_4.toString().substring(2,19);

       find(date,0);
       find(date1,1);
       find(date2,2);
       //find(date3,3);
       //find(date4,4);


       System.out.println(date);
       //System.out.println(date1);

       System.out.println("........................................");

    }

    private void find(String date, Integer k) throws ServletException, IOException, TwitterException, InterruptedException {
       List<TweetText> tweets = tweetTextRepository.findBydatetime(date,TweetStatus.PENDING);

       System.out.println(tweets);

       for(int i =0; i<tweets.size();i++)
       {
           //Thread.sleep((k*1000*60));

           TweetText t = tweets.get(i);

           userService.post(t.getUserid(),t.getText());

           t.setStatus(TweetStatus.SENT);

           TweetText t1 = tweetTextService.update(tweets.get(i).getId(),t);
       }

    }

    // Engagement

    @Scheduled(cron ="0 0 */1 * * ?")
    public void name() throws TwitterException {

        System.out.println("every hour");

       List<loginuser> Allusers = new ArrayList<>();

       Allusers = login_repository.findAll();

       for (int i = 0 ; i <Allusers.size(); i++)
       {
           loginuser lu = Allusers.get(i);

           if(lu.getFollowSet()!=null)
           {
               fetch(lu.getFollowSet(),lu.getId());
           }
       }


        Instant now = Instant.now();
        Instant yesterday = now.minus(1, ChronoUnit.DAYS);
        //System.out.println(now);
        String date = now.toString().substring(0,10);



        String[] accounts = {};

        accounts = acs.split(",");


        List<Tweet> tweets = new ArrayList<Tweet>();

        for (int i =0;i<accounts.length;i++) {

            System.out.println(accounts[i]);

            Twitter twitter = twitterConfig.getTwitterInstance();
            Query query = new Query("from:" + accounts[i]+ " +exclude:retweets"+ " +exclude:replies").since(date);

            QueryResult result = twitter.search(query);

            for (Status status : result.getTweets()) {


                Tweet tweet = Tweet.builder()
                        .text(status.getText())
                        .url_id(String.valueOf(status.getId()))
                        .user(status.getUser().getScreenName())
                        .userImage(status.getUser().getProfileImageURL())
                        .niche("engage")
                        .RtCount(status.getRetweetCount())
                        .Fav_Count(status.getFavoriteCount())
                        .build();

                tweets.add(tweet);



            }
        }

        Collections.shuffle(tweets);
        tweetRepository.saveAll(tweets);

    }

    private void fetch(List<String> followSet, String id) throws TwitterException {

        Instant now = Instant.now();
        Instant yesterday = now.minus(1, ChronoUnit.DAYS);
        //System.out.println(now);
        String date = now.toString().substring(0,10);

        String[] accounts = followSet.toArray(new String[followSet.size()]);

        accounts = acs.split(",");


        List<Tweet> tweets = new ArrayList<Tweet>();

        for (int i =0;i<accounts.length;i++) {

            System.out.println(accounts[i]);

            Twitter twitter = twitterConfig.getTwitterInstance();
            Query query = new Query("from:" + accounts[i]+ " +exclude:retweets"+ " +exclude:replies").since(date);

            QueryResult result = twitter.search(query);

            for (Status status : result.getTweets()) {


                Tweet tweet = Tweet.builder()
                        .text(status.getText().toString())
                        .url_id(String.valueOf(status.getId()))
                        .user(status.getUser().getScreenName())
                        .userImage(status.getUser().getProfileImageURL())
                        .niche(id+"engage")
                        .RtCount(status.getRetweetCount())
                        .Fav_Count(status.getFavoriteCount())
                        .build();

                tweets.add(tweet);



            }
        }

        Collections.shuffle(tweets);
        tweetRepository.saveAll(tweets);



    }


}