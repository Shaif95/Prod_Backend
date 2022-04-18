package com.example.demo.service;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import com.example.demo.config.TwitterConfig;
import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.CustomBeanUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import javax.servlet.ServletException;

@Service
public class usrService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TwitterConfig twitterConfig;

    @Value("${twitter.consumer.key}")
    private String consumerKey;

    @Value("${twitter.consumer.secret}")
    private String consumerSecret;

    @Value("${twitter.consumer_post.key}")
    private String Post_consumerKey;

    @Value("${twitter.consumer_post.secret}")
    private String Post_consumerSecret;


    public User add(User user) {
        return userRepository.save(user);
    }

    public Map<String, Object> findWithPaging(Integer page, Integer size) {
        Page<User> users = userRepository.findAll(PageRequest.of(page, size));

        Map<String, Object> usersMap = new HashMap<>();
        usersMap.put("users", users.getContent());
        usersMap.put("pages", users.getTotalPages());

        return usersMap;
    }

    public User Oauth(String id, String tokenr, String secret, String pin) throws IOException, ServletException, TwitterException {

        User user = userRepository.findById(id).get();


        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(true).setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret);

        RequestToken requestToken = new RequestToken(tokenr,secret);

        TwitterFactory twitterFactory = new TwitterFactory(configurationBuilder.build());

        Twitter twitter = twitterFactory.getInstance();

        AccessToken token  = null;
        try
        {
            token = twitter.getOAuthAccessToken(requestToken, pin);
        }
        catch (TwitterException e)
        {
            System.out.println("Was there a typo in the PIN you entered?");
            e.printStackTrace();
            System.exit(-1);
        }

        twitter.setOAuthAccessToken(token);

        user = storeAccessToken(user,twitter.verifyCredentials().getId() , token);

        return userRepository.save(user);

    }

        public String auth(String id) throws IOException, ServletException, TwitterException {

        RequestToken requestToken = null;

        User u = stat(id,"false");


            ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
            configurationBuilder.setDebugEnabled(true).setOAuthConsumerKey(consumerKey)
                    .setOAuthConsumerSecret(consumerSecret);

            TwitterFactory twitterFactory = new TwitterFactory(configurationBuilder.build());

            Twitter twitter = twitterFactory.getInstance();

            String callBack = "oob";

        try
        {
            requestToken = twitter.getOAuthRequestToken(callBack);
        }
        catch (Exception e)
        {
            System.out.println("Ah, well, there's an error with Twitter.  Are the consumer key and secret correct?");
            e.printStackTrace();
            System.exit(-1);
        }

        String url = requestToken.getAuthorizationURL() + " " + requestToken;

        return url;


    }

    private User storeAccessToken(User user, Long useId, AccessToken accessToken){
        user.setUserid(useId.toString());
        user.setToken(accessToken.getToken());
        user.setToken_secret( accessToken.getTokenSecret());
        return  user;
    }



    public String post(String id, String tweet) throws IOException, ServletException, TwitterException {

        User user = userRepository.findById(id).get();

        RequestToken requestToken = null;

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(true).setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret).setOAuthAccessToken(user.getToken())
                .setOAuthAccessTokenSecret(user.getToken_secret());

        TwitterFactory twitterFactory = new TwitterFactory(configurationBuilder.build());

        Twitter twitter = twitterFactory.getInstance();

        Status status = twitter.updateStatus(tweet);

        return new String("Successfully updated the status to " + status.getText() );

    }

    public String reply(String id, String tweetid, String replyms) throws IOException, ServletException, TwitterException {

        Map<String, Object> jsonToMap = new ObjectMapper().readValue(replyms, Map.class);

        String rlp = (String) jsonToMap.get("reply");

        User user = userRepository.findById(id).get();

        RequestToken requestToken = null;

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(true).setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret).setOAuthAccessToken(user.getToken())
                .setOAuthAccessTokenSecret(user.getToken_secret());

        TwitterFactory twitterFactory = new TwitterFactory(configurationBuilder.build());

        Twitter twitter = twitterFactory.getInstance();

 Status status = twitter.showStatus(Long.parseLong(tweetid));
 Status reply = twitter.updateStatus(new StatusUpdate(" @" + status.getUser().getScreenName() + " "+ rlp ).inReplyToStatusId(status.getId()));

        return new String("Successfully updated the status to " + status.getText() );

    }


    public User update(String id, User updatedUser) {

        User user = userRepository.findById(id).get();

        CustomBeanUtils.copyNonNullProperties(updatedUser, user);

        return userRepository.save(user);

    }

    public User stat(String id, String st) {

        User user = userRepository.findById(id).get();

        user.setStatus(st);

        return userRepository.save(user);

    }

    public User findById(String id) {
        return userRepository.findById(id).get();
    }

    public List<User> findByEmail(String id) {
        return  userRepository.findByUser(id);
    }

    public void delete(String id) {
        userRepository.deleteById(id);
    }

    public User analysis(String id, String dur) throws TwitterException {

        User user = userRepository.findById(id).get();

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(true).setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret).setOAuthAccessToken(user.getToken())
                .setOAuthAccessTokenSecret(user.getToken_secret());

        TwitterFactory twitterFactory = new TwitterFactory(configurationBuilder.build());

        Twitter twitter = twitterFactory.getInstance();



        if(dur.equals("month"))
        {
            user = getMonth(twitter,user);
        }
        else if(dur.equals("week"))
        {
            user = getWeek(twitter,user);
        }


        return userRepository.save(user);

    }

    private User getMonth(Twitter twitter, User user) throws TwitterException {

        Integer rt_mon=0;

        Integer fav_mon=0;

        String name = twitter.getScreenName();

        Instant now = Instant.now();
        Instant yesterday = now.minus(30, ChronoUnit.DAYS);
        //System.out.println(now);
        //Date checkDate = Date.from(yesterday);

        String checkDate = yesterday.toString().substring(0,10);

        System.out.println(".............................................................");
        System.out.println(checkDate);


        Query query = new Query("from:" + name + "exclude:retweets"+ " +exclude:replies").since(checkDate);
        QueryResult result = twitter.search(query);

        Status st = null;
        Integer count = 0;

        System.out.println("Showing home profile.");
        for (Status status : result.getTweets()) {

            rt_mon = rt_mon + status.getRetweetCount();
            fav_mon = fav_mon + status.getFavoriteCount();

            if(status.getFavoriteCount()>count)
            {
                count = status.getFavoriteCount();
            }
            st = status;

        }

        user.setFav_monthly(fav_mon);
        user.setRt_monthly(rt_mon);

        return  user;

    }

    private User getWeek(Twitter twitter, User user) throws TwitterException {

        Integer rt_weekly=0;

        Integer fav_weekly=0;

        String name = twitter.getScreenName();

        Instant now = Instant.now();
        Instant yesterday = now.minus(7, ChronoUnit.DAYS);
        //System.out.println(now);
        //Date checkDate = Date.from(yesterday);

        String checkDate = yesterday.toString().substring(0,10);

        System.out.println(".............................................................");
        System.out.println(checkDate);


        Query query = new Query("from:" + name ).since(checkDate);
        QueryResult result = twitter.search(query);

        Status st = null;
        Integer count = 0;

        System.out.println("Showing home profile.");
        for (Status status : result.getTweets()) {

         rt_weekly = rt_weekly + status.getRetweetCount();
         fav_weekly = fav_weekly + status.getFavoriteCount();

         if(status.getFavoriteCount()>count)
         {
             count = status.getFavoriteCount();
         }
         st = status;

        }

        user.setFav_weekly(fav_weekly);
        user.setRt_weekly(rt_weekly);

        return  user;
    }

    public String reset() {

        List<User> users = userRepository.findAll();

                for(int i =0; i<users.size();i++)
                {
                    User u = users.get(i);
                    u.setStatus("false");
                    userRepository.save(u);
                }

                return "Done";
    }
}
