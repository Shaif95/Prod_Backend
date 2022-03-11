package com.example.demo.service;

import com.example.demo.config.TwitterConfig;
import com.example.demo.domain.Niche;
import com.example.demo.domain.Tweet;
import com.example.demo.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import twitter4j.*;


import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class TweetService {

	@Autowired
	private Twitter twitter;

	@Autowired
	private NicheService nicheService;

	@Autowired
	private TweetRepository tweetRepository;

	@Autowired
	private TwitterStream twitterStream;

	@Autowired
	private TwitterConfig twitterConfig;

	@Value("${niche.saas.names}")
	private String saas;

	public List<Tweet> findAll(String niche) {
		List<Tweet> tweets = tweetRepository.findAll();

		for (int i = 0, j = tweets.size() - 1; i < j; i++) {
			tweets.add(i, tweets.remove(j));
		}

		return tweets;

	}



	public List<Tweet> streamTweets( String niche ) throws TwitterException {

		String[] accounts = {};

		if (niche.equals("saas"))
		{
			accounts = saas.split(",");
		}

		List<Tweet> tweets = new ArrayList<Tweet>();

		for (int i =0;i<accounts.length;i++) {

			System.out.println(accounts[i]);

			Twitter twitter = twitterConfig.getTwitterInstance();
			Query query = new Query("from:" + accounts[i]+ " +exclude:retweets"+
					" +exclude:replies" +" +exclude:links" );

			query.setCount(50000000);

			query.setSince("2016-02-05");

			QueryResult result = twitter.search(query);

			for (Status status : result.getTweets()) {


				Tweet tweet = Tweet.builder()
						.text(status.getText())
						.url_id(String.valueOf(status.getId()))
						.user(status.getUser().getScreenName())
						.userImage(status.getUser().getProfileImageURL())
						.niche("advice1")
						.RtCount(status.getRetweetCount())
						.Fav_Count(status.getFavoriteCount())
						.tweetedAt(status.getCreatedAt())
						.build();

				//System.out.println(status.getText());


				if (tweet.getRtCount() > 10000) {
					tweets.add(tweet);
				}
				else if (tweet.getFav_Count() > 10000) {
					tweets.add(tweet);
				}



			}
		}

		Collections.shuffle(tweets);
		return tweetRepository.saveAll(tweets);
	}


	public Map<String, Object> findTweetsWithPaging(String niche, int page, int size) {
		Page<Tweet> tweets = tweetRepository.findAll(PageRequest.of(page, size));
		Map<String, Object> tweetsMap = new HashMap<>();
		tweetsMap.put("tweets", tweets.getContent());
		tweetsMap.put("pages", tweets.getTotalPages());
		tweetsMap.put("totalTweets", tweets.getTotalElements());


		return tweetsMap;

	}



	public Map<String, Object> findTweetsWithStatus(String niche, int page, int size) {

		Page<Tweet> tweets = tweetRepository.findByNiche(niche,PageRequest.of(page, size), Sort.by("tweetedAt").ascending());
		Map<String, Object> tweetsMap = new HashMap<>();

		tweetsMap.put("tweets", tweets.getContent());
		tweetsMap.put("pages", tweets.getTotalPages());
		tweetsMap.put("totalTweets", tweets.getTotalElements());


		return tweetsMap;

	}

	public List<Tweet> fetchTweets(String niche) throws TwitterException {

		Niche niche_type = nicheService.findById(niche);

		String list = niche_type.getNames();
		String[] types = list.split(",");

		List<Tweet> tweets = new ArrayList<Tweet>();

		for (int i =0;i<types.length;i++) {

			// The factory instance is re-useable and thread safe.
			Twitter twitter = twitterConfig.getTwitterInstance();
			Query query = new Query(types[i]+ " +exclude:retweets"+
					" +exclude:replies" +" +exclude:links" );

			query.setLang("en");

			query.setCount(50000000);

			query.setSince("2010-02-05");

			QueryResult result = twitter.search(query);


			for (Status status : result.getTweets()) {

				if(true) {

					Tweet tweet = Tweet.builder()
							.text(status.getText())
							.url_id(String.valueOf(status.getId()))
							.user(status.getUser().getScreenName())
							.userImage(status.getUser().getProfileImageURL())
							.niche("us_politics2")
							.RtCount(status.getRetweetCount())
							.Fav_Count(status.getFavoriteCount())
							.tweetedAt(status.getCreatedAt())
							.build();


					if (tweet.getRtCount() > 10000000) {
						tweets.add(tweet);
					}
					else if (tweet.getFav_Count() > 100000) {
						tweets.add(tweet);
					}

				}


			}
		}

		return tweetRepository.saveAll(tweets);
	}



	public List<Tweet> fetchAccount(  ) throws TwitterException {


		Instant now = Instant.now();
		Instant yesterday = now.minus(10, ChronoUnit.DAYS);
		//System.out.println(now);
		String date = now.toString().substring(0,10);

		String[] accounts = { "Sabirtweets8"};

		List<Tweet> tweets = new ArrayList<Tweet>();

		for (int i =0;i<accounts.length;i++) {

			Twitter twitter = twitterConfig.getTwitterInstance();
			Query query = new Query("from:" + accounts[i]);
			QueryResult result = twitter.search(query);


			for (Status status : result.getTweets()) {

				String te = status.getText();

				Integer st = te.indexOf("@");

				Integer end = te.indexOf(":");

				Tweet tweet = Tweet.builder()
						.text(te.substring(end,te.length()))
						.url_id(String.valueOf(status.getId()))
						.user(te.substring(st+1,end))
						.userImage(status.getUser().getProfileImageURL())
						.niche("Sabirtweets8")
						.RtCount(status.getRetweetCount())
						.Fav_Count(status.getFavoriteCount())
						.tweetedAt(status.getCreatedAt())
						.build();

				//System.out.println(status.getText());

				tweets.add(tweet);



			}
		}


		return tweetRepository.saveAll(tweets);

	}


	public Tweet changeById(String id) {

		Tweet t = tweetRepository.findById(id).get();

		t.setNiche("misc1");

		return tweetRepository.save(t);
	}

	public Tweet changeToSkipById(String id) {

		Tweet t = tweetRepository.findById(id).get();

		t.setNiche("del1");

		return tweetRepository.save(t);
	}

	public Tweet changeCatById(String id, String c) {

		Tweet t = tweetRepository.findById(id).get();

		t.setNiche(c);

		return tweetRepository.save(t);
	}

	public Tweet getById(String id) {
		return tweetRepository.findById(id).get();
	}

}
