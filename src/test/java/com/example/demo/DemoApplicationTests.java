package com.example.demo;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.usrService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

@SpringBootTest
class DemoApplicationTests {


	@Autowired
	private UserRepository userRepository;

	@Value("${twitter.consumer.key}")
	private String consumerKey;

	@Value("${twitter.consumer.secret}")
	private String consumerSecret;

	@Test
	void contextLoads() {
	}
/*
    @Test
	public void post() throws IOException, ServletException, TwitterException {


		String id = new String("1");

		User user = userRepository.findById(id).get();

		RequestToken requestToken = null;

		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setDebugEnabled(true).setOAuthConsumerKey(consumerKey)
				.setOAuthConsumerSecret(consumerSecret).setOAuthAccessToken(user.getToken())
				.setOAuthAccessTokenSecret(user.getToken_secret());

		TwitterFactory twitterFactory = new TwitterFactory(configurationBuilder.build());

		Twitter twitter = twitterFactory.getInstance();

		Status status = twitter.updateStatus("Test");

		System.out.println("done!!");

	}

*/
}
