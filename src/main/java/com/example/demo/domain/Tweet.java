package com.example.demo.domain;


import com.example.demo.enums.TweetStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import twitter4j.MediaEntity;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tweets")
public class Tweet {
	
	@Id
	private String id;
	
	private String text;

	private String searchtext;
	
	private String user;

	private String url_id;
	
	private String niche;

	private int RtCount;

	private int Fav_Count;
	
	private Date tweetedAt;

	private String userImage;

	private List<MediaEntity> media;
}
