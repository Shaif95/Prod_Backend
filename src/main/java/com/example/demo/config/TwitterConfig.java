package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

@Configuration
public class TwitterConfig {

	@Value("${twitter.consumer.key}")
	private String consumerKey;

	@Value("${twitter.consumer.secret}")
	private String consumerSecret;

	@Value("${twitter.access.token}")
	private String accessToken;

	@Value("${twitter.access.secret}")
	private String accessSecret;



	private ConfigurationBuilder getConfigurationBuilder() {
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setDebugEnabled(true).setOAuthConsumerKey(consumerKey)
				.setOAuthConsumerSecret(consumerSecret).setOAuthAccessToken(accessToken)
				.setOAuthAccessTokenSecret(accessSecret);

		return configurationBuilder;
	}

	@Bean
	public Twitter getTwitterInstance() {
		ConfigurationBuilder configurationBuilder = getConfigurationBuilder();

		TwitterFactory twitterFactory = new TwitterFactory(configurationBuilder.build());
		return twitterFactory.getInstance();
	}

	@Bean
	public TwitterStream getTwitterStream() {
		ConfigurationBuilder configurationBuilder = getConfigurationBuilder();

		return new TwitterStreamFactory(configurationBuilder.build()).getInstance();
	}


}
